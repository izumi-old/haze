package org.izumi.haze.stages;

import lombok.NonNull;

public interface Stage {
    String apply(@NonNull String string);
}
