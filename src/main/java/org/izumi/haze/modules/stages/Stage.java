package org.izumi.haze.modules.stages;

import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface Stage {
    String apply(@NonNull String string);
    default Map<UUID, String> apply(@NonNull Map<UUID, String> strings) {
        Map<UUID, String> result = new HashMap<>();
        for (Map.Entry<UUID, String> entry : strings.entrySet()) {
            result.put(entry.getKey(), apply(entry.getValue()));
        }

        return result;
    }
}
