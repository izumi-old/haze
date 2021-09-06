package org.izumi.haze.modules.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.izumi.haze.filesystem.Extension;
import org.izumi.haze.modules.Module;
import org.izumi.haze.modules.stages.java.JavaStage;
import org.izumi.haze.modules.stages.Stage;
import org.springframework.stereotype.Component;

import java.util.Collection;

@RequiredArgsConstructor
@Component
public class JavaModule implements Module {
    private final Collection<JavaStage> stages;

    @Override
    public boolean canHandle(@NonNull Extension extension) {
        return extension.is("java");
    }

    @Override
    public String handle(@NonNull String content) {
        String result = content;
        for (Stage stage : stages) {
            result = stage.apply(result);
        }

        return result;
    }
}
