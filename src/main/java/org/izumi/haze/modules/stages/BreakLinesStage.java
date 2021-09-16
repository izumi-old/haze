package org.izumi.haze.modules.stages;

import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class BreakLinesStage implements Stage {

    @Override
    public String apply(@NonNull String string) {
        return string
                .replaceAll("\n", "")
                .replaceAll("\r", "")
                .replaceAll("\n\r", "");
    }
}
