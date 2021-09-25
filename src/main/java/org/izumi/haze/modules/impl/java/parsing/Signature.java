package org.izumi.haze.modules.impl.java.parsing;

import lombok.RequiredArgsConstructor;
import org.izumi.haze.modules.impl.java.source.Keyword;
import org.izumi.haze.string.HazeString;
import org.izumi.haze.string.HazeRegexString;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class Signature {
    private final HazeString value;
    private Collection<Keyword> keywords;
    private Collection<HazeString> unresolved;

    public Signature parse() {
        this.keywords = new LinkedList<>();
        this.unresolved = new LinkedList<>();

        Iterator<Lemma> lemmasIterator = getLemmasIterator();
        while (lemmasIterator.hasNext()) {
            Lemma lemma = lemmasIterator.next();
            HazeString lemmaString = lemma.string;
            Optional<Keyword> optional = Keyword.of(lemmaString);
            if (optional.isEmpty()) {
                unresolved.add(lemmaString);
            } else {
                keywords.add(optional.get());
            }
        }

        return this;
    }

    public static void main(String[] args) {
        String s = "\n" +
                "@RequiredArgsConstructor\n" +
                "public abstract class Signature {";
        Signature signature = new LazySignature(new HazeString(s)).parse();
        int debug = 4;
    }

    public Recognition recognize() {
        return new Recognition(keywords, unresolved);
    }

    public Collection<Keyword> getKeywords() {
        return new LinkedList<>(keywords);
    }

    public Collection<HazeString> getUnresolved() {
        return new LinkedList<>(unresolved);
    }

    private Iterator<Lemma> getLemmasIterator() {
        return new LemmasIterator(new HazeRegexString(value));
    }
}
