package org.example;

import org.apache.commons.cli.*;
import org.example.services.Ip2CountryService;
import org.example.services.Ip2CountryServiceStub;
import org.example.services.TimeZoneService;
import org.example.services.TimeZoneServiceStub;

import java.net.UnknownHostException;
import java.time.Clock;

public class Main {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("t", "trace", true, "Trace IP and returns summary");
        options.addOption("r", "report", false, "Trace IP Report: farthest, closest and average distance to Buenos Aires.");

        try {
            CommandLineParser cliParser = new DefaultParser();
            CommandLine cli = cliParser.parse(options, args);

            if (cli.hasOption("trace")) {
                String ip = cli.getOptionValue("trace");
                runTraceIp(ip);
            }

            if (cli.hasOption("report")) {
                runReporting();
            }

        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();

            System.err.println(e.getMessage());
            formatter.printHelp("trace", options);
            System.exit(1);
        } catch (UnknownHostException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void runTraceIp(String ipAddress) throws UnknownHostException {
        PersistenceLayer persistenceLayer = buildPersistenceLayer();
        IpTracer ipTracer = new IpTracer(buildIp2CountryServiceStub(),
                                         buildTimeZoneServiceStub(),
                                         persistenceLayer);

        TraceResult traceResult = ipTracer.trace(ipAddress);
        System.out.println(traceResult);
    }

    public static void runReporting() {
    }

    // TODO: Replace with actual Services and DB
    private static TimeZoneService buildTimeZoneServiceStub() {
        return new TimeZoneServiceStub(Clock.systemUTC(), """
            {   "name": %s,
                "timezones": [
                    "UTC-03:00",
                    "UTC-04:00"
                ]
            }
            """::formatted);
    }

    private static Ip2CountryService buildIp2CountryServiceStub() {
        String jsonArgentina = """
            { "ip": %s, "country_code": "AR", "country_name": "Argentina", "latitude": -34, "longitude": -64,
              "location": { "languages": [{ "code": "es", "name": "Spanish" }, { "code": "gn", "name": "Guarani" }] }
            }
            """;

        String jsonBrasil = """
            { "ip": %s, "country_code": "BR", "country_name": "Brasil", "latitude": -14.23, "longitude": -51.92,
              "location": { "languages": [{ "code": "pt", "name": "Portuguese" }] }
            }
            """;

        String jsonEspana = """
            { "ip": %s, "country_code": "ES", "country_name": "España", "latitude": 40.46, "longitude": 3.74,
              "location": { "languages": [{ "code": "es", "name": "Spanish" }, { "code": "ca", "name": "Catalan" },
                                          { "code": "gl", "name": "Galician" }] 
                                          
                                          }
            }
            """;

        String[] responses = { jsonArgentina, jsonBrasil, jsonEspana };

        return new Ip2CountryServiceStub(ipAddress -> {
            // Easy stub logic where IP's third octet relates to country
            // TODO: make safe method (ie. out of bounds, parseInt)
            String countryOctet = ipAddress.getHostAddress().split("\\.")[2];
            return responses[Integer.parseInt(countryOctet)].formatted(ipAddress.getHostAddress());
        });
    }

    private static PersistenceLayer buildPersistenceLayer() {
        return new PersistenceLayer();
    }
}