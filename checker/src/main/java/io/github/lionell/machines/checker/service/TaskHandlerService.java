package io.github.lionell.machines.checker.service;

import io.github.lionell.machines.checker.model.Submission;
import io.github.lionell.machines.checker.model.SubmissionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lionell on 4/12/16.
 *
 * @author Ruslan Sakevych
 */

@Service
public class TaskHandlerService implements ApplicationListener<SubmissionEvent> {

    private SubmissionService submissionService;
    private ProblemService problemService;
    private ExecutorService threadPool = Executors.newFixedThreadPool(5);

    @Autowired
    public TaskHandlerService(SubmissionService submissionService,
                              ProblemService problemService) {
        this.submissionService = submissionService;
        this.problemService = problemService;
    }

    @Override
    public void onApplicationEvent(SubmissionEvent submissionEvent) {
        Submission submission = submissionEvent.getSubmission();

        submission.setStatus(SubmissionStatus.CHECKING);
        submissionService.update(submission);

        Checker checker = new Checker(submissionService, problemService,
                submission);
        threadPool.execute(checker);
    }
}
