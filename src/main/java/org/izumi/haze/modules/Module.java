package org.izumi.haze.modules;

import lombok.NonNull;
import org.izumi.haze.filesystem.Extension;

public interface Module {
    boolean canHandle(@NonNull Extension extension);
    String handle(@NonNull String content);
}
