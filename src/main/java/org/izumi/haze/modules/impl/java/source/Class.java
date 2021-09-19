package org.izumi.haze.modules.impl.java.source;

import org.izumi.haze.HazeException;
import org.izumi.haze.modules.impl.java.parsing.TopLevelClassesParsing;
import org.izumi.haze.modules.impl.java.parsing.TopLevelScopesParsing;
import org.izumi.haze.modules.impl.java.util.Classes;
import org.izumi.haze.modules.impl.java.util.Comments;
import org.izumi.haze.modules.impl.java.util.Elements;
import org.izumi.haze.modules.impl.java.util.Scopes;
import org.izumi.haze.modules.impl.java.util.impl.ClassesImpl;
import org.izumi.haze.modules.impl.java.util.impl.CommentsImpl;
import org.izumi.haze.modules.impl.java.util.impl.ElementsImpl;
import org.izumi.haze.string.HazeStringBuilder;
import org.izumi.haze.util.RangeMap;
import org.izumi.haze.modules.impl.java.util.impl.ScopesImpl;
import org.izumi.haze.util.Range;
import org.izumi.haze.string.HazeString;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.function.BiPredicate;

public class Class implements Element {
    private final HazeString value;

    private long declarationOrder;

    private Classes classes; //can have annotations on it //TODO: need change top-level parsing to keep this information
    private Scopes scopes;
    private Comments comments;
    private Collection<Method> methods; //can have annotations on it
    private Collection<Annotation> annotations;
    private Collection<Variable> variables;

    private final Collection<Keyword> keywords = new LinkedList<>();
    private String name;
    private String otherSignature = ""; //TODO: generics of class name and implements/extends + parent class name with generics

    public Class(HazeString value) {
        this.value = value;
    }

    @Override
    public void renameClassAndUsages(String name, String replacement) {
        classes.renameClassAndUsages(name, replacement);
        scopes.renameClassAndUsages(name, replacement);
        comments.renameClassAndUsages(name, replacement);

        if (this.name.equals(name)) {
            this.name = replacement;
        }
    }

    @Override
    public long getDeclarationOrder() {
        return declarationOrder;
    }

    @Override
    public void setDeclarationOrder(long declarationOrder) {
        this.declarationOrder = declarationOrder;
    }

    public String getName() {
        return name;
    }

    public boolean isPublic() {
        return keywords.contains(AccessModifier.PUBLIC.getKeyword());
    }

    public void initFields() {
        classes = new ClassesImpl();
        scopes = new ScopesImpl();
        comments = new CommentsImpl();
        methods = new LinkedList<>();
        annotations = new LinkedList<>();
        variables = new LinkedList<>();

        String signature = getSignature();
        for (Keyword keyword : List.of(Keyword.ABSTRACT, Keyword.FINAL, Keyword.STATIC)) {
            if (signature.contains(keyword.toString())) {
                keywords.add(keyword);
            }
        }

        parseAccessModifier(signature);
        Type type = parseAndGetType(signature);

        String typeAsString = type.toString();
        int nameIndex = signature.indexOf(typeAsString) + typeAsString.length() + 1;
        name = signature.substring(nameIndex);
    }

    public void parse() {
        Range searchIn = getBodyRange();

        SortedMap<Range, Class> classesMap = new TopLevelClassesParsing(value, searchIn).parse();
        for (Map.Entry<Range, Class> entry : classesMap.entrySet()) {
            Class clazz = entry.getValue();
            clazz.initFields();
            clazz.parse();
            classes.add(clazz);
        }
        SortedMap<Range, Scope> scopesMap = new TopLevelScopesParsing(value, searchIn).parse();
        for (Map.Entry<Range, Scope> entry : scopesMap.entrySet()) {
            scopes.add(entry.getValue());
        }

        RangeMap<Element> elementsMap = new RangeMap<>();
        elementsMap.putAll(classesMap, scopesMap);

        for (Range unusedRange : elementsMap.getUnusedRanges(searchIn)) {
            Comment comment = new Comment(value.sub(unusedRange));
            comments.add(comment);
            elementsMap.put(unusedRange, comment);
        }

        setUpOrderNumbers(elementsMap);
        parseRecursive();
    }

    @Override
    public String toString() {
        Elements<?> ordered = new ElementsImpl(classes, scopes, comments,
                methods, annotations, variables).getOrderedCopy();
        return new HazeStringBuilder()
                .append(formSignature())
                .appendSpace()
                .append("{")
                .appendEachSeparateLine(ordered)
                .appendNewLine()
                .append("}")
                .appendNewLine()
                .build();
    }

    @SafeVarargs
    private <T, S> Optional<T> get(BiPredicate<T, S> predicate, S s, T... ts) {
        for (T t : ts) {
            if (predicate.test(t, s)) {
                return Optional.of(t);
            }
        }

        return Optional.empty();
    }

    private String getSignature() {
        return value.substring(new Range(0, value.indexOf("{")))
                .trim()
                .replaceAll(" implements.*", "") //remove implements information
                .replaceAll(" extends.*", "") //remove extends information
                .replaceAll("<.+>", ""); //remove generics information
    }

    private Type parseAndGetType(String signature) {
        Type type = get((t, s) -> s.contains(t.toString()), signature, Type.values())
                .orElseThrow(() -> new HazeException("Unknown type of class"));
        keywords.add(type.getKeyword());
        return type;
    }

    private void parseAccessModifier(String signature) {
        AccessModifier modifier = get((t, s) -> s.contains(t.toString()), signature, AccessModifier.values())
                .orElse(AccessModifier.DEFAULT);
        keywords.add(modifier.getKeyword());
    }

    private void setUpOrderNumbers(SortedMap<Range, Element> elementsMap) {
        long declarationOrder = 1L;
        for (Map.Entry<Range, Element> entry : elementsMap.entrySet()) {
            entry.getValue().setDeclarationOrder(declarationOrder++);
        }
    }

    private void parseRecursive() {
        for (Class clazz : classes) {
            clazz.initFields();
            clazz.parse();
        }
        for (Scope scope : scopes) {
            scope.parse();
        }
    }

    private String formSignature() {
        Collection<Keyword> keywords = new LinkedList<>();
        AccessModifier modifier = AccessModifier.DEFAULT;
        Type type = Type.CLASS;
        for (Keyword keyword : this.keywords) {
            Optional<AccessModifier> optionalModifier = AccessModifier.of(keyword.toString());
            Optional<Type> optionalType = Type.of(keyword);
            if (optionalModifier.isPresent()) {
                modifier = optionalModifier.get();
            } else if (optionalType.isPresent()) {
                type = optionalType.get();
            } else {
                keywords.add(keyword);
            }
        }

        return new HazeStringBuilder()
                .append(modifier.toString())
                .appendSpace()
                .appendEachSeparateSpace(keywords)
                .append(type.toString())
                .appendSpace()
                .append(name)
                .append(otherSignature)
                .build();
    }

    private Range getBodyRange() {
        return new Range(
                value.firstIndexOf("{") + 1,
                value.lastIndexOf("}") - 1);
    }
}
