package org.izumi.haze.modules.stages;

import lombok.NonNull;
import org.izumi.haze.modules.Content;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface Stage {
    Content apply(@NonNull Content content);
    default Map<UUID, Content> apply(@NonNull Map<UUID, Content> strings) {
        Map<UUID, Content> result = new HashMap<>();
        for (Map.Entry<UUID, Content> entry : strings.entrySet()) {
            result.put(entry.getKey(), apply(entry.getValue()));
        }

        return result;
    }
}
