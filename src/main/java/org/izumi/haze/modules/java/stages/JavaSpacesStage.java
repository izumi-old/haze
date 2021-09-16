package org.izumi.haze.modules.java.stages;

import lombok.NonNull;
import org.izumi.haze.modules.java.source.Code;
import org.izumi.haze.modules.stages.SpacesStage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JavaSpacesStage extends SpacesStage implements JavaStage {

    @Override
    public Map<UUID, Code> applyCode(@NonNull Map<UUID, Code> code) {
        return new HashMap<>(code); //TODO: implement
    }
}
