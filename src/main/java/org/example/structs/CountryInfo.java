package org.example.structs;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Currency;
import java.util.List;

public class CountryInfo {
    public String countryName;
    public String countryCode;
    public double latitude;
    public double longitude;
    public Location location;
    public CurrencyInfo currency;

    public static CountryInfo fromJson(String json) {
        Gson gson = new GsonBuilder()
                            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                            .create();

        return gson.fromJson(json, CountryInfo.class);
    }

    public List<Language> languages() {
        return location.languages;
    }

    public Currency currency() {
        return this.currency.getCurrency();
    }
}

