package org.example;

import java.net.InetAddress;

public class Ip2CountryService {
    public CountryInfo ipAddressToCountryInfo(InetAddress ipAddress) {
        String jsonResponse = this.requestCountryInfo(ipAddress);
        return CountryInfo.fromJson(jsonResponse);
    }

    // Reference:
    // https://ipapi.com/documentation
    // https://ipapi.com/documentation#api_response_objects
    private String requestCountryInfo(InetAddress ipAddress) {
        return """
            {   "ip": %s,
                "country_code": "AR",
                "country_name": "Argentina",
                "latitude": 38.4161,
                "longitude": 63.6167,
                "location": {
                    "languages": [
                       {
                            "code": "es",
                            "name": "Spanish"
                       },
                       {
                            "code": "gn",
                            "name": "Guarani"
                       }
                    ]
                }
            }
            """.formatted(ipAddress.getHostAddress());
    }
}
