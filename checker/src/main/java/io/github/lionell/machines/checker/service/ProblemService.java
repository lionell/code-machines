package io.github.lionell.machines.checker.service;

import io.github.lionell.machines.checker.model.Problem;

import java.util.List;

/**
 * Created by lionell on 4/12/16.
 *
 * @author Ruslan Sakevych
 */
public interface ProblemService {
    List<Problem> getAll();

    Problem get(long id);
}