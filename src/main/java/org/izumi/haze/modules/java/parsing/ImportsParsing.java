package org.izumi.haze.modules.java.parsing;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.modules.java.Keyword;
import org.izumi.haze.modules.java.source.Imports;
import org.izumi.haze.util.Range;
import org.izumi.haze.util.StringBuilder;

@RequiredArgsConstructor
public class ImportsParsing {
    private final StringBuilder value;

    public Imports parse() {
        int importsEnd = value.lastIndexOf(Keyword.IMPORT.toString());
        importsEnd = value.indexOf(";", importsEnd) + 1;

        return new Imports(value, new Range(0, importsEnd));
    }
}
