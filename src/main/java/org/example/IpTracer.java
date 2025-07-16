package org.example;

import java.net.UnknownHostException;
import java.util.Objects;

public class IpTracer {
    public IpTracer(String ipAddress) throws UnknownHostException {
        Objects.requireNonNull(ipAddress, "Invalid IP Address: Null");

        if (ipAddress.isBlank()) {
            throw new UnknownHostException("Invalid IP Address: Empty");
        }
    }
}
