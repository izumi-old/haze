package org.izumi.haze.modules.java.stages;

import lombok.NonNull;
import org.izumi.haze.modules.java.source.File;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface JavaStage {
    File apply(@NonNull File file);
    Map<UUID, File> apply(@NonNull Map<UUID, File> files);

    default Map<UUID, File> copy(Map<UUID, File> map) {
        Map<UUID, File> result = new HashMap<>();
        for (Map.Entry<UUID, File> entry : map.entrySet()) {
            result.put(entry.getKey(), new File(entry.getValue()));
        }

        return result;
    }
}
