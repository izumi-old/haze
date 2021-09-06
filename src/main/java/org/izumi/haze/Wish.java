package org.izumi.haze;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.izumi.haze.filesystem.File;

@EqualsAndHashCode
@RequiredArgsConstructor
public class Wish {

    @NonNull
    public final File source;

    @NonNull
    public final File target;
}
