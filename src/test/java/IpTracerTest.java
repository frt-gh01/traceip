import org.example.IpTracer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IpTracerTest {

    @Test
    @DisplayName("Should not work with empty IP")
    void testIpTracerCreationWithEmptyIP() {
        Exception exception = assertThrows(UnknownHostException.class, () -> {
            new IpTracer("  ");
        });

        assertEquals("Invalid IP Address: Empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should not work with a NULL IP")
    void testIpTracerCreationWithNoIP() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new IpTracer(null);
        });

        assertEquals("Invalid IP Address: Null", exception.getMessage());
    }
}
