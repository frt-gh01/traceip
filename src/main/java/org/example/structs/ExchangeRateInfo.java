package org.example.structs;

import java.math.BigDecimal;
import java.util.Currency;

public class ExchangeRateInfo {
    final private Currency baseCurrency;
    final private Currency targetCurrency;
    private final double exchangeRate;

    public ExchangeRateInfo(Currency baseCurrency, Currency targetCurrency, double exchangeRate) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.exchangeRate = exchangeRate;
    }

    @Override
    public String toString() {
        return "%s (1 %s = %s %s)".formatted(
                this.baseCurrency.getCurrencyCode(),
                this.baseCurrency.getCurrencyCode(),
                new BigDecimal(String.valueOf(this.exchangeRate)).toPlainString(),
                this.targetCurrency.getCurrencyCode()
        );
    }
}
