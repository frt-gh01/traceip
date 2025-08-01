package org.example;

import org.example.structs.CountryInfo;
import org.example.structs.ExchangeRateInfo;
import org.example.structs.Language;
import org.example.structs.RequestInfo;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class TraceResult {
    private final RequestInfo requestInfo;
    private final CountryInfo countryInfo;
    private final List<OffsetDateTime> dateTimes;
    private final ExchangeRateInfo dollarExchangeRate;

    private final GeoPosition GEO_BUENOS_AIRES = new GeoPosition(-58.4370, -34.6075);

    public TraceResult(RequestInfo requestInfo, CountryInfo countryInfo, List<OffsetDateTime> dateTimes, ExchangeRateInfo dollarExchangeRate) {
        this.requestInfo = requestInfo;
        this.countryInfo = countryInfo;
        this.dateTimes = dateTimes;
        this.dollarExchangeRate = dollarExchangeRate;
    }

    public String ipAddress() {
        return this.requestInfo.getIpAddress().getHostAddress();
    }

    public OffsetDateTime localDateTime() {
        return this.requestInfo.getDateTime();
    }

    public String countryName() {
        return this.countryInfo.countryName;
    }

    public String countryCode() {
        return this.countryInfo.countryCode;
    }

    public GeoPosition geoPosition() {
        return new GeoPosition(this.countryInfo.longitude, this.countryInfo.latitude);
    }

    public double distanceKilometersToBuenosAires() {
        GeoPosition geoPosition = this.geoPosition();
        return geoPosition.haversineDistanceKilometersTo(GEO_BUENOS_AIRES);
    }

    public List<Language> languages() {
        return this.countryInfo.languages();
    }

    public List<OffsetDateTime> dateTimes() {
        return this.dateTimes;
    }

    @Override
    public String toString() {
        String localDateTime = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss '(UTC'xxx')'").format(this.localDateTime());
        String languages = this.languages().stream().map(Language::toString).collect(Collectors.joining(", "));
        String currency = this.dollarExchangeRate.toString();
        String times = this.dateTimes.stream().map(offsetDateTime -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss '(UTC'xxx')'");
            return offsetDateTime.format(formatter);
        }).collect(Collectors.joining(", "));
        String distance = "%s kms %s a Buenos Aires %s".formatted(this.distanceKilometersToBuenosAires(), this.geoPosition(), GEO_BUENOS_AIRES);

        return """ 
            IP: %s, fecha actual: %s
            País: %s
            ISO Code: %s
            Idiomas: %s
            Moneda: %s
            Hora: %s
            Distancia estimada: %s
        """.formatted(this.ipAddress(), localDateTime, this.countryName(), this.countryCode(),
                      languages, currency, times, distance);
    }
}
