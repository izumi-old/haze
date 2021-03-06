package org.izumi.haze.modules.impl.java.parsing;

import org.izumi.haze.modules.impl.java.source.Comment;
import org.izumi.haze.string.HazeRegexString;
import org.izumi.haze.util.Range;
import org.izumi.haze.string.Regex;
import org.izumi.haze.string.HazeString;

import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

public class CommentsParsing {
    private static final Regex COMMENT_REGEX = new Regex("/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/");
    private final HazeRegexString value;
    private final Range range;

    public CommentsParsing(HazeString value, Range range) {
        this.value = new HazeRegexString(value);
        this.range = range;
    }

    public CommentsParsing(HazeString value) {
        this(value, new Range(value));
    }

    public SortedMap<Range, Comment> parse() {
        SortedMap<Range, Comment> map = new TreeMap<>();
        Range searchIn = range;
        while (true) {
            Optional<Range> optional = value.firstRangeOfRegex(searchIn, COMMENT_REGEX);
            if (optional.isEmpty()) {
                return map;
            }

            Range range = optional.get();
            map.put(range, new Comment(value.getSub(range)));
            searchIn = new Range(range.end, this.range.end);
        }
    }
}
