package com.hlavalle.purchase_transaction.dto;

import com.hlavalle.purchase_transaction.entity.Transaction;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Builder
@Getter
public class TransactionResponseDTO {

    private String id;
    private String description;
    private BigDecimal originalAmount;
    private OffsetDateTime timestamp;
    private BigDecimal exchangeRate;
    private String exchangeCurrency;
    private BigDecimal convertedAmount;

    public static TransactionResponseDTO fromEntity(
            Transaction transaction) {
        return TransactionResponseDTO.builder()
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
