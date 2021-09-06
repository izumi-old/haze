package org.izumi.haze.validation;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InputValidation implements Validation {
    private final String input;

    @Override
    public CompletedValidation validate() {
        String message;
        CompletedValidation.Result result;
        if (input == null) {
            message = "One of the given wishes is null";
            result = CompletedValidation.Result.BAD;
        } else if (input.isBlank()) {
            message = "One of the given wishes is blank";
            result = CompletedValidation.Result.BAD;
        } else {
            return new CompletedValidation(CompletedValidation.Result.GOOD);
        }

        return new CompletedValidation(message, result);
    }
}
