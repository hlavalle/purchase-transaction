package com.hlavalle.purchase_transaction.service;

import com.hlavalle.purchase_transaction.dto.CurrencyConversionResponseDTO;
import com.hlavalle.purchase_transaction.entity.Transaction;
import com.hlavalle.purchase_transaction.exception.CurrencyConversionUnavailableException;
import com.hlavalle.purchase_transaction.exception.TransactionNotFoundException;
import com.hlavalle.purchase_transaction.repository.TransactionRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Log4j2
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CurrencyConversionService currencyConversionService;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, CurrencyConversionService currencyConversionService) {
        this.transactionRepository = transactionRepository;
        this.currencyConversionService = currencyConversionService;
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction convertCurrency(String transactionId, String currency) {

        Transaction transaction = transactionRepository.findById(UUID.fromString(transactionId))
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with id "+transactionId+" not found"));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        OffsetDateTime timestamp = transaction.getTimestamp();
        String gteDate = fmt.format(timestamp.minusMonths(6));
        String lteDate = fmt.format(timestamp);

        CurrencyConversionResponseDTO currencyConversionResponseDTO = currencyConversionService.convertCurrency(transaction.getAmount(), currency, gteDate, lteDate);

        if (currencyConversionResponseDTO.getData().isEmpty()) {
            throw new CurrencyConversionUnavailableException("The purchase cannot be converted to the target currency");
        }

        BigDecimal exchangeRate = new BigDecimal(currencyConversionResponseDTO.getData().get(0).getExchangeRate());
        BigDecimal amountConverted = transaction.getAmount().multiply(exchangeRate).setScale(2, RoundingMode.HALF_EVEN);
        transaction.setExchangeRate(exchangeRate);
        transaction.setCurrency(currency);
        transaction.setAmountConverted(amountConverted);
        return transaction;
    }

}
