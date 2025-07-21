package org.example;

import org.example.services.Ip2CountryService;
import org.example.services.TimeZoneService;
import org.example.structs.CountryInfo;
import org.example.structs.RequestInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

public class IpTracer {
    private final Clock clock;
    private final Ip2CountryService ip2CountryService;
    private final TimeZoneService timeZoneService;
    private final PersistenceLayer persistenceLayer;

    public IpTracer(Clock clock, Ip2CountryService ip2CountryService, TimeZoneService timeZoneService, PersistenceLayer persistenceLayer) {
        this.clock = clock;
        this.ip2CountryService = ip2CountryService;
        this.timeZoneService = timeZoneService;
        this.persistenceLayer = persistenceLayer;
    }

    public TraceResult trace(String ipAddress) throws UnknownHostException {
        Objects.requireNonNull(ipAddress, "Invalid IP Address: Null");

        if (ipAddress.isBlank()) {
            throw new UnknownHostException("Invalid IP Address: Empty");
        }

        RequestInfo requestInfo = this.requestInfo(ipAddress, this.clock);
        CountryInfo countryInfo = this.countryInfo(requestInfo.getIpAddress());
        List<OffsetDateTime> dateTimes = this.timeZoneInfo(countryInfo);

        TraceResult traceResult = new TraceResult(requestInfo, countryInfo, dateTimes);
        this.persistenceLayer.persist(traceResult);

        return traceResult;
    }

    private RequestInfo requestInfo(String ipAddress, Clock clock) throws UnknownHostException {
        return new RequestInfo(ipAddress, OffsetDateTime.now(clock));
    }

    private CountryInfo countryInfo(InetAddress ipAddress) {
        return ip2CountryService.ipAddressToCountryInfo(ipAddress);
    }

    private List<OffsetDateTime> timeZoneInfo(CountryInfo countryInfo) {
        return timeZoneService.countryDateTimes(countryInfo.countryName);
    }
}
