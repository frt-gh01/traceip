package org.example.structs;

import java.util.Currency;

public class CurrencyInfo {
    public String code;
    public String name;
    public String symbol;

    public Currency getCurrency() {
        return Currency.getInstance(this.code);
    }
}
