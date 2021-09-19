package org.izumi.haze.modules.java.parsing;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.modules.java.source.Comment;
import org.izumi.haze.util.Range;
import org.izumi.haze.util.Regex;
import org.izumi.haze.util.HazeString;

import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

@RequiredArgsConstructor
public class TopLevelCommentsParsing {
    private static final Regex COMMENT_REGEX = new Regex("/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/");
    private final HazeString value;
    private final Range range;

    public TopLevelCommentsParsing(HazeString value) {
        this(value, new Range(value));
    }

    public SortedMap<Range, Comment> parse() {
        SortedMap<Range, Comment> map = new TreeMap<>();
        Range searchIn = range;
        while (true) {
            Optional<Range> optional = value.rangeOf(searchIn, COMMENT_REGEX);
            if (optional.isEmpty()) {
                return map;
            }

            Range range = optional.get();
            map.put(range, new Comment(value.sub(range)));
            searchIn = new Range(range.end, this.range.end);
        }
    }
}
