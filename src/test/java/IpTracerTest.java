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
    void testIpTracerCreationWithNoIP() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            ipTracer.trace(null);
        });

        assertEquals("Invalid IP Address: Null", exception.getMessage());
    }

    @Test
    @DisplayName("Should work with a valid IP Address")
    void testIpTracerCreationWithValidIP() {
        String ipAddress = "192.168.0.1";

        assertDoesNotThrow(() -> {
            InetAddress address = ipTracer.trace(ipAddress);

            assertEquals(ipAddress, address.getHostAddress());
        });
    }
}
