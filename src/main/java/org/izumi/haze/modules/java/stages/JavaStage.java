package org.izumi.haze.modules.java.stages;

import lombok.NonNull;
import org.izumi.haze.modules.Content;
import org.izumi.haze.modules.java.source.Code;
import org.izumi.haze.modules.stages.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface JavaStage extends Stage {
    Map<UUID, Code> applyCode(@NonNull Map<UUID, Code> code);

    @Override
    default Content apply(@NonNull Content content) {
        throw new UnsupportedOperationException(); //TODO: bad design!
    }

    @Override
    default Map<UUID, Content> apply(@NonNull Map<UUID, Content> contents) {
        return mapFrom(applyCode(mapTo(contents)));
    }

    private Map<UUID, Content> mapFrom(Map<UUID, Code> handled) {
        Map<UUID, Content> result = new HashMap<>();
        for (Map.Entry<UUID, Code> entry : handled.entrySet()) {
            result.put(entry.getKey(), entry.getValue().toContent());
        }
        return result;
    }

    private Map<UUID, Code> mapTo(Map<UUID, Content> contents) {
        Map<UUID, Code> code = new HashMap<>();
        for (Map.Entry<UUID, Content> entry : contents.entrySet()) {
            code.put(entry.getKey(), new Code(entry.getValue()));
        }
        return code;
    }
}
