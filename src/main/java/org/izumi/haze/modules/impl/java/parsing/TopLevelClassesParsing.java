package org.izumi.haze.modules.impl.java.parsing;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.modules.impl.java.source.Keyword;
import org.izumi.haze.modules.impl.java.source.Type;
import org.izumi.haze.modules.impl.java.source.Class;
import org.izumi.haze.string.Regex;
import org.izumi.haze.string.HazeRegexString;
import org.izumi.haze.util.Range;
import org.izumi.haze.string.HazeString;
import org.izumi.haze.util.Ranges;

import java.util.Iterator;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

@RequiredArgsConstructor
public class TopLevelClassesParsing {
    private final HazeString value;

    public SortedMap<Range, Class> parse() {
        SortedMap<Range, Class> map = new TreeMap<>();

        Iterator<Range> topLevelBracesIterator = new TopLevelBracesIterator(value);
        while (topLevelBracesIterator.hasNext()) {
            Range bracesRange = topLevelBracesIterator.next();
            //is a scope?
                //(yes) ignore

                //(no) parse signature
                //is a class?
                    //(no) ignore
                    //(yes) check for annotations. put class into result

            //if have a lemma before braces and the lemma doesn't end with ';' - it is a scope
            //otherwise it is a method or a class
        }

        /*Iterator<RangeClass> iterator = new TopClassesIterator(value);
        Collection<RangeClass> rangeClasses = new LinkedList<>();
        while (iterator.hasNext()) {
            rangeClasses.add(iterator.next());
        }

        Range range = new Range(value);
        for (int start = range.start; start <= range.end; start++) {
            start = value.firstIndexOf("{", new Range(start, range.end));
            if (start == -1) {
                break;
            }

            int openBraces = 1;
            for (int end = start+1; end <= range.end; end++) {
                char c = value.charAt(end);
                if (c == '}') {
                    openBraces--;
                    if (openBraces == 0) {
                        Range bracesRange = new Range(start, end);
                        *//*
                        Here I'm not sure it is a class actually. It can be a scope.
                        How can I detect that it is a class? I don't understand now...
                        I can have all in one line and it is okay.

                        I think I should try find ; symbol before braces. And it have be not in an another scope
                        If I met ; and not met any brace this means I detected signature + annotations
                         *//*
                        //TODO: exclude imports also
                        //TODO: I need only class signature + annotations
                        //TODO: actually it is a pure design to put annotations into signature. I should handle them in a separate way
                        Recognition recognition = new LazySignature(value.sub(bracesRange)).recognize();
                        if (recognition.isAnyClassType()) {
                            Optional<Range> optional = withSignature(bracesRange);
                            optional.ifPresent(rangeWithSignature -> {
                                Range rangeWithAnnotations = withAnnotations(rangeWithSignature);
                                map.put(rangeWithAnnotations, new Class(value.sub(rangeWithAnnotations)));
                            });
                        }

                        start = end;
                        break;
                    }
                }

                if (c == '{') {
                    openBraces++;
                }
            }
        }*/

        return map;
    }

    private Optional<Range> withSignature(Range range) {
        boolean exceptForEnd = true;
        int start;
        int end = range.start > 0 ? range.start - 1 : 0;
        while (true) {
            start = value.lastIndexOf(" ", new Range(0, end)) + 1;
            if (start == -1 || end == -1) {
                return Optional.empty();
            }

            String lemma = value.getSub(new Range(start, end)).toString();
            Optional<Keyword> optional = Keyword.of(lemma);
            if (optional.isPresent()) {
                if (Type.of(optional.get()).isPresent()) {
                    exceptForEnd = false;
                }
            } else if (!exceptForEnd) {
                Optional<String> optionalLemma = checkForLeftKeywords(lemma);
                if (optionalLemma.isPresent()) {
                    start += lemma.length() - optionalLemma.get().length();
                }
                return Optional.of(new Range(start, range.end));
            }

            end = start > 0 ? start - 1 : 0;
        }
    }

    private Range withAnnotations(Range range) {
        Range annotationsZone = new Range(0, range.start > 0 ? range.start - 1 : 0);
        HazeRegexString regexString = new HazeRegexString(value.getSub(annotationsZone));
        Ranges ranges = regexString.rangesOfRegex(new Regex("@.*@"));

        Optional<Range> min = ranges.getMin();
        if (min.isEmpty()) {
            ranges = regexString.rangesOfRegex(new Regex("@.*"));
            min = ranges.getMin();
            if (min.isEmpty()) {
                return range;
            } else {
                Range range1 = min.get();
                return new Range(range1.start, range.end);
            }
        } else {
            Range range1 = min.get();
            return new Range(range1.start, range.end);
        }
    }

    private Optional<String> checkForLeftKeywords(String lemma) {
        for (String s : lemma.split("\n")) {
            if (!"".equals(s) && Keyword.of(s).isPresent()) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }
}
