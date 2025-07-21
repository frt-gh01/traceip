package org.example.structs;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Map;

/**
 * Class representing fixer.io response.
 * Needed for parsing JSON.
 * https://fixer.io/documentation
 */
public class ExchangeRate {
    public String base;
    public String date;
    public Map<String, Double> rates;

    public static ExchangeRate fromJson(String json) {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        return gson.fromJson(json, ExchangeRate.class);
    }

    public Double getRate(String currencyCode) {
        return this.rates.get(currencyCode);
    }
}
