package com.hlavalle.purchase_transaction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="transaction")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @Column(name= "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "description", length = 50)
    private String description;

    @Column(name= "timestamp")
    private OffsetDateTime timestamp;

    @Column(name="amount")
    private BigDecimal amount;

}
