package org.izumi.haze.modules.impl.java.util;

import org.izumi.haze.modules.impl.java.source.Class;

import java.util.Optional;

public interface Classes extends Elements<Class> {
    Optional<Class> findByName(String name);
}
