package org.izumi.haze.modules.java.parsing;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.modules.java.source.Scope;
import org.izumi.haze.modules.java.util.Scopes;
import org.izumi.haze.util.Range;
import org.izumi.haze.util.StringBuilder;

@RequiredArgsConstructor
public class TopLevelScopesParsing {
    private final StringBuilder value;
    private final Range range;

    public Scopes parse() {
        int metUnresolvedOpening = -1;
        int start = -1;
        int end;
        Scopes result = new Scopes();
        for (int i = range.start; i < range.end; i++) {
            char c = value.charAt(i);
            if (c == '{') {
                if (metUnresolvedOpening == -1) {
                    start = i;
                    metUnresolvedOpening = 0;
                } else {
                    metUnresolvedOpening++;
                }
            }

            if (c == '}') {
                if (metUnresolvedOpening == 0) {
                    end = i+1;

                    Range range = new Range(start, end);
                    if (isScope(range)) {
                        result.add(new Scope(value, range));
                    }
                    metUnresolvedOpening = -1;
                } else {
                    metUnresolvedOpening--;
                }
            }
        }

        return result;
    }

    private boolean isScope(Range range) {
        return new Recognition(value, range).recognize() == Recognition.Element.SCOPE;
    }
}
