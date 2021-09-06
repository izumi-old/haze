package org.izumi.haze.stages;

import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SpacesStage implements Stage {

    @Override
    public String apply(@NonNull String string) {
        return string.replaceAll(" ", "");
    }
}
