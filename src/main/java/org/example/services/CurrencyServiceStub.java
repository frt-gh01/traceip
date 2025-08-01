package org.example.services;

import org.javatuples.Tuple;

import java.time.Clock;
import java.util.function.Function;

public class CurrencyServiceStub extends CurrencyService {
    private final Function<Tuple, String> requestHandler;

    public CurrencyServiceStub(Clock clock, RequestExecutor requestExecutor, Function<Tuple, String> requestHandler) {
        super(clock, requestExecutor);
        this.requestHandler = requestHandler;
    }

    @Override
    String requestExchangeRate(Tuple requestParams) {
        return this.requestHandler.apply(requestParams);
    }
}
