package org.example.services;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class RequestExecutorCached extends RequestExecutor {
    private final Map<Integer, String> cache;
    private int cacheHitsCount;

    public RequestExecutorCached() {
        super();
        this.cache = new HashMap<Integer, String>();
        this.cacheHitsCount = 0;
    }

    public int cacheHitsCount() {
        return cacheHitsCount;
    }

    String execute(Object obj, Function<Object, String> fn) {
        int hashCode = obj.hashCode();
        String cachedJson = cache.get(hashCode);

        if (cachedJson == null) {
            String jsonResponse = fn.apply(obj);
            cache.put(hashCode, jsonResponse);
            return jsonResponse;
        } else {
            this.cacheHitsCount += 1;
            return cachedJson;
        }
    }
}
