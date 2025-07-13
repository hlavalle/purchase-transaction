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
    public CurrencyConversionResponseDTO convertCurrency(
            BigDecimal amount, String currency, String gteDate, String lteDate) {

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange?filter=country_currency_desc:eq:");
        urlBuilder.append(currency);
        urlBuilder.append(",effective_date:gte:");
        urlBuilder.append(gteDate);
        urlBuilder.append(",effective_date:lte:");
        urlBuilder.append(lteDate);
        urlBuilder.append("&sort=-effective_date");

        CurrencyConversionResponseDTO currencyConversionResponseDTO;
        ResponseEntity<CurrencyConversionResponseDTO> responseEntity;
        try {
            responseEntity = restClient.get()
                    .uri(urlBuilder.toString())
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
