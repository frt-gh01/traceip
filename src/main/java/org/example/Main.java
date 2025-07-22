package org.example;

import org.apache.commons.cli.*;
import org.example.db.PersistenceLayerDB;
import org.example.services.StubsFactory;

import java.net.UnknownHostException;
import java.time.Clock;


public class Main {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("t", "trace", true, "Trace IP and returns summary");
        options.addOption("s", "stats", false, "Trace IP Report: farthest, closest and average distance to Buenos Aires.");

        try {
            CommandLineParser cliParser = new DefaultParser();
            CommandLine cli = cliParser.parse(options, args);

            if (cli.hasOption("trace")) {
                String ip = cli.getOptionValue("trace");
                runTraceIp(ip);
            }

            if (cli.hasOption("stats")) {
                runStatsGenerator();
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
        IpTracer ipTracer = new IpTracer(buildClock(),
                                         StubsFactory.buildIp2CountryServiceStub(),
                                         StubsFactory.buildTimeZoneServiceStub(buildClock()),
                                         StubsFactory.buildCurrencyServiceStub(buildClock()),
                                         buildPersistenceLayerDB());

        TraceResult traceResult = ipTracer.trace(ipAddress);
        System.out.println(traceResult);
    }

    public static void runStatsGenerator() {
        StatsGenerator statsGenerator = new StatsGenerator(buildPersistenceLayerDB());
        System.out.println(statsGenerator.generate());
    }

    private static Clock buildClock() {
        return Clock.systemUTC();
    }

    private static PersistenceLayerDB buildPersistenceLayerDB() {
        return new PersistenceLayerDB();
    }
}