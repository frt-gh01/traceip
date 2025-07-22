package org.example.services;

import org.example.structs.CountryInfo;
import org.javatuples.Tuple;

import java.net.InetAddress;

public abstract class Ip2CountryService {
    private final RequestExecutor requestExecutor;

    public Ip2CountryService(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    public CountryInfo ipAddressToCountryInfo(InetAddress ipAddress) {
        String jsonResponse = requestExecutor.execute(ipAddress, (param) -> this.requestCountryInfo((InetAddress)param));
        return CountryInfo.fromJson(jsonResponse);
    }

    abstract String requestCountryInfo(InetAddress ipAddress);
}
