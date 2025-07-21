package org.example.services;

import org.example.PersistenceLayer;

import java.time.Clock;

public class StubsFactory {
    public static TimeZoneService buildTimeZoneServiceStub(Clock clock) {
        return new TimeZoneServiceStub(clock, """
            {   "name": %s,
                "timezones": [
                    "UTC-03:00",
                    "UTC-04:00"
                ]
            }
            """::formatted);
    }

    public static Ip2CountryService buildIp2CountryServiceStub() {
        String jsonArgentina = """
            { "ip": %s, "country_code": "AR", "country_name": "Argentina", "latitude": -34, "longitude": -64,
              "location": { "languages": [{ "code": "es", "name": "Spanish" }, { "code": "gn", "name": "Guarani" }] }
            }
            """;

        String jsonBrasil = """
            { "ip": %s, "country_code": "BR", "country_name": "Brasil", "latitude": -14.23, "longitude": -51.92,
              "location": { "languages": [{ "code": "pt", "name": "Portuguese" }] }
            }
            """;

        String jsonEspana = """
            { "ip": %s, "country_code": "ES", "country_name": "España", "latitude": 40.46, "longitude": 3.74,
              "location": { "languages": [{ "code": "es", "name": "Spanish" }, { "code": "ca", "name": "Catalan" },
                                          { "code": "gl", "name": "Galician" }] 
                                          
                                          }
            }
            """;

        String[] responses = { jsonArgentina, jsonBrasil, jsonEspana };

        return new Ip2CountryServiceStub(ipAddress -> {
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
