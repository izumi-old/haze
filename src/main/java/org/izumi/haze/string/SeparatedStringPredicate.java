package org.izumi.haze.string;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

@Component
public class SeparatedStringPredicate implements Predicate<SeparatedString> {
    private final Collection<Character> separated = List.of(' ', '.', '<', '>', '(', ';');

    @Override
    public boolean test(SeparatedString separatedString) {
        return separated.contains(separatedString.before) && separated.contains(separatedString.after);
    }
}
