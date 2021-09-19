package org.izumi.haze.modules.impl.java.stages;

import lombok.NonNull;
import org.izumi.haze.modules.impl.java.source.File;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class RemoveCommentsStage implements JavaStage {

    @Override
    public File apply(@NonNull File file) {
        return file;
    }

    @Override
    public Map<UUID, File> apply(@NonNull Map<UUID, File> files) {
        return files;
    }
}
