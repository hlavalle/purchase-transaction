package com.hlavalle.purchase_transaction.dto;

import com.hlavalle.purchase_transaction.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class TransactionResponseDTO {

    private String id;
    private String description;
    private BigDecimal amount;
    private OffsetDateTime timestamp;

    public static TransactionResponseDTO fromEntity(
            Transaction transaction) {
        return TransactionResponseDTO.builder()
                .id(transaction.getId().toString())
                .description(transaction.getDescription())
                .amount(transaction.getAmount())
                .timestamp(transaction.getTimestamp())
                .build();
    }

    public static List<TransactionResponseDTO> fromEntityList(List<Transaction> transactionList) {
        List<TransactionResponseDTO> transactionResponseDTOList = new ArrayList<>();
        for (Transaction transaction:transactionList) {
            transactionResponseDTOList.add(TransactionResponseDTO.fromEntity(transaction));
        }
        return transactionResponseDTOList;
    }

}
