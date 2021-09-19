package org.izumi.haze.modules.java.util.impl;

import org.izumi.haze.modules.java.source.Class;
import org.izumi.haze.modules.java.util.RenamingGenerator;
import org.izumi.haze.util.RandomIntegerList;
import org.izumi.haze.util.Range;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Component
public class RenamingGeneratorImpl implements RenamingGenerator {
    private final RandomIntegerList classNameLengthList = new RandomIntegerList()
            .fillValuesRangeInclude(new Range(32, 64));
    private final RandomIntegerList charactersList = new RandomIntegerList()
            .fillValuesRangeInclude(new Range(65, 90));
    private final RandomIntegerList symbolsPool = new RandomIntegerList()
            .fillValuesRangeInclude(new Range(48, 57))
            .fillValuesRangeInclude(new Range(65, 90))
            .fillValuesRangeInclude(new Range(97, 122))
            .add(128)
            .add(131)
            .fillValuesRangeInclude(new Range(192, 214));

    public RenamingGeneratorImpl() {
    }

    public Map<Class, String> generate(Collection<Class> classes) {
        Map<Class, String> map = new HashMap<>();
        Collection<String> used = new LinkedList<>();

        for (Class clazz : classes) {
            String name = generate();
            while (used.contains(name)) {
                name = generate();
            }

            used.add(name);
            map.put(clazz, name);
        }

        return map;
    }

    private String generate() {
        HazeStringBuilder builder = new HazeStringBuilder();
        builder.append(this.getRandomUpperLetter());

        for (int i = 1; i < this.classNameLengthList.getRandom(); i++) {
            char c = Character.toChars(this.symbolsPool.getRandom())[0];
            builder.append(c);
        }

        return builder.build();
    }

    private char getRandomUpperLetter() {
        return Character.toChars(this.charactersList.getRandom())[0];
    }
}
