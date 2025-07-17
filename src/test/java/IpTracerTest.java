import org.example.CountryInfo;
import org.example.IpTracer;
import org.example.Language;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IpTracerTest {

    IpTracer ipTracer;

    @BeforeEach
    void setUp() {
        ipTracer = new IpTracer();
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
            CountryInfo countryInfo = ipTracer.trace("192.168.0.1");
            assertEquals("Argentina", countryInfo.countryName);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should return country code")
    void testIpTracerReturnCountryCode() {
        try {
            CountryInfo countryInfo = ipTracer.trace("192.168.0.1");
            assertEquals("AR", countryInfo.countryCode);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should return country latitude / longitude")
    void testIpTracerReturnCountryLatLng() {
        try {
            CountryInfo countryInfo = ipTracer.trace("192.168.0.1");
            assertEquals(38.4161, countryInfo.latitude);
            assertEquals(63.6167, countryInfo.longitude);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should return country languages")
    void testIpTracerReturnCountryLanguages() {
        try {
            CountryInfo countryInfo = ipTracer.trace("192.168.0.1");
            List<Language> languages = countryInfo.languages();
            assertEquals(1, languages.size());

            assertEquals("es", languages.getFirst().code);
            assertEquals("Spanish", languages.getFirst().name);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
}
