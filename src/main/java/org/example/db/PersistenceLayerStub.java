package org.example.db;

import org.example.TraceResult;
import org.javatuples.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class PersistenceLayerStub extends PersistenceLayer {
    @Override
    public Optional<Double> queryAverageDistanceToBuenosAires() {
        return averageDistance;
    }

    @Override
    protected void updateAverageDistanceToBsAs(TraceResult traceResult) {
        double prevCount = this.queryTraceResultsCount() - 1;

        double prevSum = prevCount * this.averageDistance.orElse(0.0);
        double newSum = prevSum + traceResult.distanceKilometersToBuenosAires();

        this.averageDistance = Optional.of(truncate(newSum / (prevCount + 1)));
    }
}
