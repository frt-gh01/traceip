package org.example;

import java.time.OffsetDateTime;
import java.util.List;

public class TraceResult {
    private final CountryInfo countryInfo;
    private final List<OffsetDateTime> dateTimes;

    public TraceResult(CountryInfo countryInfo, List<OffsetDateTime> dateTimes) {
        this.countryInfo = countryInfo;
        this.dateTimes = dateTimes;
    }

    public String countryName() {
        return this.countryInfo.countryName;
    }

    public String countryCode() {
        return this.countryInfo.countryCode;
    }

    public double latitude() {
        return this.countryInfo.latitude;
    }

    public double longitude() {
        return this.countryInfo.longitude;
    }

    public List<Language> languages() {
        return this.countryInfo.languages();
    }

    public List<OffsetDateTime> dateTimes() {
        return this.dateTimes;
    }
}
