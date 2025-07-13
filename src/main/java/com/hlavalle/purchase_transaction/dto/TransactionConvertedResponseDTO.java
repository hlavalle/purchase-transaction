package com.hlavalle.purchase_transaction.dto;

import com.hlavalle.purchase_transaction.entity.Transaction;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Builder
@Getter
public class TransactionConvertedResponseDTO {

    private String id;
    private String description;
    private BigDecimal originalAmount;
    private OffsetDateTime timestamp;
    private BigDecimal exchangeRate;
    private String exchangeCurrency;
    private BigDecimal convertedAmount;

    public static TransactionConvertedResponseDTO fromEntity(
            Transaction transaction) {
        return TransactionConvertedResponseDTO.builder()
                .id(transaction.getId().toString())
                .description(transaction.getDescription())
                .originalAmount(transaction.getAmount())
                .timestamp(transaction.getTimestamp())
                .exchangeRate(transaction.getExchangeRate())
                .exchangeCurrency(transaction.getCurrency())
                .convertedAmount(transaction.getAmountConverted())
                .build();
    }
}
