package org.izumi.haze.modules.impl.java.parsing;

import org.izumi.haze.modules.impl.java.source.Keyword;
import org.izumi.haze.string.HazeString;

import java.util.Collection;

public class LazySignature extends Signature {
    private boolean parsed = false;

    public LazySignature(HazeString value) {
        super(value);
    }

    @Override
    public Signature parse() {
        if (parsed) {
            return this;
        }

        Signature signature = super.parse();
        parsed = true;
        return signature;
    }

    @Override
    public Recognition recognize() {
        if (!parsed) {
            parse();
        }

        return super.recognize();
    }

    @Override
    public Collection<Keyword> getKeywords() {
        if (!parsed) {
            parse();
        }

        return super.getKeywords();
    }

    @Override
    public Collection<HazeString> getUnresolved() {
        if (!parsed) {
            parse();
        }

        return super.getUnresolved();
    }
}
