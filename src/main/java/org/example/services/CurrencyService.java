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

    public CurrencyService(Clock clock) {
        this.clock = clock;
    }

    public ExchangeRateInfo calculateExchangeRate(Currency baseCurrency, Currency targetCurrency) {
        Tuple requestParams = new Triplet<String, String, OffsetDateTime>(
                baseCurrency.getCurrencyCode(), targetCurrency.getCurrencyCode(), OffsetDateTime.now(this.clock));

        String jsonResponse = this.requestExchangeRate(requestParams);
        ExchangeRate exchangeRate = ExchangeRate.fromJson(jsonResponse);
        return new ExchangeRateInfo(baseCurrency, targetCurrency, exchangeRate.getRate(targetCurrency.getCurrencyCode()));
    }

    abstract String requestExchangeRate(Tuple requestParams);
}
