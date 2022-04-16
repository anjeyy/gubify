package io.github.anjeyy.infrastructure.exception;

public class GithubRestException extends RuntimeException {

    public GithubRestException(String message) {
        super(message);
    }

    public GithubRestException(String message, Throwable cause) {
        super(message, cause);
    }
}
