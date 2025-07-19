import org.example.*;
import org.example.services.Ip2CountryService;
import org.example.services.Ip2CountryServiceStub;
import org.example.services.TimeZoneService;
import org.example.services.TimeZoneServiceStub;
import org.example.structs.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.UnknownHostException;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IpTracerTest {

    PersistenceLayer persistenceLayer;
    IpTracer ipTracer;

    @BeforeEach
    void setUp() {
        persistenceLayer = buildPersistenceLayer();
        ipTracer = new IpTracer(buildIp2CountryServiceStub(),
                                buildTimeZoneServiceStub(),
                                persistenceLayer);
    }

    @Test
    @DisplayName("Should not work with empty IP")
    void testIpTracerCreationWithEmptyIP() {
        Exception exception = assertThrows(UnknownHostException.class, () -> {
            ipTracer.trace("  ");
        });

        assertEquals("Invalid IP Address: Empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should not work with a NULL IP")
    void testIpTracerWithNoIP() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            ipTracer.trace(null);
        });

        assertEquals("Invalid IP Address: Null", exception.getMessage());
    }

    @Test
    @DisplayName("Should work with a valid IP Address")
    void testIpTracerWithValidIP() {
        assertDoesNotThrow(() -> {
            ipTracer.trace("192.168.0.1");
        });
    }

    @Test
    @DisplayName("Should return country name")
    void testIpTracerReturnCountryName() {
        try {
            TraceResult traceResult = ipTracer.trace("192.168.0.1");
            assertEquals("Argentina", traceResult.countryName());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should return country code")
    void testIpTracerReturnCountryCode() {
        try {
            TraceResult traceResult = ipTracer.trace("192.168.0.1");
            assertEquals("AR", traceResult.countryCode());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should return country latitude / longitude")
    void testIpTracerReturnCountryLatLng() {
        try {
            TraceResult traceResult = ipTracer.trace("192.168.0.1");

            GeoPosition expected = new GeoPosition(64, 34);
            assertTrue(traceResult.geoPosition().equals(expected));

        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should return country languages")
    void testIpTracerReturnCountryLanguages() {
        try {
            TraceResult traceResult = ipTracer.trace("192.168.0.1");
            List<Language> languages = traceResult.languages();

            assertEquals(2, languages.size());

            assertEquals("es", languages.get(0).code);
            assertEquals("Spanish", languages.get(0).name);

            assertEquals("gn", languages.get(1).code);
            assertEquals("Guarani", languages.get(1).name);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should return country datetimes")
    void testIpTracerReturnTimeZones() throws UnknownHostException {
        TraceResult traceResult = ipTracer.trace("192.168.0.1");
        List<OffsetDateTime> dateTimes = traceResult.dateTimes();

        assertEquals(2, dateTimes.size());
        assertEquals("2025-07-18T07:00-03:00", dateTimes.get(0).toString());
        assertEquals("2025-07-18T06:00-04:00", dateTimes.get(1).toString());
    }

    @Test
    @DisplayName("Should calculate distance to geoposition")
    void testIpTracerReturnDistanceToCity() {
        try {
            TraceResult traceResult = ipTracer.trace("192.168.0.1");

            // see. https://www.distance.to/Buenos-Aires,Ciudad-Aut%C3%B3noma-de-Buenos-Aires,ARG/Argentina
            assertEquals(515.35, traceResult.distanceKilometersToBuenosAires());

        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should persist trace")
    void testIpTracerPersistTraceResult() {
        try {

            assertEquals(0, persistenceLayer.traceResultsCount());

            ipTracer.trace("192.168.0.1");

            assertEquals(1, persistenceLayer.traceResultsCount());

            TraceResult persistedTraceResult = persistenceLayer.getTraceResults("AR").getFirst();

            assertEquals("AR", persistedTraceResult.countryCode());
            assertEquals("Argentina", persistedTraceResult.countryName());
            assertEquals("192.168.0.1", persistedTraceResult.ipAddress());

        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should persist multiple traces for same ip")
    void testIpTracerPersistMultipleTraceResultSameIp() {
        try {

            assertEquals(0, persistenceLayer.traceResultsCount());

            ipTracer.trace("192.168.0.1");
            ipTracer.trace("192.168.0.1");

            assertEquals(2, persistenceLayer.traceResultsCount());

            List<TraceResult> persistedTraceResults = persistenceLayer.getTraceResults("AR");

            assertTrue(persistedTraceResults.stream().allMatch(traceResult ->
                    traceResult.ipAddress().equals("192.168.0.1") && traceResult.countryCode().equals("AR")));

        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should persist multiple traces for different ips (same country)")
    void testIpTracerPersistMultipleTraceResultDiffIpsSameCountry() {
        try {

            assertEquals(0, persistenceLayer.traceResultsCount());

            ipTracer.trace("192.168.0.1");
            ipTracer.trace("192.168.0.2");

            assertEquals(2, persistenceLayer.traceResultsCount());

            List<TraceResult> persistedTraceResults = persistenceLayer.getTraceResults("AR");

            assertTrue(persistedTraceResults.stream().allMatch(traceResult -> traceResult.countryCode().equals("AR")));
            assertTrue(persistedTraceResults.stream().anyMatch(traceResult -> traceResult.ipAddress().equals("192.168.0.1")));
            assertTrue(persistedTraceResults.stream().anyMatch(traceResult -> traceResult.ipAddress().equals("192.168.0.2")));

        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    private static Clock fixedClock() {
        Instant now = Instant.parse("2025-07-18T10:00:00Z");
        return Clock.fixed(now, ZoneOffset.UTC);
    }

    private static TimeZoneService buildTimeZoneServiceStub() {
        return new TimeZoneServiceStub(fixedClock(), """
            {   "name": %s,
                "timezones": [
                    "UTC-03:00",
                    "UTC-04:00"
                ]
            }
            """::formatted);
    }

    private static Ip2CountryService buildIp2CountryServiceStub() {
        return new Ip2CountryServiceStub(ipAddress -> {
            return """
            {   "ip": %s,
                "country_code": "AR",
                "country_name": "Argentina",
                "latitude": 34,
                "longitude": 64,
                "location": {
                    "languages": [
                       {
                            "code": "es",
                            "name": "Spanish"
                       },
                       {
                            "code": "gn",
                            "name": "Guarani"
                       }
                    ]
                }
            }
            """.formatted(ipAddress.getHostAddress());
        });
    }

    private static PersistenceLayer buildPersistenceLayer() {
        return new PersistenceLayer();
    }
}
