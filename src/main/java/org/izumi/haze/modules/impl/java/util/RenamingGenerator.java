package org.izumi.haze.modules.impl.java.util;

import org.izumi.haze.modules.impl.java.source.Class;

import java.util.Collection;
import java.util.Map;

public interface RenamingGenerator {
    Map<Class, String> generate(Collection<Class> classes);
}
