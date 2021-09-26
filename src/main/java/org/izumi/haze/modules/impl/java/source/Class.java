package org.izumi.haze.modules.impl.java.source;

import org.izumi.haze.HazeException;
import org.izumi.haze.modules.impl.java.parsing.AnnotationsParsing;
import org.izumi.haze.modules.impl.java.parsing.ClassesParsing;
import org.izumi.haze.modules.impl.java.parsing.ScopesParsing;
import org.izumi.haze.modules.impl.java.util.Classes;
import org.izumi.haze.modules.impl.java.util.Comments;
import org.izumi.haze.modules.impl.java.util.Elements;
import org.izumi.haze.modules.impl.java.util.Scopes;
import org.izumi.haze.modules.impl.java.util.impl.ClassesImpl;
import org.izumi.haze.modules.impl.java.util.impl.CommentsImpl;
import org.izumi.haze.modules.impl.java.util.impl.ElementsImpl;
import org.izumi.haze.string.HazeString;
import org.izumi.haze.string.HazeStringBuilder;
import org.izumi.haze.string.LemmaString;
import org.izumi.haze.string.Regex;
import org.izumi.haze.string.HazeRegexString;
import org.izumi.haze.util.ExtendedList;
import org.izumi.haze.util.RangeMap;
import org.izumi.haze.modules.impl.java.util.impl.ScopesImpl;
import org.izumi.haze.util.Range;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.function.BiPredicate;

public class Class implements Element {
    private final HazeRegexString value;

    private long declarationOrder;

    private final Classes classes = new ClassesImpl();
    private final Scopes scopes = new ScopesImpl();
    private final Comments comments = new CommentsImpl();
    private final Collection<Method> methods = new LinkedList<>();
    private final Collection<Annotation> annotations = new LinkedList<>();
    private final Collection<Variable> variables = new LinkedList<>();
    private final Comments unresolved = new CommentsImpl();

    private final Collection<Keyword> keywords = new LinkedList<>();
    private String name;
    private String otherSignature;

    public Class(CharSequence sequence) {
        this.value = new HazeRegexString(sequence);
    }

    @Override
    public void renameClassAndUsages(String name, String replacement) {
        otherSignature = otherSignature.replace(name, replacement);

        classes.renameClassAndUsages(name, replacement);
        scopes.renameClassAndUsages(name, replacement);
        comments.renameClassAndUsages(name, replacement);
        unresolved.renameClassAndUsages(name, replacement);

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

    public void parse() {
        annotations.addAll(parseAnnotations());
        String signature = getSignature().toString();
        parseOtherSignature(signature);
        if (!otherSignature.isEmpty()) {
            signature = signature.replace(otherSignature, "");
        }

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

        Range searchIn = getBodyRange();

        SortedMap<Range, Class> classesMap = new ClassesParsing(value.getSub(searchIn)).parse();
        classes.addAll(classesMap.values());
        SortedMap<Range, Scope> scopesMap = new ScopesParsing(value.getSub(searchIn)).parse();
        scopes.addAll(scopesMap.values());

        RangeMap<Element> elementsMap = new RangeMap<>();
        elementsMap.putAll(classesMap, scopesMap);

        markUnresolved(searchIn, elementsMap);

        setUpOrderNumbers(elementsMap);
        parseRecursive();
    }

    @Override
    public String toString() {
        Elements<?> ordered = new ElementsImpl(classes, scopes, comments,
                unresolved, methods, variables).getOrderedCopy();
        return new HazeStringBuilder()
                .appendEachSeparateSpace(annotations)
                .append(formSignature())
                .appendSpace()
                .append("{")
                .appendEachSeparateSpace(ordered)
                .appendSpace()
                .append("}")
                .build();
    }

    private void markUnresolved(Range searchIn, RangeMap<Element> elementsMap) {
        for (Range unusedRange : elementsMap.getUnusedRanges(searchIn)) {
            HazeString string = value.getSub(unusedRange).trim();
            if (string.equalsAny(new ExtendedList<>("", "\n", "\r"))) {
                continue;
            }

            Comment comment = new Comment(string);
            unresolved.add(comment);
            elementsMap.put(unusedRange, comment);
        }
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

    private HazeString getSignature() {
        LemmaString withAnnotations = new LemmaString(
                value.getSub(new Range(0, value.firstRangeOf("{").get().start - 1)));
        if (withAnnotations.doesNotContain("@")) {
            return withAnnotations.trim();
        }

        return excludeAnnotations(withAnnotations);
    }

    private HazeString excludeAnnotations(LemmaString withAnnotations) {
        Range annotationsRange = getAnnotationsRange(withAnnotations);
        Range range = withAnnotations.getRange().get();
        if (annotationsRange.end == range.end) {
            return new HazeString("");
        }

        return withAnnotations
                .getSub(new Range(annotationsRange.end + 1, range.end))
                .trim();
    }

    private Collection<Annotation> parseAnnotations() {
        LemmaString withAnnotations = new LemmaString(
                value.getSub(new Range(0, value.firstRangeOf("{").get().start - 1)));
        if (withAnnotations.doesNotContain("@")) {
            return new ExtendedList<>();
        }

        Range annotationsRange = getAnnotationsRange(withAnnotations);
        LemmaString annotations = withAnnotations.getSub(annotationsRange);

        return new AnnotationsParsing(annotations).parse();
    }

    //@SuppressWarnings(value = \"\") @NoArgsConstructor @AllArgsConstructor @RequiredArgsConstructor public
    //@RequiredArgsConstructor(value = "1")\npublic class Regex //TODO: in given case it will be broken
    private Range getAnnotationsRange(LemmaString withAnnotations) {
        Optional<Range> optionalLemmaRange = withAnnotations.getFirstLemmaRange();
        Range annotationsRange = new Range(0, optionalLemmaRange.get().end);
        while (optionalLemmaRange.isPresent()) {
            Range range = optionalLemmaRange.get();
            LemmaString lemma = withAnnotations.getSub(range);
            if (lemma.doesNotContain("@")) {
                break;
            }

            annotationsRange = new Range(annotationsRange.start, range.end);
            optionalLemmaRange = withAnnotations.getLemmaRangeAfter(annotationsRange);
        }

        return annotationsRange;
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
        scopes.forEach(Scope::parse);
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
                value.firstRangeOf("{").get().start + 1,
                value.lastRangeOf("}").get().start - 1);
    }

    private void parseOtherSignature(String signature) {
        HazeRegexString regexString = new HazeRegexString(signature);
        Optional<Range> additionalInfo = regexString.firstRangeOfRegex(new Regex("<.+> implements.*"));
        if (additionalInfo.isEmpty()) {
            additionalInfo = regexString.firstRangeOfRegex(new Regex("<.+> extends.*"));
            if (additionalInfo.isEmpty()) {
                additionalInfo = regexString.firstRangeOfRegex(new Regex("<.+>"));
                if (additionalInfo.isEmpty()) {
                    additionalInfo = regexString.firstRangeOfRegex(new Regex(" implements.*"));
                    if (additionalInfo.isEmpty()) {
                        additionalInfo = regexString.firstRangeOfRegex(new Regex(" extends.*"));
                    }
                }
            }
        }

        if (additionalInfo.isPresent()) {
            otherSignature = regexString.getSub(additionalInfo.get()).toString();
        } else {
            otherSignature = "";
        }
    }
}
