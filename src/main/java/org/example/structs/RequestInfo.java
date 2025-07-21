package org.example.structs;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.OffsetDateTime;

public class RequestInfo {
    private final InetAddress ipAddress;
    private final OffsetDateTime dateTime;

    public RequestInfo(String ipAddress, OffsetDateTime dateTime) throws UnknownHostException {
        this.ipAddress = InetAddress.getByName(ipAddress);
        this.dateTime = dateTime;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }
}
