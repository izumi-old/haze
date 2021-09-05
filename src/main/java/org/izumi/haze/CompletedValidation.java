package org.izumi.haze;

import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class CompletedValidation {
    public final String message;
    public final Result result;

    public CompletedValidation(Result result) {
        this("", result);
    }

    public void ifGoodOtherwise(Runnable ifGood, Consumer<String> ifBad) {
        if (result == Result.GOOD) {
            ifGood.run();
        } else {
            ifBad.accept(message);
        }
    }

    enum Result {
        GOOD, BAD
    }
}
