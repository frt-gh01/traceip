package org.example;

import com.google.gson.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class IpTracer {
    private final Ip2CountryService ip2CountryService;

    public IpTracer(Ip2CountryService ip2CountryService) {
        this.ip2CountryService = ip2CountryService;
    }

    public CountryInfo trace(String ipAddress)  throws UnknownHostException {
        Objects.requireNonNull(ipAddress, "Invalid IP Address: Null");

        if (ipAddress.isBlank()) {
            throw new UnknownHostException("Invalid IP Address: Empty");
        }

        InetAddress address = InetAddress.getByName(ipAddress);
        return ip2CountryService.ipAddressToCountryInfo(address);
    }
}
