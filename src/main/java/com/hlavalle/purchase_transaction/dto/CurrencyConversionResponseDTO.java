package com.hlavalle.purchase_transaction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CurrencyConversionResponseDTO {

    private List<Data> data;
    private Object meta;
    private Object links;

    @Getter
    @Setter
    public static class Data {
        @JsonProperty("effective_date")
        private String effectiveDate;

        @JsonProperty("exchange_rate")
        private String exchangeRate;
    }
}

