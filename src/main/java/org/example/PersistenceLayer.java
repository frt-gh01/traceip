package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class PersistenceLayer {
    List<TraceResult> traceResults = new ArrayList<>();
    Map<String, Double> distanceToBsAsByCountry = new HashMap<>();

    public void persist(TraceResult traceResult) {
        // Add record
        this.traceResults.add(traceResult);

        // Add distance to Buenos Aires
        distanceToBsAsByCountry.putIfAbsent(traceResult.countryCode(), traceResult.distanceKilometersToBuenosAires());
    }

    public int recordsCount() {
        return this.traceResults.size();
    }

    public List<TraceResult> recordsByCountry(String countryCode) {
        return this.traceResults.stream()
                .filter(traceResult -> Objects.equals(traceResult.countryCode(), countryCode))
                .collect(Collectors.toList());
    }

    public Optional<Double> distanceToBuenosAiresByCountry(String countryCode) {
        Double distance = distanceToBsAsByCountry.get(countryCode);

        if (distance == null) {
            return Optional.empty();
        } else {
            return Optional.of(distance);
        }
    }
}
