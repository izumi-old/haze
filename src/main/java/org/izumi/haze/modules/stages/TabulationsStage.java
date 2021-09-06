package org.izumi.haze.modules.stages;

import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class TabulationsStage implements Stage {

    @Override
    public String apply(@NonNull String string) {
        return string.replaceAll("\t", "");
    }
}
