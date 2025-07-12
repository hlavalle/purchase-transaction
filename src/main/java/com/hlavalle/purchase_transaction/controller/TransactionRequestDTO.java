package com.hlavalle.purchase_transaction.controller;

import com.hlavalle.purchase_transaction.entity.Transaction;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDTO {

    private String description;
    private BigDecimal amount;

    public static Transaction toEntity(TransactionRequestDTO transactionDTO) {
        return Transaction.builder()
                .id(UUID.randomUUID())
                .description(transactionDTO.getDescription())
                .amount(transactionDTO.getAmount().setScale(2, RoundingMode.HALF_EVEN))
                .timestamp(OffsetDateTime.now())
                .build();
    }

}
