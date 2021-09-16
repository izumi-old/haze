package org.izumi.haze.modules.java;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.izumi.haze.filesystem.Extension;
import org.izumi.haze.modules.Module;
import org.izumi.haze.modules.java.stages.JavaStage;
import org.izumi.haze.modules.stages.Stage;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

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

    @Override
    public Map<UUID, String> handle(@NonNull Map<UUID, String> contents) {
        Map<UUID, String> result = contents;
        for (Stage stage : stages) {
            result = stage.apply(contents);
        }

        return result;
    }
}
