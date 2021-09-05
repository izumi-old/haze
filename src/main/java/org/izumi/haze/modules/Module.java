package org.izumi.haze.modules;

import org.izumi.haze.filesystem.Extension;
import org.izumi.haze.filesystem.Files;

public interface Module {
    boolean canHandle(Extension extension);
    void handle(Files files);
}
