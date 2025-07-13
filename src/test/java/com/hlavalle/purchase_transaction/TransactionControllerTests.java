package com.hlavalle.purchase_transaction;

import com.hlavalle.purchase_transaction.entity.Transaction;
import com.hlavalle.purchase_transaction.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransactionControllerTests {

    @MockitoBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateTransaction() throws Exception {

        UUID id = UUID.fromString("4cdc356a-cba2-40af-8c2a-18ea99c57bf4");

        Transaction createdTransaction = Transaction.builder()
                .id(id)
                .description("Transaction description")
                .timestamp(OffsetDateTime.parse("2001-07-10T18:58:07.534276Z"))
                .amount(new BigDecimal("100.89"))
                .build();

        when(transactionService.createTransaction(any())).thenReturn(createdTransaction);

        String payload = """
                {
                  "description": "Transaction description",
                  "amount": 100.89,
                  "timestamp": "2001-07-10T18:58:07.534276Z"
                }
                """;

        String exceptedResponse = """
                {
                  "id": "4cdc356a-cba2-40af-8c2a-18ea99c57bf4",
                  "description": "Transaction description",
                  "amount": 100.89,
                  "timestamp": "2001-07-10T18:58:07.534276Z"
                }
                """;

        mockMvc.perform(post("/api/v1/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated())
                .andExpect(content().json(exceptedResponse));

    }
}
