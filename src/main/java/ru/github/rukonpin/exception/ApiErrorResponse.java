package ru.github.rukonpin.exception;

public class ApiErrorResponse {

    private final String message;

    private ApiErrorResponse(String message) {
        this.message = message;
    }

    public static ApiErrorResponse of(String message) {
        return new ApiErrorResponse(message);
    }

    public String getMessage() {
        return message;
    }
}
