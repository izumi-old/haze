package org.izumi.haze.modules.java.parsing;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.modules.java.Keyword;
import org.izumi.haze.modules.java.Type;
import org.izumi.haze.modules.java.source.Class;
import org.izumi.haze.modules.java.util.Classes;
import org.izumi.haze.util.Range;
import org.izumi.haze.util.StringBuilder;

import java.util.Optional;

@RequiredArgsConstructor
public class TopLevelClassesParsing {
    private final StringBuilder value;
    private final Range range;

    public Classes parse() {
        Classes result = new Classes();

        for (int start = range.start; start < range.end; start++) {
            start = value.firstIndexOf("{", new Range(start, range.end));
            if (start == -1) {
                break;
            }

            int openBraces = 0;
            for (int end = start+1; end < range.end; end++) {
                char c = value.charAt(end);
                if (c == '}') {
                    if (openBraces != 0) {
                        openBraces--;
                    } else {
                        Optional<Range> optional = withSignature(new Range(start, end + 1));
                        optional.ifPresent(range1 -> result.add(new Class(value, range1)));

                        start = start + end;
                        if (start >= range.end) {
                            return result;
                        }

                        break;
                    }
                }

                if (c == '{') {
                    openBraces++;
                }
            }
        }

        return result;
    }

    private Optional<Range> withSignature(Range range) {
        boolean exceptForEnd = true;
        int start;
        int end = range.start - 1;
        while (true) {
            start = value.lastIndexOf(" ", new Range(0, end)) + 1;
            if (start == -1) {
                return Optional.empty();
            }

            String lemma = value.substring(new Range(start, end));
            Optional<Keyword> optional = Keyword.of(lemma);
            if (optional.isPresent()) {
                if (Type.of(optional.get()).isPresent()) {
                    exceptForEnd = false;
                }
            } else if (!exceptForEnd) {
                return Optional.of(new Range(start, range.end));
            }

            end = start - 1;
        }
    }
}
