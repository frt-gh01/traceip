package org.example;

import com.google.gson.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class IpTracer {
    public IpTracer() {
    }

    public CountryInfo trace(String ipAddress)  throws UnknownHostException {
        Objects.requireNonNull(ipAddress, "Invalid IP Address: Null");

        if (ipAddress.isBlank()) {
            throw new UnknownHostException("Invalid IP Address: Empty");
        }

        InetAddress address = InetAddress.getByName(ipAddress);

        return this.getCountryInfoFrom(address);
    }

    private CountryInfo getCountryInfoFrom(InetAddress ipAddress) {
        String jsonResponse = """
                                { "ip": %s,
                                  "country_code": "AR",
                                  "country_name": "Argentina" }
                                """.formatted(ipAddress.getHostAddress());

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        return gson.fromJson(jsonResponse, CountryInfo.class);
    }
}
