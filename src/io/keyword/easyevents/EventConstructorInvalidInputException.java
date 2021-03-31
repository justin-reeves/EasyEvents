package io.keyword.easyevents;

public class EventConstructorInvalidInputException extends RuntimeException {

    public EventConstructorInvalidInputException() {
    }

    public EventConstructorInvalidInputException(String message) {
        super(message);
    }

    public EventConstructorInvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventConstructorInvalidInputException(Throwable cause) {
        super(cause);
    }
}
