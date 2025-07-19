package org.example;

import org.example.services.Ip2CountryService;
import org.example.services.TimeZoneService;
import org.example.structs.CountryInfo;
import org.example.structs.RequestInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

public class IpTracer {
    private final Ip2CountryService ip2CountryService;
    private final TimeZoneService timeZoneService;
    private final PersistenceLayer persistenceLayer;

    public IpTracer(Ip2CountryService ip2CountryService, TimeZoneService timeZoneService, PersistenceLayer persistenceLayer) {
        this.ip2CountryService = ip2CountryService;
        this.timeZoneService = timeZoneService;
        this.persistenceLayer = persistenceLayer;
    }

    public TraceResult trace(String ipAddress) throws UnknownHostException {
        Objects.requireNonNull(ipAddress, "Invalid IP Address: Null");

        if (ipAddress.isBlank()) {
            throw new UnknownHostException("Invalid IP Address: Empty");
        }

        RequestInfo requestInfo = this.requestInfo(ipAddress);
        CountryInfo countryInfo = this.countryInfo(requestInfo.getIpAddress());
        List<OffsetDateTime> dateTimes = this.timeZoneInfo(countryInfo);

        TraceResult traceResult = new TraceResult(requestInfo, countryInfo, dateTimes);
        this.persistenceLayer.persist(traceResult);

        return traceResult;
    }

    private RequestInfo requestInfo(String ipAddress) throws UnknownHostException {
        return new RequestInfo(ipAddress);
    }

    private CountryInfo countryInfo(InetAddress ipAddress) {
        return ip2CountryService.ipAddressToCountryInfo(ipAddress);
    }

    private List<OffsetDateTime> timeZoneInfo(CountryInfo countryInfo) {
        return timeZoneService.countryDateTimes(countryInfo.countryName);
    }
}
