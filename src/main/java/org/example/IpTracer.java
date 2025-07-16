package org.example;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class IpTracer {
    public IpTracer() {
    }

    public InetAddress trace(String ipAddress)  throws UnknownHostException {
        Objects.requireNonNull(ipAddress, "Invalid IP Address: Null");

        if (ipAddress.isBlank()) {
            throw new UnknownHostException("Invalid IP Address: Empty");
        }

        return InetAddress.getByName(ipAddress);
    }
}
