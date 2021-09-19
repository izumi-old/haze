package org.izumi.haze.modules.java.source;

import org.izumi.haze.modules.Content;
import org.izumi.haze.modules.java.parsing.ImportsParsing;
import org.izumi.haze.modules.java.parsing.PackageParsing;
import org.izumi.haze.modules.java.parsing.TopLevelClassesParsing;
import org.izumi.haze.modules.java.parsing.TopLevelCommentsParsing;
import org.izumi.haze.modules.java.util.Classes;
import org.izumi.haze.modules.java.util.Comments;
import org.izumi.haze.modules.java.util.Imports;
import org.izumi.haze.modules.java.util.impl.ClassesImpl;
import org.izumi.haze.modules.java.util.impl.CommentsImpl;
import org.izumi.haze.modules.java.util.impl.ElementsImpl;
import org.izumi.haze.modules.java.util.impl.HazeStringBuilder;
import org.izumi.haze.modules.java.util.impl.ImportsImpl;
import org.izumi.haze.modules.java.util.impl.RangeMap;
import org.izumi.haze.util.HazeString;
import org.izumi.haze.util.Range;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;

public class File {
    private final Content content;

    private Package aPackage;
    private Imports imports;
    private Classes classes;
    private Comments comments;
    private String fileName;
    private boolean parsed = false;

    public File(Content content) {
        this.content = content;
        this.fileName = content.fileName;
    }

    public File(File file) {
        this(file.content);

        this.aPackage = file.aPackage;
        this.imports = file.imports;
        this.classes = file.classes;
        this.comments = file.comments;
        this.fileName = file.fileName;
        this.parsed = file.parsed;
    }

    public Collection<Class> getClasses() {
        return new ArrayList<>(classes);
    }

    public void renameClassAndUsages(Class clazz, String replacement) {
        if (classes.contains(clazz) && clazz.isPublic()) {
            fileName = replacement;
        }

        imports.renameClassAndUsages(clazz, replacement);
        classes.renameClassAndUsages(clazz, replacement);
        comments.renameClassAndUsages(clazz, replacement);
    }

    public void parse() {
        HazeString contentAsString = new HazeString(content.content);
        SortedMap<Range, Package> packagesMap = new PackageParsing(contentAsString).parse();
        SortedMap<Range, Import> importsMap = new ImportsParsing(contentAsString).parse();
        SortedMap<Range, Class> classesMap = new TopLevelClassesParsing(contentAsString).parse();
        SortedMap<Range, Comment> commentsMap = new TopLevelCommentsParsing(contentAsString).parse();

        long declarationOrder = 1L;
        aPackage = packagesMap.values().iterator().next();
        aPackage.setDeclarationOrder(declarationOrder++);

        imports = new ImportsImpl();
        for (Map.Entry<Range, Import> entry : importsMap.entrySet()) {
            Import anImport = entry.getValue();
            anImport.setDeclarationOrder(declarationOrder++);
            imports.add(anImport);
        }

        classes = new ClassesImpl();
        for (Map.Entry<Range, Class> entry : classesMap.entrySet()) {
            Class clazz = entry.getValue();
            clazz.initFields();
            clazz.parse();
            classes.add(clazz);
        }

        comments = new CommentsImpl();
        for (Map.Entry<Range, Comment> entry : commentsMap.entrySet()) {
            Comment comment = entry.getValue();
            comments.add(comment);
        }

        RangeMap<Element> elementsMap = new RangeMap<>();
        elementsMap.putAll(classesMap, commentsMap);

        for (Range unusedRange : elementsMap.getUnusedRanges(getBodyRange())) {
            Comment comment = new Comment(contentAsString.sub(unusedRange));
            comments.add(comment);
            elementsMap.put(unusedRange, comment);
        }

        for (Map.Entry<Range, Element> entry : elementsMap.entrySet()) {
            entry.getValue().setDeclarationOrder(declarationOrder++);
        }
    }

    private Range getBodyRange() {
        HazeString string = new HazeString(this.content.content);
        return new Range(string.firstIndexOf("{") + 1,
                string.lastIndexOf("}") - 1);
    }

    public Content toContent() {
        return new Content(fileName, toString());
    }

    @Override
    public String toString() {
        HazeStringBuilder builder = new HazeStringBuilder()
                .appendSeparateLine(aPackage);

        if (aPackage.isNotDefault()) {
            builder = builder.appendNewLine();
        }

        if (imports.isNotEmpty()) {
            builder = builder.appendEachSeparateLine(imports.getOrderedCopy())
                    .appendNewLine();
        }

        return builder.appendEachSeparateLine(new ElementsImpl(classes, comments).getOrderedCopy())
                .build();
    }
}
