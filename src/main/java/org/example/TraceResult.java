package org.example;

import org.example.structs.CountryInfo;
import org.example.structs.Language;
import org.example.structs.RequestInfo;

import java.net.InetAddress;
import java.time.OffsetDateTime;
import java.util.List;

public class TraceResult {
    private final RequestInfo requestInfo;
    private final CountryInfo countryInfo;
    private final List<OffsetDateTime> dateTimes;

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
        GeoPosition geoBuenosAires = new GeoPosition(58.4370, 34.6075);

        GeoPosition geoPosition = this.geoPosition();
        return geoPosition.haversineDistanceKilometersTo(geoBuenosAires);
    }

    public List<Language> languages() {
        return this.countryInfo.languages();
    }

    public List<OffsetDateTime> dateTimes() {
        return this.dateTimes;
    }
}
