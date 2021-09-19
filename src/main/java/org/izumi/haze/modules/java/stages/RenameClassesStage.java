package org.izumi.haze.modules.java.stages;

import lombok.NonNull;
import org.izumi.haze.modules.java.source.Class;
import org.izumi.haze.modules.java.source.File;
import org.izumi.haze.util.RandomIntegerList;
import org.izumi.haze.util.RandomList;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class RenameClassesStage implements JavaStage {
    private final RandomList<Character> charactersRandomList = new RandomList<>(List.of('A', 'B', 'C', 'D', 'E', 'F'));

    @Override
    public File apply(@NonNull File file) {
        file = new File(file);

        Collection<Class> classes = file.getClasses();
        RandomIntegerList list = new RandomIntegerList().addRandomValuesChaining(classes.size(), Integer.MAX_VALUE);
        Map<Class, String> renamingMap = new HashMap<>();
        for (Class clazz : classes) {
            String newName = charactersRandomList.getRandom().toString() + list.extractRandom();
            renamingMap.put(clazz, newName);
        }

        for (Map.Entry<Class, String> entry : renamingMap.entrySet()) {
            file.renameImports(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<Class, String> entry : renamingMap.entrySet()) {
            file.renameClassAndUsages(entry.getKey(), entry.getValue());
        }

        return file;
    }

    @Override
    public Map<UUID, File> apply(@NonNull Map<UUID, File> files) {
        files = copy(files);

        Collection<Class> classes = new LinkedList<>();
        files.forEach((i, c) -> classes.addAll(c.getClasses()));

        RandomIntegerList list = new RandomIntegerList().addRandomValuesChaining(classes.size(), Integer.MAX_VALUE);
        Map<Class, String> renamingMap = new HashMap<>();
        for (Class clazz : classes) {
            String newName = charactersRandomList.getRandom().toString() + list.extractRandom();
            renamingMap.put(clazz, newName);
        }

        for (Map.Entry<Class, String> entry : renamingMap.entrySet()) {
            for (Map.Entry<UUID, File> entry1 : files.entrySet()) {
                entry1.getValue().renameImports(entry.getKey(), entry.getValue());
            }
        }
        for (Map.Entry<Class, String> entry : renamingMap.entrySet()) {
            for (Map.Entry<UUID, File> entry1 : files.entrySet()) {
                entry1.getValue().renameClassAndUsages(entry.getKey(), entry.getValue());
            }
        }

        return files;
    }
}
