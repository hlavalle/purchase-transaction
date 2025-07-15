package com.hlavalle.purchase_transaction;

import com.hlavalle.purchase_transaction.entity.Transaction;
import com.hlavalle.purchase_transaction.exception.TransactionNotFoundException;
import com.hlavalle.purchase_transaction.service.TransactionService;
import org.junit.jupiter.api.DisplayName;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @DisplayName("POST /api/v1/transaction - Success")
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

    @Test
    @DisplayName("POST /api/v1/transaction - Bad request invalid description size")
    void testInvalidDescriptionOnCreateTransaction() throws Exception {

        String payload = """
                {
                  "description": "Very very very very very long Transaction description",
                  "amount": 100.89,
                  "timestamp": "2001-07-10T18:58:07.534276Z"
                }
                """;

        String exceptedResponse = """
                {
                  "errors": [
                    "Description size must not exceed 50 characters."
                  ]
                }
                """;

        mockMvc.perform(post("/api/v1/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(exceptedResponse));

    }

    @Test
    @DisplayName("POST /api/v1/transaction - Bad request amount not positive")
    void testAmountNotPositiveOnCreateTransaction() throws Exception {

        String payload = """
                {
                  "description": "Transaction description",
                  "amount": -100.89,
                  "timestamp": "2001-07-10T18:58:07.534276Z"
                }
                """;

        String exceptedResponse = """
                {
                  "errors": [
                    "The amount must be strictly positive."
                  ]
                }
                """;

        mockMvc.perform(post("/api/v1/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(exceptedResponse));
    }

    @Test
    @DisplayName("GET /api/v1/transaction/currencyconversion/{transactionId}/{currency} - Success")
    void testGetTransactionConversion() throws Exception {

        UUID id = UUID.fromString("4cdc356a-cba2-40af-8c2a-18ea99c57bf4");

        Transaction convertedTransaction = Transaction.builder()
                .id(id)
                .description("Transaction description")
                .timestamp(OffsetDateTime.parse("2001-07-10T18:58:07.534276Z"))
                .amount(new BigDecimal("1234.78"))
                .amountConverted(new BigDecimal("870.52"))
                .exchangeRate(new BigDecimal("0.705"))
                .currency("United Kingdom-Pound")
                .build();

        when(transactionService.convertCurrency(any(), any())).thenReturn(convertedTransaction);

        String exceptedResponse = """
                {
                  "id": "4cdc356a-cba2-40af-8c2a-18ea99c57bf4",
                  "description": "Transaction description",
                  "originalAmount": 1234.78,
                  "timestamp": "2001-07-10T18:58:07.534276Z",
                  "exchangeRate": 0.705,
                  "exchangeCurrency": "United Kingdom-Pound",
                  "convertedAmount": 870.52
                }
                """;

        mockMvc.perform(get("/api/v1/transaction/currencyconversion/{transactionId}/{currency}",
                        "4cdc356a-cba2-40af-8c2a-18ea99c57bf4","United Kingdom-Pound"))
                .andExpect(status().isOk())
                .andExpect(content().json(exceptedResponse));

    }

    @Test
    @DisplayName("GET /api/v1/transaction/currencyconversion/{transactionId}/{currency} - Not found")
    void testGetTransactionConversionTransactionNotFound() throws Exception {

         when(transactionService.convertCurrency(any(), any())).thenThrow(
                new TransactionNotFoundException("Transaction with id 323d1874-9fd2-462-9d83-2251c8c77180 not found")
        );

        String exceptedResponse = """
                {
                  "errors": [
                    "Transaction with id 323d1874-9fd2-462-9d83-2251c8c77180 not found"
                  ]
                }
                """;

        mockMvc.perform(get("/api/v1/transaction/currencyconversion/{transactionId}/{currency}",
                        "323d1874-9fd2-462-9d83-2251c8c77180","United Kingdom-Pound"))
                .andExpect(status().isNotFound())
                .andExpect(content().json(exceptedResponse));

    }

}
