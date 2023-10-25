package com.transactioncurrency.services;

import com.transactioncurrency.infra.util.IntegerUtil;
import com.transactioncurrency.infra.util.StringUtil;
import io.vertx.ext.web.handler.impl.HttpStatusException;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FiscalDataService {

    private static final String DATA = "data";
    private static final String EXCHANGE_RATE = "exchange_rate";
    private static final String UNABLE_FIND = "Unable to find exchange rate";
    private static final String TRY_AGAIN = "An exchange rate was not found for this transaction date or currency. " +
            "Please, verify input data and try again";

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.fiscaldata.ratesOfExchange}")
    private String ratesOfExchangeApiUrl;

    public BigDecimal getByDateAndCurrency(
            @NotNull LocalDate date, @NotNull String currency) throws HttpStatusException {
        String formattedUrl = formatUrl(date, currency);
        ResponseEntity<Map> fiscalDataResponse = restTemplate.getForEntity(formattedUrl, Map.class);

        HttpStatusCode responseStatus = fiscalDataResponse.getStatusCode();
        if (responseStatus == HttpStatus.OK) {
            return getExchangeRate(fiscalDataResponse.getBody());
        } else {
            throw new HttpStatusException(HttpStatus.BAD_GATEWAY.value(), responseStatus.toString());
        }
    }

    private BigDecimal getExchangeRate(Map dataMap) {
        if (dataMap == null || !dataMap.containsKey(DATA)) {
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR.value(), UNABLE_FIND);
        }

        List<Map<String, Object>> dataList = (List<Map<String, Object>>) dataMap.get(DATA);
        if (dataList == null || dataList.isEmpty()) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST.value(), TRY_AGAIN);
        }

        Map<String, Object> firstElement = dataList.get(0);
        if (!firstElement.containsKey(EXCHANGE_RATE)) {
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR.value(), UNABLE_FIND);
        }

        String exchangeRateString = (String) firstElement.get(EXCHANGE_RATE);

        return StringUtil.parseBigDecimalWithScale(exchangeRateString, IntegerUtil.TWO);
    }

    private String formatUrl(LocalDate date, String currency) {
        return this.ratesOfExchangeApiUrl + "?fields=currency,exchange_rate,record_date" +
                "&filter=country_currency_desc:in:(%s),record_date:lte:%s,record_date:gte:%s"
                .formatted(currency, date.toString(), date.minusMonths(IntegerUtil.SIX).toString()) +
                "&sort=-record_date&format=json&page[number]=1&page[size]=1";
    }
}
