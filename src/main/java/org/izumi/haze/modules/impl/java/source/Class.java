package org.izumi.haze.modules.impl.java.source;

import org.izumi.haze.HazeException;
import org.izumi.haze.modules.impl.java.parsing.ClassesParsing;
import org.izumi.haze.modules.impl.java.parsing.ScopesParsing;
import org.izumi.haze.modules.impl.java.util.Classes;
import org.izumi.haze.modules.impl.java.util.Comments;
import org.izumi.haze.modules.impl.java.util.Elements;
import org.izumi.haze.modules.impl.java.util.Scopes;
import org.izumi.haze.modules.impl.java.util.impl.ClassesImpl;
import org.izumi.haze.modules.impl.java.util.impl.CommentsImpl;
import org.izumi.haze.modules.impl.java.util.impl.ElementsImpl;
import org.izumi.haze.string.HazeStringBuilder;
import org.izumi.haze.string.Regex;
import org.izumi.haze.string.HazeRegexString;
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
        //@RequiredArgsConstructor\npublic class Regex
        String signature = getSignature();
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

        for (Range unusedRange : elementsMap.getUnusedRanges(searchIn)) {
            Comment comment = new Comment(value.getSub(unusedRange));
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
        return value.getSub(new Range(0, value.firstRangeOf("{").get().start))
                .toString()
                .trim();
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
        classes.forEach(Class::parse);
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
