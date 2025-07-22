package org.example.services;

import org.example.PersistenceLayer;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class StubsFactory {

    public static CurrencyService buildCurrencyServiceStub(Clock clock) {
        return buildCurrencyServiceStub(clock, new RequestExecutor());
    }

    // Reference:
    // https://fixer.io/documentation
    public static CurrencyService buildCurrencyServiceStub(Clock clock, RequestExecutor requestExecutor) {
        Map<String, Double> dollarExchangeRateByCurrency = Map.of(
                "ARS", 0.00078,
                "BRL", 0.18,
                "EUR", 1.17
        );

        return new CurrencyServiceStub(clock, requestExecutor, params -> {
            String base = params.getValue(0).toString();
            String target = params.getValue(1).toString();

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            OffsetDateTime offsetDateTime = (OffsetDateTime) params.getValue(2);
            String date = offsetDateTime.format(dateTimeFormatter);

            return """
            {
                "historical": "true",
                "date": %s,
                "base": %s,
                "rates": {
                    %s: %s
                }
            }
            """.formatted(date, base, target, dollarExchangeRateByCurrency.get(base));
        });
    }

    public static TimeZoneService buildTimeZoneServiceStub(Clock clock) {
        return buildTimeZoneServiceStub(clock, new RequestExecutor());
    }

    // Reference:
    // https://countrylayer.com/documentation/
    public static TimeZoneService buildTimeZoneServiceStub(Clock clock, RequestExecutor requestExecutor) {
        return new TimeZoneServiceStub(clock, requestExecutor, """
            {   "name": %s,
                "timezones": [
                    "UTC-03:00",
                    "UTC-04:00"
                ]
            }
            """::formatted);
    }

    public static Ip2CountryService buildIp2CountryServiceStub() {
        return buildIp2CountryServiceStub(new RequestExecutor());
    }

    // Reference:
    // https://ipapi.com/documentation
    // https://ipapi.com/documentation#api_response_objects
    public static Ip2CountryService buildIp2CountryServiceStub(RequestExecutor requestExecutor) {
        String jsonArgentina = """
            { "ip": %s, "country_code": "AR", "country_name": "Argentina", "latitude": -34, "longitude": -64,
              "location": { "languages": [{ "code": "es", "name": "Spanish" }, { "code": "gn", "name": "Guarani" }] },
              "currency": { "code": "ARS", "name": "Argentine Peso", "symbol": "$" }
            }
            """;

        String jsonBrasil = """
            { "ip": %s, "country_code": "BR", "country_name": "Brasil", "latitude": -14.23, "longitude": -51.92,
              "location": { "languages": [{ "code": "pt", "name": "Portuguese" }] },
              "currency": { "code": "BRL", "name": "Brazilian real", "symbol": "R$" }
            }
            """;

        String jsonEspana = """
            { "ip": %s, "country_code": "ES", "country_name": "España", "latitude": 40.46, "longitude": 3.74,
              "location": { "languages": [{ "code": "es", "name": "Spanish" }, { "code": "ca", "name": "Catalan" },
                                          { "code": "gl", "name": "Galician" }] },
              "currency": { "code": "EUR", "name": "Euro", "symbol": "€" }
            }
            """;

        String[] responses = { jsonArgentina, jsonBrasil, jsonEspana };

        return new Ip2CountryServiceStub(requestExecutor, ipAddress -> {
            // Easy stub logic where IP's third octet relates to country
            // TODO: make safe method (ie. out of bounds, parseInt)
            String countryOctet = ipAddress.getHostAddress().split("\\.")[2];
            return responses[Integer.parseInt(countryOctet)].formatted(ipAddress.getHostAddress());
        });
    }

    public static PersistenceLayer buildPersistenceLayer() {
        return new PersistenceLayer();
    }
}
