package com.hlavalle.purchase_transaction.dto;

import com.hlavalle.purchase_transaction.entity.Transaction;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDTO {

    @Size(max = 50, message = "Description size must not exceed 50 characters.")
    @Size(min = 1, message = "Description is mandatory.")
    private String description;

    private OffsetDateTime timestamp;

    @Positive(message = "The amount must be strictly positive.")
    private BigDecimal amount;

    public static Transaction toEntity(TransactionRequestDTO transactionDTO) {
        return Transaction.builder()
                .id(UUID.randomUUID())
                .description(transactionDTO.getDescription())
                .amount(transactionDTO.getAmount().setScale(2, RoundingMode.HALF_EVEN))
                .timestamp(transactionDTO.timestamp)
                .build();
    }

}
