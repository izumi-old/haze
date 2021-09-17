package org.izumi.haze.modules.java;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.izumi.haze.filesystem.Extension;
import org.izumi.haze.modules.Content;
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
    public Content handle(@NonNull Content content) {
        Content result = content;
        for (Stage stage : stages) {
            result = stage.apply(content);
        }

        return result;
    }

    @Override
    public Map<UUID, Content> handle(@NonNull Map<UUID, Content> contents) {
        Map<UUID, Content> result = contents;
        for (Stage stage : stages) {
            result = stage.apply(contents);
        }

        return result;
    }
}
