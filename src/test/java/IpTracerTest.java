import org.example.IpTracer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
    @DisplayName("Should return country name with a valid IP Address")
    void testIpTracerReturnCountryNameWithValidIP() {
        try {
            String countryName = ipTracer.trace("192.168.0.1");
            assertEquals("Argentina", countryName);
        } catch (Exception ex) {
            fail();
        }
    }
}
