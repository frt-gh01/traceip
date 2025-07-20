package org.example;

import org.example.structs.CountryInfo;
import org.example.structs.Language;
import org.example.structs.RequestInfo;

import java.net.InetAddress;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class TraceResult {
    private final RequestInfo requestInfo;
    private final CountryInfo countryInfo;
    private final List<OffsetDateTime> dateTimes;

    private final GeoPosition GEO_BUENOS_AIRES = new GeoPosition(-58.4370, -34.6075);

    public TraceResult(RequestInfo requestInfo, CountryInfo countryInfo, List<OffsetDateTime> dateTimes) {
        this.requestInfo = requestInfo;
        this.countryInfo = countryInfo;
        this.dateTimes = dateTimes;
    }

    public String ipAddress() {
        return this.requestInfo.getIpAddress().getHostAddress();
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
        String languages = this.languages().stream().map(Language::toString).collect(Collectors.joining(", "));
        String times = this.dateTimes.stream().map(offsetDateTime -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss '(UTC'XXX')'");
            return offsetDateTime.format(formatter);
        }).collect(Collectors.joining(", "));
        String distance = "%s kms %s a Buenos Aires %s".formatted(this.distanceKilometersToBuenosAires(), this.geoPosition(), GEO_BUENOS_AIRES);

        return """ 
                   IP: %s
                   País: %s
                   ISO Code: %s
                   Idiomas: %s
                   Hora: %s
                   Distancia estimada: %s
               """.formatted(this.ipAddress(), this.countryName(), this.countryCode(),
                             languages, times, distance);
    }
}
