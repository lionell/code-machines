package io.github.lionell.machines.checker.domain.exceptions;

/**
 * Created by lionell on 3/21/16.
 *
 * @author Ruslan Sakevych
 */
public class CheckerException extends RuntimeException {
    public CheckerException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "[StringMachineChecker]" + super.getMessage();
    }
}
