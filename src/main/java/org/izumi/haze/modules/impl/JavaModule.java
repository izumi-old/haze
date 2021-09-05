package org.izumi.haze.modules.impl;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.filesystem.Extension;
import org.izumi.haze.filesystem.File;
import org.izumi.haze.filesystem.Files;
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
    public boolean canHandle(Extension extension) {
        return extension != null && extension.is("java");
    }

    @Override
    public void handle(Files files) {
        for (File file : files) {
            String content = file.getContent();
            for (Stage stage : stages) {
                content = stage.apply(content);
            }
        }

        throw new UnsupportedOperationException(); //TODO: implement
    }
}
