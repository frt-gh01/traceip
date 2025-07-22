package org.example.services;

import java.net.InetAddress;
import java.util.function.Function;

public class Ip2CountryServiceStub extends Ip2CountryService {

    private final Function<InetAddress, String> requestHandler;

    public Ip2CountryServiceStub(RequestExecutor requestExecutor, Function<InetAddress, String> requestHandler) {
        super(requestExecutor);
        this.requestHandler = requestHandler;
    }

    @Override
    String requestCountryInfo(InetAddress ipAddress) {
        return this.requestHandler.apply(ipAddress);
    }
}
