package org.example.services;

import java.util.function.Function;

public class RequestExecutor {
    String execute(Object obj, Function<Object, String> fn) {
        return fn.apply(obj);
    }
}
