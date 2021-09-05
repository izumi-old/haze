package org.izumi.haze.modules;

import org.izumi.haze.HazeException;
import org.izumi.haze.filesystem.Extension;

public interface ModuleFactory {
    Module getAbleToHandle(Extension extension) throws HazeException;
}
