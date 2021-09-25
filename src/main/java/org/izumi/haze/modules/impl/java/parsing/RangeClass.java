package org.izumi.haze.modules.impl.java.parsing;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.modules.impl.java.source.Class;
import org.izumi.haze.util.Range;

@RequiredArgsConstructor
public class RangeClass {
    public final Range range;
    public final Class clazz;
}
