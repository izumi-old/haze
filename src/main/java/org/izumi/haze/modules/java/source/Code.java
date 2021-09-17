package org.izumi.haze.modules.java.source;

import lombok.NonNull;
import org.izumi.haze.modules.Content;
import org.izumi.haze.modules.java.parsing.ImportsParsing;
import org.izumi.haze.modules.java.parsing.TopLevelClassesParsing;
import org.izumi.haze.util.Range;
import org.izumi.haze.util.StringBuilder;

import java.util.ArrayList;
import java.util.Collection;

public class Code {
    private final StringBuilder value;
    private String fileName;
    private Collection<Class> topLevelClasses;
    private Imports imports;

    public Code(@NonNull Content content) {
        this(content.content, content.fileName);
    }

    public Code(@NonNull String value, @NonNull String fileName) {
        this.value = new StringBuilder(value);
        this.fileName = fileName;
    }

    public Collection<Class> getClasses() {
        return new ArrayList<>(topLevelClasses);
    }

    public void renameImports(@NonNull Class clazz, @NonNull String replacement) {
        int changed = imports.renameClass(clazz, replacement);
        for (Class topLevelCLass :topLevelClasses) {
            topLevelCLass.shift(changed);
        }
    }

    public void renameClasses(@NonNull Class clazz, @NonNull String onWhat) {
        int changed = value.replaceAllIfSeparate(clazz.getName(), onWhat);
        for (Class topLevelClass : topLevelClasses) {
            topLevelClass.shift(changed);
        }

        if (topLevelClasses.contains(clazz) && clazz.isPublic()) {
            fileName = onWhat;
        }
    }

    public void deleteAll(@NonNull String what) {
        value.replaceAllIfSeparate(what, "");
    }

    public void parse() {
        this.imports = new ImportsParsing(value).parse();
        this.topLevelClasses = new TopLevelClassesParsing(value, new Range(0, value.length())).parse();
        for (Class topLevelClass : topLevelClasses) {
            topLevelClass.parse();
        }
    }

    public Content toContent() {
        return new Content(fileName, value.toString());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
