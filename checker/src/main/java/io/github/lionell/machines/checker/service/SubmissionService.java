package io.github.lionell.machines.checker.service;

import io.github.lionell.machines.checker.model.Submission;

import java.util.List;

/**
 * Created by lionell on 4/12/16.
 *
 * @author Ruslan Sakevych
 */
public interface SubmissionService {
    List<Submission> getAll();

    Submission get(long id);

    Submission create(Submission submission);
}