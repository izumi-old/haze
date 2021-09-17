package org.izumi.haze.modules.java.stages;

import lombok.NonNull;
import org.izumi.haze.modules.java.source.Code;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class RemoveCommentsStage implements JavaStage {

    @Override
    public Map<UUID, Code> applyCode(@NonNull Map<UUID, Code> code) {
        return code;
    }
}
