package org.izumi.haze.modules;

import lombok.NonNull;
import org.izumi.haze.filesystem.Extension;

import java.util.Map;
import java.util.UUID;

public interface Module {
    boolean canHandle(@NonNull Extension extension);
    Content handle(@NonNull Content content);
    Map<UUID, Content> handle(@NonNull Map<UUID, Content> contents);
}
