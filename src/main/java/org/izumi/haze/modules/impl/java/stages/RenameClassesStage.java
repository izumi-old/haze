package org.izumi.haze.modules.impl.java.stages;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.izumi.haze.modules.impl.java.source.Class;
import org.izumi.haze.modules.impl.java.source.File;
import org.izumi.haze.modules.impl.java.util.RenamingGenerator;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class RenameClassesStage implements JavaStage {
    private final RenamingGenerator generator;

    @Override
    public File apply(@NonNull File file) {
        file = new File(file);
        Map<Class, String> renamingMap = generateNameChangingMap(Map.of(new Object(), file));

        for (Map.Entry<Class, String> entry : renamingMap.entrySet()) {
            file.renameClassAndUsages(entry.getKey(), entry.getValue());
        }

        return file;
    }

    @Override
    public Map<UUID, File> apply(@NonNull Map<UUID, File> files) {
        files = copy(files);
        Map<Class, String> renamingMap = generateNameChangingMap(files);
        files.forEach((i, f) -> renamingMap.forEach(f::renameClassAndUsages));
        return files;
    }

    private Map<Class, String> generateNameChangingMap(Map<?, File> files) {
        Collection<Class> classes = new LinkedList<>();
        files.forEach((i, c) -> classes.addAll(c.getClasses()));
        return generator.generate(classes);
    }
}
