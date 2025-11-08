package com.saucedemo.data;

import java.util.HashMap;
import java.util.Map;

public class Context {
    private static final Map<String, Object> context = new HashMap<String, Object>();

    public static void put(String key, Object value) {
        context.put(key, value);
    }

    public static <T> T get(String key) {
        return (T) context.get(key);
    }

    public static void clear() {
        context.clear();
    }
}
