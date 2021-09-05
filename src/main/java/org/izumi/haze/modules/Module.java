package org.izumi.haze.modules;

import org.izumi.haze.filesystem.Extension;
import org.izumi.haze.filesystem.File;

public interface Module {
    boolean canHandle(Extension extension);
    String handle(File file);
}
