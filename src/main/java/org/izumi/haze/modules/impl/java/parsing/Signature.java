package org.izumi.haze.modules.impl.java.parsing;

import org.izumi.haze.modules.impl.java.source.Keyword;
import org.izumi.haze.string.HazeString;
import org.izumi.haze.string.LemmaString;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

public abstract class Signature {
    private final LemmaString value;
    private Collection<Keyword> keywords;
    private Collection<HazeString> unresolved;

    public Signature(CharSequence sequence) {
        this.value = new LemmaString(sequence);
    }

    public Signature parse() {
        this.keywords = new LinkedList<>();
        this.unresolved = new LinkedList<>();

        Collection<LemmaString> lemmas = value.getLemmas();
        for (LemmaString lemma : lemmas) {
            Optional<Keyword> optional = Keyword.of(lemma);
            if (optional.isEmpty()) {
                unresolved.add(lemma);
            } else {
                keywords.add(optional.get());
            }
        }

        return this;
    }

    public Collection<Keyword> getKeywords() {
        return new LinkedList<>(keywords);
    }

    public Collection<HazeString> getUnresolved() {
        return new LinkedList<>(unresolved);
    }
}
