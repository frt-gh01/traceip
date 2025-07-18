import org.example.*;
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

    IpTracer ipTracer;

    @BeforeEach
    void setUp() {
        ipTracer = new IpTracer(new Ip2CountryService(),
                                new TimeZoneService(this.fixedClock()));
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

    private Clock fixedClock() {
        Instant now = Instant.parse("2025-07-18T10:00:00Z");
        return Clock.fixed(now, ZoneOffset.UTC);
    }
}
