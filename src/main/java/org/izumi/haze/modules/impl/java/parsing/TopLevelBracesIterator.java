package org.izumi.haze.modules.impl.java.parsing;

import org.izumi.haze.HazeException;
import org.izumi.haze.string.HazeString;
import org.izumi.haze.util.Range;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class TopLevelBracesIterator implements Iterator<Range> {
    private final HazeString string;
    private Range currentRange;

    public TopLevelBracesIterator(HazeString string) {
        this.string = string;
        this.currentRange = new Range(string);
    }

    @Override
    public boolean hasNext() {
        return string.contains("{", currentRange) && string.contains("}", currentRange);
    }

    @Override
    public Range next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Iterator doesn't have elements");
        }

        int start = string.firstIndexOf("{", currentRange);
        int end = findClosingBrace(start).orElseThrow(() -> new HazeException("Here is not enough of closing braces"));

        Range bracesRange = new Range(start, end);
        if (bracesRange.end == string.getRange().get().end) {
            currentRange = new Range(0, 0);
        } else {
            currentRange = new Range(bracesRange.end + 1, string.getRange().get().end);
        }

        return bracesRange;
    }

    private Optional<Integer> findClosingBrace(int start) {
        int openBraces = 1;
        for (int end = start+1; end <= currentRange.end; end++) {
            char c = string.charAt(end);
            if (c == '}' && isNotInLiteral(end)) {
                openBraces--;
                if (openBraces == 0) {
                    return Optional.of(end);
                }
            }

            if (c == '{' && isNotInLiteral(end)) {
                openBraces++;
            }
        }

        return Optional.empty();
    }

    private boolean isInLiteral(int index) {
        if (index == 0 || index == string.getRange().get().end) {
            return false;
        }

        Range range = new Range(0, index);
        int textBlockStartIndex = string.lastIndexOf("\"\"\"", range);
        int textBlockEndIndex = string.firstIndexOf("\"\"\"", index) + 2;
        if (textBlockStartIndex != -1 && textBlockEndIndex > 1) {
            Range textBlockRange = new Range(textBlockStartIndex, textBlockEndIndex);
            if (textBlockRange.contains(index)) {
                return true;
            }
        }

        int lineBreakingIndexBefore = string.lastIndexOf("\n", range);
        int lineBreakingIndexAfter = string.firstIndexOf("\n", index);
        int openingIndex = string.lastIndexOf(List.of("\"", "'"), range);
        int closingIndex = string.firstIndexOf(List.of("\"", "'"), index);
        if (openingIndex == -1 || closingIndex == -1) {
            return false;
        }

        return new Range(openingIndex, closingIndex).doesNotContainAny(lineBreakingIndexBefore, lineBreakingIndexAfter);
    }

    private boolean isNotInLiteral(int index) {
        return !isInLiteral(index);
    }
}
