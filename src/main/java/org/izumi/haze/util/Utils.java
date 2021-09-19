package org.izumi.haze.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Utils {
    public static <K, V1, V2> Map<K, V2> map(Map<K, V1> map, Function<V1, V2> function) {
        Map<K, V2> result = new HashMap<>();
        for (Map.Entry<K, V1> entry : map.entrySet()) {
            result.put(entry.getKey(), function.apply(entry.getValue()));
        }

        return result;
    }

    private Utils() {}
}
