package com.hlavalle.purchase_transaction;

import com.hlavalle.purchase_transaction.dto.CurrencyConversionResponseDTO;
import com.hlavalle.purchase_transaction.service.CurrencyConversionService;
import com.hlavalle.purchase_transaction.service.CurrencyConversionServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ExtendWith(MockitoExtension.class)
@RestClientTest(CurrencyConversionServiceImpl.class)
@ActiveProfiles("test")
public class ConversionServiceTests {

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    private CurrencyConversionService conversionService;

    @Test
    @DisplayName("GET https://api.fiscaldata.treasury.gov/... - Success")
    void testCurrencyConversion() {

        String url = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange?" +
                "filter=country_currency_desc:eq:Brazil-Real,effective_date:gte:2001-01-10," +
                "effective_date:lte:2001-07-10&sort=-effective_date";

        String jsonResponseString = """
                {
                  "data": [
                    {
                      "record_date": "2001-06-30",
                      "country": "Brazil",
                      "currency": "Real",
                      "country_currency_desc": "Brazil-Real",
                      "exchange_rate": "2.36",
                      "effective_date": "2001-06-30",
                      "src_line_nbr": "25",
                      "record_fiscal_year": "2001",
                      "record_fiscal_quarter": "3",
                      "record_calendar_year": "2001",
                      "record_calendar_quarter": "2",
                      "record_calendar_month": "06",
                      "record_calendar_day": "30"
                    },
                    {
                      "record_date": "2001-03-31",
                      "country": "Brazil",
                      "currency": "Real",
                      "country_currency_desc": "Brazil-Real",
                      "exchange_rate": "2.043",
                      "effective_date": "2001-03-31",
                      "src_line_nbr": "25",
                      "record_fiscal_year": "2001",
                      "record_fiscal_quarter": "2",
                      "record_calendar_year": "2001",
                      "record_calendar_quarter": "1",
                      "record_calendar_month": "03",
                      "record_calendar_day": "31"
                    }
                  ],
                  "meta": {
                    "count": 2,
                    "labels": {
                      "record_date": "Record Date",
                      "country": "Country",
                      "currency": "Currency",
                      "country_currency_desc": "Country - Currency Description",
                      "exchange_rate": "Exchange Rate",
                      "effective_date": "Effective Date",
                      "src_line_nbr": "Source Line Number",
                      "record_fiscal_year": "Fiscal Year",
                      "record_fiscal_quarter": "Fiscal Quarter Number",
                      "record_calendar_year": "Calendar Year",
                      "record_calendar_quarter": "Calendar Quarter Number",
                      "record_calendar_month": "Calendar Month Number",
                      "record_calendar_day": "Calendar Day Number"
                    },
                    "dataTypes": {
                      "record_date": "DATE",
                      "country": "STRING",
                      "currency": "STRING",
                      "country_currency_desc": "STRING",
                      "exchange_rate": "NUMBER",
                      "effective_date": "DATE",
                      "src_line_nbr": "INTEGER",
                      "record_fiscal_year": "YEAR",
                      "record_fiscal_quarter": "QUARTER",
                      "record_calendar_year": "YEAR",
                      "record_calendar_quarter": "QUARTER",
                      "record_calendar_month": "MONTH",
                      "record_calendar_day": "DAY"
                    },
                    "dataFormats": {
                      "record_date": "YYYY-MM-DD",
                      "country": "String",
                      "currency": "String",
                      "country_currency_desc": "String",
                      "exchange_rate": "10.2",
                      "effective_date": "YYYY-MM-DD",
                      "src_line_nbr": "10",
                      "record_fiscal_year": "YYYY",
                      "record_fiscal_quarter": "Q",
                      "record_calendar_year": "YYYY",
                      "record_calendar_quarter": "Q",
                      "record_calendar_month": "MM",
                      "record_calendar_day": "DD"
                    },
                    "total-count": 2,
                    "total-pages": 1
                  },
                  "links": {
                    "self": "&page%5Bnumber%5D=1&page%5Bsize%5D=100",
                    "first": "&page%5Bnumber%5D=1&page%5Bsize%5D=100",
                    "prev": null,
                    "next": null,
                    "last": "&page%5Bnumber%5D=1&page%5Bsize%5D=100"
                  }
                }
                """;

        this.mockRestServiceServer.expect(requestTo(Matchers.equalTo(url)))
                .andExpect(method(HttpMethod.GET))
                        .andRespond(withSuccess(jsonResponseString, MediaType.APPLICATION_JSON));

        CurrencyConversionResponseDTO currencyConversionResponseDTO = conversionService.convertCurrency(
                new BigDecimal("1234.78"), "Brazil-Real","2001-01-10", "2001-07-10");

        assertNotNull(currencyConversionResponseDTO);
        assertEquals(2, currencyConversionResponseDTO.getData().size());

        assertTrue(currencyConversionResponseDTO.getData().get(0).getEffectiveDate()
                .compareTo(currencyConversionResponseDTO.getData().get(1).getEffectiveDate()) > 0);
    }


}
