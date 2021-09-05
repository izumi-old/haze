package org.izumi.haze.modules.impl;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.HazeException;
import org.izumi.haze.filesystem.Extension;
import org.izumi.haze.modules.Module;
import org.izumi.haze.modules.ModuleFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;

@RequiredArgsConstructor
@Component
public class ModuleFactoryImpl implements ModuleFactory {
    private final Collection<Module> modules;

    @Override
    public Module getAbleToHandle(Extension extension) throws HazeException {
        for (Module module : modules) {
            if (module.canHandle(extension)) {
                return module;
            }
        }

        throw new HazeException("No one module able to handle given extension was found. Extension: " + extension);
    }
}
