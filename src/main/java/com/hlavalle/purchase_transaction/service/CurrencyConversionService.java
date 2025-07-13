package com.hlavalle.purchase_transaction.service;

import com.hlavalle.purchase_transaction.dto.CurrencyConversionResponseDTO;

import java.math.BigDecimal;

public interface CurrencyConversionService {

    CurrencyConversionResponseDTO convertCurrency(BigDecimal amount, String currency, String gteDate, String lteDate);
}
