package org.example;

import java.net.InetAddress;

public abstract class Ip2CountryService {
    public CountryInfo ipAddressToCountryInfo(InetAddress ipAddress) {
        String jsonResponse = this.requestCountryInfo(ipAddress);
        return CountryInfo.fromJson(jsonResponse);
    }

    abstract String requestCountryInfo(InetAddress ipAddress);
}
