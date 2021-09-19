package org.izumi.haze.modules.impl.java.source;

import java.util.Optional;

public enum Keyword {
    CLASS("class"),
    INTERFACE("interface"),
    ANNOTATION("@interface"),
    ENUM("enum"),
    STATIC("static"),
    ABSTRACT("abstract"),
    FINAL("final"),
    PUBLIC("public"),
    PRIVATE("private"),
    PROTECTED("protected"),
    VOID("void"),
    PACKAGE("package"),
    IMPORT("import"),
    VOLATILE("volatile"),
    SYNCHRONIZED("synchronized"),
    RETURN("return"),
    DEFAULT("default"),
    INT("int"),
    DOUBLE("double"),
    FLOAT("float"),
    CHAR("char"),
    LONG("long"),
    BYTE("byte"),
    BOOLEAN("boolean"),
    LAMBDA("lambda"),
    NEW("new"),
    FOR("for"),
    IF("if"),
    ELSE("else"),
    WHILE("while"),
    BREAK("break"),
    SWITCH("switch"),
    CASE("case"),
    DEFAULT_ACCESS_MODIFIER("");

    public static Optional<Keyword> of(String value) {
        for (Keyword keyword : Keyword.values()) {
            if (keyword.value.equals(value)) {
                return Optional.of(keyword);
            }
        }

        return Optional.empty();
    }

    private final String value;

    Keyword(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
