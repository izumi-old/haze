package org.izumi.haze.modules.java;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.izumi.haze.filesystem.Extension;
import org.izumi.haze.modules.Content;
import org.izumi.haze.modules.Module;
import org.izumi.haze.modules.java.source.File;
import org.izumi.haze.modules.java.stages.JavaStage;
import org.izumi.haze.util.Utils;
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
        File file = new File(content);

        file.parse();

        for (JavaStage stage : stages) {
            file = stage.apply(file);
        }

        return file.toContent();
    }

    @Override
    public Map<UUID, Content> handle(@NonNull Map<UUID, Content> contents) {
        Map<UUID, File> files = Utils.map(contents, File::new);

        for (Map.Entry<UUID, File> entry : files.entrySet()) {
            entry.getValue().parse();
        }

        for (JavaStage stage : stages) {
            files = stage.apply(files);
        }

        return Utils.map(files, File::toContent);
    }
}
