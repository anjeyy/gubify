package io.github.anjeyy.api.gepository.dto;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

    private final HttpStatus httpStatus;
    private final String message;
    private final String hint;

    public ErrorResponse(HttpStatus httpStatus, String message, String hint) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.hint = hint;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public String getHint() {
        return hint;
    }

    public static class ErrorResponseBuilder {

        private HttpStatus httpStatus;
        private String message;
        private String hint;

        public ErrorResponseBuilder httpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public ErrorResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ErrorResponseBuilder hint(String hint) {
            this.hint = hint;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(httpStatus, message, hint);
        }
    }
}
