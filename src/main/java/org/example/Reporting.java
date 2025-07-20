package org.example;

import java.util.Optional;

public class Reporting {

    private final PersistenceLayer persistenceLayer;

    public Reporting(PersistenceLayer persistenceLayer) {
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
}
