package org.example.structs;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class RequestInfo {
    private final InetAddress ipAddress;
    // TODO: add current local time

    public RequestInfo(String ipAddress) throws UnknownHostException {
        this.ipAddress = InetAddress.getByName(ipAddress);
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }
}
