package org.izumi.haze.modules.impl.java.source;

import org.izumi.haze.modules.Content;
import org.izumi.haze.modules.impl.java.parsing.ImportsParsing;
import org.izumi.haze.modules.impl.java.parsing.PackageParsing;
import org.izumi.haze.modules.impl.java.parsing.ClassesParsing;
import org.izumi.haze.modules.impl.java.parsing.CommentsParsing;
import org.izumi.haze.modules.impl.java.util.Classes;
import org.izumi.haze.modules.impl.java.util.Comments;
import org.izumi.haze.modules.impl.java.util.Imports;
import org.izumi.haze.modules.impl.java.util.impl.ClassesImpl;
import org.izumi.haze.modules.impl.java.util.impl.CommentsImpl;
import org.izumi.haze.modules.impl.java.util.impl.ElementsImpl;
import org.izumi.haze.string.HazeRegexString;
import org.izumi.haze.string.HazeStringBuilder;
import org.izumi.haze.modules.impl.java.util.impl.ImportsImpl;
import org.izumi.haze.string.Regex;
import org.izumi.haze.util.RangeMap;
import org.izumi.haze.string.HazeString;
import org.izumi.haze.util.Range;

import java.util.Map;
import java.util.Optional;
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

    public Classes getClasses() {
        Classes classes = new ClassesImpl();
        for (Class clazz : this.classes) {
            classes.addAll(clazz.getClasses());
            classes.add(clazz);
        }

        return classes;
    }

    public void renameClassAndUsages(String name, String replacement) {
        Optional<Class> optional = classes.findByName(name);
        if (optional.isPresent() && optional.get().isPublic()) {
            fileName = replacement;
        }

        imports.renameClassAndUsages(name, replacement);
        classes.renameClassAndUsages(name, replacement);
        comments.renameClassAndUsages(name, replacement);
    }

    public void parse() {
        HazeRegexString contentAsString = new HazeRegexString(content.content)
                .replaceAll(new Regex("[ ]*\n[ ]*"), " ")
                .replaceAll(new Regex("[ ]*\r[ ]*"), " ")
                .replaceAll(new Regex("[ ]*\n\r[ ]*"), " ")
                .replaceAll(new Regex("[ ][ ]*"), " ");
        SortedMap<Range, Package> packagesMap = new PackageParsing(contentAsString).parse();
        SortedMap<Range, Import> importsMap = new ImportsParsing(contentAsString).parse();
        SortedMap<Range, Class> classesMap = new ClassesParsing(contentAsString).parse();
        SortedMap<Range, Comment> commentsMap = new CommentsParsing(contentAsString).parse();

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
            Comment comment = new Comment(contentAsString.getSub(unusedRange));
            comments.add(comment);
            elementsMap.put(unusedRange, comment);
        }

        for (Map.Entry<Range, Element> entry : elementsMap.entrySet()) {
            entry.getValue().setDeclarationOrder(declarationOrder++);
        }
    }

    private Range getBodyRange() {
        HazeString string = new HazeString(this.content.content);
        return new Range(
                string.firstRangeOf("{").get().start + 1,
                string.lastRangeOf("}").get().start - 1);
    }

    public Content toContent() {
        return new Content(fileName, toString());
    }

    @Override
    public String toString() {
        HazeStringBuilder builder = new HazeStringBuilder()
                .append(aPackage);

        if (aPackage.isNotDefault()) {
            builder = builder.appendSpace();
        }

        if (imports.isNotEmpty()) {
            builder = builder.appendEachSeparateLine(imports.getOrderedCopy())
                    .appendSpace();
        }

        return builder.appendEachSeparateSpace(new ElementsImpl(classes, comments).getOrderedCopy())
                .build();
    }
}
