import org.example.*;
import org.example.services.StubsFactory;
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
        persistenceLayer = StubsFactory.buildPersistenceLayer();
        ipTracer = new IpTracer(StubsFactory.buildIp2CountryServiceStub(),
                                StubsFactory.buildTimeZoneServiceStub(buildClock()),
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

            GeoPosition expected = new GeoPosition(-64, -34);
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

            assertEquals(0, persistenceLayer.queryTraceResultsCount());

            ipTracer.trace("192.168.0.1");

            assertEquals(1, persistenceLayer.queryTraceResultsCount());

            TraceResult persistedTraceResult = persistenceLayer.queryTraceResultsByCountry("AR").getFirst();

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

            assertEquals(0, persistenceLayer.queryTraceResultsCount());

            ipTracer.trace("192.168.0.1");
            ipTracer.trace("192.168.0.1");

            assertEquals(2, persistenceLayer.queryTraceResultsCount());

            List<TraceResult> persistedTraceResults = persistenceLayer.queryTraceResultsByCountry("AR");

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

            assertEquals(0, persistenceLayer.queryTraceResultsCount());

            ipTracer.trace("192.168.0.1");
            ipTracer.trace("192.168.0.2");

            assertEquals(2, persistenceLayer.queryTraceResultsCount());

            List<TraceResult> persistedTraceResults = persistenceLayer.queryTraceResultsByCountry("AR");

            assertTrue(persistedTraceResults.stream().allMatch(traceResult -> traceResult.countryCode().equals("AR")));
            assertTrue(persistedTraceResults.stream().anyMatch(traceResult -> traceResult.ipAddress().equals("192.168.0.1")));
            assertTrue(persistedTraceResults.stream().anyMatch(traceResult -> traceResult.ipAddress().equals("192.168.0.2")));

        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should persist multiple traces for different ips (different countries)")
    void testIpTracerPersistMultipleTraceResultDiffIpsDiffCountries() {
        try {

            assertEquals(0, persistenceLayer.queryTraceResultsCount());

            ipTracer.trace("192.168.0.1"); // AR
            ipTracer.trace("192.168.0.2"); // AR
            ipTracer.trace("192.168.1.1"); // BR
            ipTracer.trace("192.168.2.1"); // ES
            ipTracer.trace("192.168.2.2"); // ES

            assertEquals(5, persistenceLayer.queryTraceResultsCount());

            List<TraceResult> persistedTraceResults;

            // AR
            persistedTraceResults = persistenceLayer.queryTraceResultsByCountry("AR");

            assertTrue(persistedTraceResults.stream().allMatch(traceResult -> traceResult.countryCode().equals("AR")));
            assertTrue(persistedTraceResults.stream().anyMatch(traceResult -> traceResult.ipAddress().equals("192.168.0.1")));
            assertTrue(persistedTraceResults.stream().anyMatch(traceResult -> traceResult.ipAddress().equals("192.168.0.2")));

            // BR
            persistedTraceResults = persistenceLayer.queryTraceResultsByCountry("BR");

            assertTrue(persistedTraceResults.stream().allMatch(traceResult -> traceResult.countryCode().equals("BR")));
            assertTrue(persistedTraceResults.stream().anyMatch(traceResult -> traceResult.ipAddress().equals("192.168.1.1")));

            // ES
            persistedTraceResults = persistenceLayer.queryTraceResultsByCountry("ES");

            assertTrue(persistedTraceResults.stream().allMatch(traceResult -> traceResult.countryCode().equals("ES")));
            assertTrue(persistedTraceResults.stream().anyMatch(traceResult -> traceResult.ipAddress().equals("192.168.2.1")));
            assertTrue(persistedTraceResults.stream().anyMatch(traceResult -> traceResult.ipAddress().equals("192.168.2.2")));

        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should persist distance by country")
    void testIpTracerPersistDistanceByCountry() {
        try {
            ipTracer.trace("192.168.0.1"); // AR
            ipTracer.trace("192.168.0.2"); // AR
            ipTracer.trace("192.168.1.1"); // BR
            ipTracer.trace("192.168.2.1"); // ES
            ipTracer.trace("192.168.2.2"); // ES

            assertEquals(2358.39, persistenceLayer.queryDistanceToBuenosAiresByCountry("BR").orElse(0.0), 0.1);
            assertEquals(10493.89, persistenceLayer.queryDistanceToBuenosAiresByCountry("ES").orElse(0.0), 0.1);

        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should persist farthest distance when no countries")
    void testIpTracerPersistFathestDistanceNoCountries() {
        try {

            assert(persistenceLayer.queryFarthestDistanceToBuenosAires().isEmpty());

        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should persist farthest distance when 1 country")
    void testIpTracerPersistFathestDistanceOneCountry() {
        try {

            ipTracer.trace("192.168.1.1"); // BR

            assertEquals(2358.39, persistenceLayer.queryFarthestDistanceToBuenosAires().orElse(0.0), 0.1);

        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should persist farthest distance when many countries")
    void testIpTracerPersistFathestDistanceManyCountry() {
        try {

            ipTracer.trace("192.168.0.1"); // AR
            ipTracer.trace("192.168.0.2"); // AR
            ipTracer.trace("192.168.1.1"); // BR
            ipTracer.trace("192.168.2.1"); // ES
            ipTracer.trace("192.168.2.2"); // ES

            assertEquals(10493.89, persistenceLayer.queryFarthestDistanceToBuenosAires().orElse(0.0), 0.1);

        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should persist closest distance when no countries")
    void testIpTracerPersistClosestDistanceNoCountries() {
        try {

            assert(persistenceLayer.queryClosestDistanceToBuenosAires().isEmpty());

        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should persist closest distance when 1 country")
    void testIpTracerPersistClosestDistanceOneCountry() {
        try {

            ipTracer.trace("192.168.1.1"); // BR

            assertEquals(2358.39, persistenceLayer.queryClosestDistanceToBuenosAires().orElse(0.0), 0.1);

        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should persist closest distance when many countries")
    void testIpTracerPersistClosestDistanceManyCountry() {
        try {

            ipTracer.trace("192.168.0.1"); // AR
            ipTracer.trace("192.168.0.2"); // AR
            ipTracer.trace("192.168.1.1"); // BR
            ipTracer.trace("192.168.2.1"); // ES
            ipTracer.trace("192.168.2.2"); // ES

            assertEquals(515.35, persistenceLayer.queryClosestDistanceToBuenosAires().orElse(0.0), 0.1);

        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should persist average distance when no country")
    void testIpTracerPersistAverageDistanceNoCountries() {
        try {

            assertEquals(0.0, persistenceLayer.queryAverageDistanceToBuenosAires().orElse(0.0), 0.1);

        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should persist average distance when 1 country")
    void testIpTracerPersistAverageDistanceOneCountry() {
        try {

            ipTracer.trace("192.168.1.1"); // BR

            assertEquals(2358.39, persistenceLayer.queryAverageDistanceToBuenosAires().orElse(0.0), 0.1);

        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should persist average distance when many countries")
    void testIpTracerPersistAverageDistanceManyCountry() {
        try {

            ipTracer.trace("192.168.0.1"); // AR
            ipTracer.trace("192.168.0.2"); // AR
            ipTracer.trace("192.168.1.1"); // BR
            ipTracer.trace("192.168.1.2"); // BR
            ipTracer.trace("192.168.1.2"); // BR
            ipTracer.trace("192.168.2.1"); // ES
            ipTracer.trace("192.168.2.2"); // ES
            ipTracer.trace("192.168.2.3"); // ES
            ipTracer.trace("192.168.2.3"); // ES

            assertEquals(5564.6, persistenceLayer.queryAverageDistanceToBuenosAires().orElse(0.0), 0.1);

        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should print result")
    void testIpTracerPrintReport() {
        try {

            TraceResult traceResult =  ipTracer.trace("192.168.0.1"); // ES

            String expected = """
                IP: 192.168.0.1
                País: Argentina
                ISO Code: AR
                Idiomas: Spanish (es), Guarani (gn)
                Hora: 07:00:00 (UTC-03:00), 06:00:00 (UTC-04:00)
                Distancia estimada: 515.35 kms (-34.0, -64.0) a Buenos Aires (-34.6075, -58.437)
            """;

            assertEquals(expected, traceResult.toString());

        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    private static Clock buildClock() {
        Instant now = Instant.parse("2025-07-18T10:00:00Z");
        return Clock.fixed(now, ZoneOffset.UTC);
    }
}
