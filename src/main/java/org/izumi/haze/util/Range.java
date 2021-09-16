package org.izumi.haze.util;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Range {
    public final int start;
    public final int end;

    public Range shift(int changed) {
        return new Range(start, end - changed);
    }
}
