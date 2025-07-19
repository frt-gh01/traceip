package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PersistenceLayer {
    List<TraceResult> traceResults = new ArrayList<>();

    public void persist(TraceResult traceResult) {
        this.traceResults.add(traceResult);
    }

    public int traceResultsCount() {
        return this.traceResults.size();
    }

    public List<TraceResult> getTraceResults(String countryCode) {
        return this.traceResults.stream()
                .filter(traceResult -> Objects.equals(traceResult.countryCode(), countryCode))
                .collect(Collectors.toList());
    }
}
