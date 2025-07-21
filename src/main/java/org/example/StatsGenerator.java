package org.example;

import java.util.Optional;

public class StatsGenerator {

    private final PersistenceLayer persistenceLayer;

    public StatsGenerator(PersistenceLayer persistenceLayer) {
        this.persistenceLayer = persistenceLayer;
    }

    public Optional<Double> farthestDistanceToBuenosAires() {
        return this.persistenceLayer.queryFarthestDistanceToBuenosAires();
    }

    public Optional<Double> closestDistanceToBuenosAires() {
        return this.persistenceLayer.queryClosestDistanceToBuenosAires();
    }

    public Optional<Double> averageDistanceToBuenosAires() {
        return this.persistenceLayer.queryAverageDistanceToBuenosAires();
    }

    public String generate() {
        String farDistance = unwrapDoubleToString(this.farthestDistanceToBuenosAires());
        String closeDistance = unwrapDoubleToString(this.closestDistanceToBuenosAires());
        String avgDistance = unwrapDoubleToString(this.averageDistanceToBuenosAires());

        return """
            Distancia más lejana a Buenos Aires: %s
            Distancia más cercana a Buenos Aires: %s
            Distancia promedio: %s
        """.formatted(farDistance, closeDistance, avgDistance);
    }

    private String unwrapDoubleToString(Optional<Double> distance) {
        return distance.isPresent() ? distance.toString() : "No hay registros";
    }
}
