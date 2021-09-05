package org.izumi.haze;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.filesystem.File;

@RequiredArgsConstructor
public class Wish {
    public final File source;
    public final File target;
}
