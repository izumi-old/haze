package org.izumi.haze.modules.impl.java.parsing;

import org.izumi.haze.string.HazeRegexString;
import org.izumi.haze.string.Regex;
import org.izumi.haze.util.Range;

import java.util.Iterator;

public class LemmasIterator implements Iterator<Lemma> {
    private HazeRegexString value;

    public LemmasIterator(HazeRegexString value) {
        this.value = value.replaceAll(new Regex("[ ]*\n[ ]*"), " ")
                .replaceAll(new Regex("[ ]*\r[ ]*"), " ");
    }

    @Override
    public boolean hasNext() {
        return value.isNotEmpty();
    }

    @Override
    public Lemma next() {
        int beforeSpace = value.lastRangeOf(" ").get().start;
        Lemma lemma;
        if (beforeSpace == -1) {
            Range range = new Range(value);
            lemma = new Lemma(range, value.getSub(range));
            value = new HazeRegexString("");
        } else {
            int afterSpace = value.length() - 1;
            Range range = new Range(beforeSpace + 1, afterSpace);
            lemma = new Lemma(range, value.getSub(range));
            value = value.getSub(new Range(0, beforeSpace - 1));
        }

        return lemma;
    }
}
