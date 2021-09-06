package org.izumi.haze;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class CompletedValidation {

    @NonNull
    public final String message;

    @NonNull
    public final Result result;

    public CompletedValidation(@NonNull Result result) {
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
