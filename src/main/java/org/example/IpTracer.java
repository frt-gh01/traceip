package org.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class IpTracer {
    public IpTracer() {
    }

    public String trace(String ipAddress)  throws UnknownHostException {
        Objects.requireNonNull(ipAddress, "Invalid IP Address: Null");

        if (ipAddress.isBlank()) {
            throw new UnknownHostException("Invalid IP Address: Empty");
        }

        InetAddress address = InetAddress.getByName(ipAddress);

        return this.getCountryInfoFrom(address);
    }

    private String getCountryInfoFrom(InetAddress ipAddress) {
        String jsonResponse = """
                                { "ip": %s,
                                  "country_code": "AR",
                                  "country_name": "Argentina" }
                                """.formatted(ipAddress.getHostAddress());

        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

        return jsonObject.get("country_name").getAsString();
    }
}
