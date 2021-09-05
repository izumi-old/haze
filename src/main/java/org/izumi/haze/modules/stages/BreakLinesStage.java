package org.izumi.haze.modules.stages;

import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class BreakLinesStage implements Stage {

    @Override
    public String apply(String string) {
        Objects.requireNonNull(string);
        return string
                .replaceAll("\n", "")
                .replaceAll("\r", "")
                .replaceAll("\n\r", "");
    }
}
