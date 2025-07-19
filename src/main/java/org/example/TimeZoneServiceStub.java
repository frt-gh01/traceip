package org.example;

import java.time.Clock;
import java.util.function.Function;

public class TimeZoneServiceStub extends TimeZoneService {

    private final Function<String, String> requestHandler;

    public TimeZoneServiceStub(Clock clock, Function<String, String> requestHandler) {
        super(clock);
        this.requestHandler = requestHandler;
    }

    // Reference:
    // https://countrylayer.com/documentation/
    @Override
    String requestZoneOffsets(String countryName) {
        return requestHandler.apply(countryName);
    }
}
