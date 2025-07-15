package com.hlavalle.purchase_transaction;

import com.hlavalle.purchase_transaction.dto.CurrencyConversionResponseDTO;
import com.hlavalle.purchase_transaction.entity.Transaction;
import com.hlavalle.purchase_transaction.exception.CurrencyConversionUnavailableException;
import com.hlavalle.purchase_transaction.repository.TransactionRepository;
import com.hlavalle.purchase_transaction.service.CurrencyConversionService;
import com.hlavalle.purchase_transaction.service.TransactionServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class TransactionServiceTests {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private CurrencyConversionService currencyConversionService;

    @Test
    @DisplayName("Currency conversion service - Success")
    void testConvertTransaction() {

        UUID id = UUID.fromString("4cdc356a-cba2-40af-8c2a-18ea99c57bf4");

        Transaction transaction = Transaction.builder()
                .id(id)
                .description("Transaction description")
                .timestamp(OffsetDateTime.parse("2001-07-10T18:58:07.534276Z"))
                .amount(new BigDecimal("100.89"))
                .build();

        when(transactionRepository.findById(UUID.fromString("4cdc356a-cba2-40af-8c2a-18ea99c57bf4")))
                .thenReturn(Optional.of(transaction));

        CurrencyConversionResponseDTO.Data data = new CurrencyConversionResponseDTO.Data();
        data.setEffectiveDate("2025-03-31");
        data.setExchangeRate("0.772");
        CurrencyConversionResponseDTO currencyConversionResponseDTO =
                new CurrencyConversionResponseDTO();
        currencyConversionResponseDTO.setData(List.of(data));

        when(currencyConversionService.convertCurrency(any(),any(),any(),any())).
                thenReturn(currencyConversionResponseDTO);

        Transaction convertedTransaction = transactionService.convertCurrency(
                "4cdc356a-cba2-40af-8c2a-18ea99c57bf4", "United Kingdom-Pound");

        assertNotNull(convertedTransaction);
        assertEquals(new BigDecimal("77.89"), convertedTransaction.getAmountConverted());

    }

    @Test
    @DisplayName("Currency conversion service - Exception currency not available")
    void testCurrencyNotAvailableWhenConvertTransaction() {

        UUID id = UUID.fromString("4cdc356a-cba2-40af-8c2a-18ea99c57bf4");

        Transaction transaction = Transaction.builder()
                .id(id)
                .description("Transaction description")
                .timestamp(OffsetDateTime.parse("2001-07-10T18:58:07.534276Z"))
                .amount(new BigDecimal("100.89"))
                .build();

        when(transactionRepository.findById(UUID.fromString("4cdc356a-cba2-40af-8c2a-18ea99c57bf4")))
                .thenReturn(Optional.of(transaction));

        CurrencyConversionResponseDTO currencyConversionResponseDTO = new CurrencyConversionResponseDTO();
        currencyConversionResponseDTO.setData(new ArrayList<>());

        when(currencyConversionService.convertCurrency(any(),any(),any(),any())).
                thenReturn(currencyConversionResponseDTO);

        Exception exception = assertThrows(CurrencyConversionUnavailableException.class, () ->
                transactionService.convertCurrency(
                "4cdc356a-cba2-40af-8c2a-18ea99c57bf4", "United Kingdom-Pound"));

        String expectedMessage = "The purchase cannot be converted to the target currency";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

}
