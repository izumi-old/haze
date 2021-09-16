package org.izumi.haze.modules.java.stages;

import lombok.NonNull;
import org.izumi.haze.modules.java.source.Code;
import org.izumi.haze.modules.stages.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface JavaStage extends Stage {
    Map<UUID, Code> applyCode(@NonNull Map<UUID, Code> code);

    @Override
    default String apply(@NonNull String string) {
        throw new UnsupportedOperationException(); //TODO: bad design!
    }

    @Override
    default Map<UUID, String> apply(@NonNull Map<UUID, String> strings) {
        return mapFrom(applyCode(mapTo(strings)));
    }

    private Map<UUID, String> mapFrom(Map<UUID, Code> handled) {
        Map<UUID, String> result = new HashMap<>();
        for (Map.Entry<UUID, Code> entry : handled.entrySet()) {
            result.put(entry.getKey(), entry.getValue().toString());
        }
        return result;
    }

    private Map<UUID, Code> mapTo(Map<UUID, String> strings) {
        Map<UUID, Code> code = new HashMap<>();
        for (Map.Entry<UUID, String> entry : strings.entrySet()) {
            code.put(entry.getKey(), new Code(entry.getValue()));
        }
        return code;
    }
}
