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
                "latitude": 34,
                "longitude": 64,
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
