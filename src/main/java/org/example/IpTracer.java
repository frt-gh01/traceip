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
        // Reference: https://ipapi.com/documentation
        String jsonResponse = """
                                { "ip": %s,
                                  "country_code": "AR",
                                  "country_name": "Argentina",
                                   "latitude": 38.4161,
                                   "longitude": 63.6167}
                                """.formatted(ipAddress.getHostAddress());

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        return gson.fromJson(jsonResponse, CountryInfo.class);
    }
}
