package org.izumi.haze.modules.stages;

import lombok.NonNull;

public interface Stage {
    String apply(@NonNull String string);
}
