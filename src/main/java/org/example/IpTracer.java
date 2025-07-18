package org.example;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

public class IpTracer {
    private final Ip2CountryService ip2CountryService;
    private final TimeZoneService timeZoneService;

    public IpTracer(Ip2CountryService ip2CountryService, TimeZoneService timeZoneService) {
        this.ip2CountryService = ip2CountryService;
        this.timeZoneService = timeZoneService;
    }

    public TraceResult trace(String ipAddress) throws UnknownHostException {
        Objects.requireNonNull(ipAddress, "Invalid IP Address: Null");

        if (ipAddress.isBlank()) {
            throw new UnknownHostException("Invalid IP Address: Empty");
        }

        CountryInfo countryInfo = this.countryInfo(ipAddress);
        List<OffsetDateTime> dateTimes = this.timeZoneInfo(countryInfo);

        return new TraceResult(countryInfo, dateTimes);
    }

    private CountryInfo countryInfo(String ipAddress) throws UnknownHostException {
        InetAddress address = InetAddress.getByName(ipAddress);
        return ip2CountryService.ipAddressToCountryInfo(address);
    }

    private List<OffsetDateTime> timeZoneInfo(CountryInfo countryInfo) {
        return timeZoneService.countryDateTimes(countryInfo.countryName);
    }
}
