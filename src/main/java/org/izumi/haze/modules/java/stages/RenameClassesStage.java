package org.izumi.haze.modules.java.stages;

import lombok.NonNull;
import org.izumi.haze.modules.java.source.Class;
import org.izumi.haze.modules.java.source.Code;
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
    private final RandomList<Integer> integersRandomList = new RandomList<>(List.of(9, 8, 7, 6, 5, 4, 3, 2, 1));

    @Override
    public Map<UUID, Code> applyCode(@NonNull Map<UUID, Code> code) {
        code = new HashMap<>(code);
        code.forEach((i, c) -> c.parse());

        Collection<Class> classes = new LinkedList<>();
        code.forEach((i, c) -> classes.addAll(c.getClasses()));

        Map<Class, String> renamingMap = new HashMap<>();
        for (Class clazz : classes) {
            String newName = charactersRandomList.getRandom().toString() + integersRandomList.getRandom();
            renamingMap.put(clazz, newName);
        }

        for (Map.Entry<Class, String> entry : renamingMap.entrySet()) {
            for (Map.Entry<UUID, Code> entry1 : code.entrySet()) {
                entry1.getValue().renameImports(entry.getKey(), entry.getValue());
            }
        }
        for (Map.Entry<Class, String> entry : renamingMap.entrySet()) {
            for (Map.Entry<UUID, Code> entry1 : code.entrySet()) {
                entry1.getValue().renameClasses(entry.getKey(), entry.getValue());
            }
        }

        return code;
    }
}
