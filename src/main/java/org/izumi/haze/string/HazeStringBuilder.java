package org.izumi.haze.string;

import java.util.Collection;

public class HazeStringBuilder {
    private static final String NEW_LINE = "\n";
    private static final String SPACE = " ";
    private final StringBuilder builder;

    public HazeStringBuilder() {
        this.builder = new StringBuilder();
    }

    public <T> HazeStringBuilder append(T t) {
        builder.append(t.toString());
        return this;
    }

    public <T> HazeStringBuilder appendSpace() {
        builder.append(SPACE);
        return this;
    }

    public <T> HazeStringBuilder appendSeparateLine(T t) {
        builder.append(t.toString()).append(NEW_LINE);
        return this;
    }

    public <T> HazeStringBuilder appendEachSeparateSpace(Collection<T> ts) {
        for (T t : ts) {
            builder.append(t.toString()).append(SPACE);
        }
        return this;
    }

    public <T> HazeStringBuilder appendEachSeparateLine(Collection<T> ts) {
        for (T t : ts) {
            builder.append(t.toString()).append(NEW_LINE);
        }
        return this;
    }

    public HazeStringBuilder appendNewLine() {
        builder.append(NEW_LINE);
        return this;
    }

    public String build() {
        return builder.toString();
    }

    @Override
    public String toString() {
        return build();
    }
}
