package org.example.db;

import jdk.jshell.spi.ExecutionControl;
import org.example.TraceResult;
import org.javatuples.Pair;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class PersistenceLayer {
    List<TraceResult> traceResults = new ArrayList<>();

    // WARNING: Eager calculations.
    // They will be updated when persisting a new traceResult.
    Map<String, Double> distanceToBsAsByCountry = new HashMap<>();
    // TODO: The following might be implemented as lazy calculation:
    Pair<Optional<String>, Optional<Double>> farthestDistanceToBuenosAires = Pair.with(Optional.empty(), Optional.empty());
    Pair<Optional<String>, Optional<Double>> closestDistanceToBuenosAires = Pair.with(Optional.empty(), Optional.empty());
    // The following needs to be eager calculation because of app usage:
    Optional<Double> averageDistance = Optional.empty();

    // @Transactional
    public void persist(TraceResult traceResult) {

        this.traceResults.add(traceResult);

        this.updateDistanceToBsAsByCountry(traceResult);
        this.updateFarthestDistanceToBsAs(traceResult);
        this.updateClosestDistanceToBsAs(traceResult);
        this.updateAverageDistanceToBsAs(traceResult);
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

    public Optional<Double> queryAverageDistanceToBuenosAires() {
        throw new RuntimeException("Should be implemented.");
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

    protected void updateAverageDistanceToBsAs(TraceResult traceResult) {
        throw new RuntimeException("Should be implemented.");
    }

    // TODO: remove repeated code with GeoPosition
    protected double truncate(double number) {
        BigDecimal truncated = new BigDecimal(number).setScale(2, RoundingMode.DOWN);
        return truncated.doubleValue();
    }
}
