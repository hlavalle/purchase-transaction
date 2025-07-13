package com.hlavalle.purchase_transaction.controller;

import com.hlavalle.purchase_transaction.dto.TransactionRequestDTO;
import com.hlavalle.purchase_transaction.dto.TransactionResponseDTO;
import com.hlavalle.purchase_transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Creates a transaction")
    @PostMapping("/api/v1/transaction")
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {

        TransactionResponseDTO transactionResponseDTO =
                TransactionResponseDTO.fromEntity(
                        transactionService.createTransaction(
                            TransactionRequestDTO.toEntity(transactionRequestDTO)));

        return new ResponseEntity<>(transactionResponseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Convert transaction currency")
    @GetMapping("/api/v1/transaction/currencyconversion/{transactionId}/{currency}")
    public ResponseEntity<TransactionResponseDTO> getTransactionCurrencyConversion(
            @PathVariable String transactionId,
            @PathVariable String currency
    ) {

        TransactionResponseDTO transactionResponseDTO = TransactionResponseDTO.fromEntity(
                transactionService.convertCurrency(transactionId, currency));

        return new ResponseEntity<>(transactionResponseDTO, HttpStatus.OK);
    }

}
