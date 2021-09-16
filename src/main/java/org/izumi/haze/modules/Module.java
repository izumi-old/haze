package org.izumi.haze.modules;

import lombok.NonNull;
import org.izumi.haze.filesystem.Extension;

import java.util.Map;
import java.util.UUID;

public interface Module {
    boolean canHandle(@NonNull Extension extension);
    String handle(@NonNull String content);
    Map<UUID, String> handle(@NonNull Map<UUID, String> contents);
}
