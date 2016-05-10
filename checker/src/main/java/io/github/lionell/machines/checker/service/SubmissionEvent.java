package io.github.lionell.machines.checker.service;

import io.github.lionell.machines.checker.model.Submission;
import org.springframework.context.ApplicationEvent;

/**
 * Created by lionell on 5/9/16.
 *
 * @author Ruslan Sakevych
 */
public class SubmissionEvent extends ApplicationEvent {
    private Submission submission;

    public SubmissionEvent(Object source, Submission submission) {
        super(source);

        this.submission = submission;
    }

    public Submission getSubmission() {
        return submission;
    }
}
