package io.github.lionell.machines.checker.domain.interpreters.utm.exceptions;

/**
 * Created by lionell on 3/21/16.
 *
 * @author Ruslan Sakevych
 */
public class ParserException extends RuntimeException {
    public ParserException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "[Parser]" + super.getMessage();
    }
}
