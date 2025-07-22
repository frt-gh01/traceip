package org.example.services;

import org.example.structs.ExchangeRate;
import org.example.structs.ExchangeRateInfo;
import org.javatuples.Triplet;
import org.javatuples.Tuple;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Currency;

public abstract class CurrencyService {
    private final Clock clock;
    private final RequestExecutor requestExecutor;

    public CurrencyService(Clock clock, RequestExecutor requestExecutor) {
        this.clock = clock;
        this.requestExecutor = requestExecutor;
    }

    public ExchangeRateInfo calculateExchangeRate(Currency baseCurrency, Currency targetCurrency) {
        Tuple requestParams = new Triplet<String, String, OffsetDateTime>(
                baseCurrency.getCurrencyCode(), targetCurrency.getCurrencyCode(), OffsetDateTime.now(this.clock));

        String jsonResponse = requestExecutor.execute(requestParams, (param) -> this.requestExchangeRate((Tuple)param));
        ExchangeRate exchangeRate = ExchangeRate.fromJson(jsonResponse);
        return new ExchangeRateInfo(baseCurrency, targetCurrency, exchangeRate.getRate(targetCurrency.getCurrencyCode()));
    }

    abstract String requestExchangeRate(Tuple requestParams);
}
