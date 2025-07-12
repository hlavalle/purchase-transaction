package com.hlavalle.purchase_transaction.controller;

import com.hlavalle.purchase_transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Operation(summary = "List all transactions")
    @GetMapping("/api/v1/transaction")
    public ResponseEntity<List<TransactionResponseDTO>> listTransactions() {


        List<TransactionResponseDTO> transactionResponseDTOList =
                TransactionResponseDTO.fromEntityList(transactionService.getAllTransactions());

        return new ResponseEntity<>(transactionResponseDTOList, HttpStatus.OK);
    }

}
