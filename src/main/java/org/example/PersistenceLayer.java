package org.example;

import org.javatuples.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class PersistenceLayer {
    List<TraceResult> traceResults = new ArrayList<>();
    Map<String, Double> distanceToBsAsByCountry = new HashMap<>();
    Pair<Optional<String>, Optional<Double>> farthestDistanceToBuenosAires = Pair.with(Optional.empty(), Optional.empty());
    Pair<Optional<String>, Optional<Double>> closestDistanceToBuenosAires = Pair.with(Optional.empty(), Optional.empty());

    // @Transactional
    public void persist(TraceResult traceResult) {

        this.traceResults.add(traceResult);

        this.updateDistanceToBsAsByCountry(traceResult);
        this.updateFarthestDistanceToBsAs(traceResult);
        this.updateClosestDistanceToBsAs(traceResult);
    }

    public int queryTraceResultsCount() {
        return this.traceResults.size();
    }

    public List<TraceResult> queryTraceResultsByCountry(String countryCode) {
        return this.traceResults.stream()
                .filter(traceResult -> Objects.equals(traceResult.countryCode(), countryCode))
                .collect(Collectors.toList());
    }

    public Optional<Double> queryDistanceToBuenosAiresByCountry(String countryCode) {
        Double distance = distanceToBsAsByCountry.get(countryCode);

        if (distance == null) {
            return Optional.empty();
        } else {
            return Optional.of(distance);
        }
    }

    public Optional<Double> queryFarthestDistanceToBuenosAires() {
        return farthestDistanceToBuenosAires.getValue1();
    }

    public Optional<Double> queryClosestDistanceToBuenosAires() {
        return closestDistanceToBuenosAires.getValue1();
    }

    private void updateDistanceToBsAsByCountry(TraceResult traceResult) {
        distanceToBsAsByCountry.putIfAbsent(traceResult.countryCode(), traceResult.distanceKilometersToBuenosAires());
    }

    private void updateFarthestDistanceToBsAs(TraceResult traceResult) {
        Optional<Double> farthestDistance = farthestDistanceToBuenosAires.getValue1();
        double traceResultDistance = traceResult.distanceKilometersToBuenosAires();

        if (farthestDistance.isEmpty() || farthestDistance.get() < traceResultDistance) {
            farthestDistanceToBuenosAires = Pair.with(Optional.of(traceResult.countryCode()), Optional.of(traceResult.distanceKilometersToBuenosAires()));
        }
    }

    private void updateClosestDistanceToBsAs(TraceResult traceResult) {
        Optional<Double> closestDistance = closestDistanceToBuenosAires.getValue1();
        double traceResultDistance = traceResult.distanceKilometersToBuenosAires();

        if (closestDistance.isEmpty() || closestDistance.get() > traceResultDistance) {
            closestDistanceToBuenosAires = Pair.with(Optional.of(traceResult.countryCode()), Optional.of(traceResult.distanceKilometersToBuenosAires()));
        }
    }
}
