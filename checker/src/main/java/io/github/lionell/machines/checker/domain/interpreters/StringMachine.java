package io.github.lionell.machines.checker.domain.interpreters;

import java.util.concurrent.Callable;

/**
 * Created by lionell on 3/22/16.
 *
 * @author Ruslan Sakevych
 */
public interface StringMachine extends Callable<String> {
    StringMachine init(String input);

    StringMachine copy();

    @Override
    String call();
}
