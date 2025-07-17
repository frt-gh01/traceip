package org.example;

import java.util.List;

public class CountryInfo {
    public String countryName;
    public String countryCode;
    public double latitude;
    public double longitude;
    public Location location;

    public List<Language> languages() {
        return location.languages;
    }
}

