package org.example;

import java.net.InetAddress;
import java.util.function.Function;

public class Ip2CountryServiceStub extends Ip2CountryService {

    private final Function<InetAddress, String> requestHandler;

    public Ip2CountryServiceStub(Function<InetAddress, String> requestHandler) {
        super();
        this.requestHandler = requestHandler;
    }

    // Reference:
    // https://ipapi.com/documentation
    // https://ipapi.com/documentation#api_response_objects
    @Override
    String requestCountryInfo(InetAddress ipAddress) {
        return this.requestHandler.apply(ipAddress);
    }
}
