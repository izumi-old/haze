package org.izumi.haze.modules.java.util;

import org.izumi.haze.modules.java.source.Class;

import java.util.Collection;
import java.util.Map;

public interface RenamingGenerator {
    Map<Class, String> generate(Collection<Class> classes);
}
