package org.izumi.haze.modules.impl.java.parsing;

import org.izumi.haze.HazeException;
import org.izumi.haze.string.HazeString;
import org.izumi.haze.util.OptionalList;
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

        int start = string.firstRangeOf("{", currentRange).get().start;
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
        Optional<Range> optionalTextBlockStartRange = string.lastRangeOf("\"\"\"", range);
        Optional<Range> optionalTextBlockEndRange = string.firstRangeOf("\"\"\"", index);
        if (new OptionalList<>(optionalTextBlockStartRange, optionalTextBlockEndRange).allPresent()) {
            Range textBlockRange = new Range(
                    optionalTextBlockStartRange.get().start,
                    optionalTextBlockEndRange.get().start);
            if (textBlockRange.contains(index)) {
                return true;
            }
        }

        Optional<Range> optionalLineBreakingBeforeRange = string.lastRangeOf("\n", range);
        Optional<Range> optionalLineBreakingAfterRange = string.firstRangeOf("\n", index);
        Optional<Range> optionalOpeningRange = string.lastRangeOf(List.of("\"", "'"), range);
        Optional<Range> optionalClosingRange = string.firstRangeOf(List.of("\"", "'"), index);
        if (new OptionalList<>(optionalOpeningRange, optionalClosingRange).anyEmpty()) {
            return false;
        }

        Range literalRange = new Range(optionalOpeningRange.get().start, optionalClosingRange.get().start);
        return literalRange.doesNotContainAny(
                new OptionalList<>(optionalLineBreakingBeforeRange, optionalLineBreakingAfterRange).getAllPresent());
    }

    private boolean isNotInLiteral(int index) {
        return !isInLiteral(index);
    }
}
