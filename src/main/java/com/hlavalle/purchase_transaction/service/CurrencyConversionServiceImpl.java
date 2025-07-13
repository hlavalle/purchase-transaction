package com.hlavalle.purchase_transaction.service;

import com.hlavalle.purchase_transaction.dto.CurrencyConversionResponseDTO;
import com.hlavalle.purchase_transaction.exception.CurrencyConversionException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Service
@Log4j2
public class CurrencyConversionServiceImpl implements CurrencyConversionService {

    private final RestClient restClient;

    @Autowired
    public CurrencyConversionServiceImpl(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    @Override
    public CurrencyConversionResponseDTO convertCurrency(BigDecimal amount, String currency) {

        String url = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange?filter=currency:eq:Real,record_date:eq:2001-06-30&fields=record_date,effective_date,exchange_rate";

        CurrencyConversionResponseDTO currencyConversionResponseDTO;
        ResponseEntity<CurrencyConversionResponseDTO> responseEntity;
        try {
            responseEntity = restClient.get()
                    .uri(url)
                    .retrieve()
                    .toEntity(CurrencyConversionResponseDTO.class);
            currencyConversionResponseDTO = responseEntity.getBody();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CurrencyConversionException("Currency conversion service failure.");
        }

        assert currencyConversionResponseDTO != null;

        return currencyConversionResponseDTO;
    }
}
