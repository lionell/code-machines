package io.github.lionell.machines.checker.service;

import io.github.lionell.machines.checker.domain.checkers.StringMachineChecker;
import io.github.lionell.machines.checker.domain.interpreters.StringMachine;
import io.github.lionell.machines.checker.domain.interpreters.markov.Markov;
import io.github.lionell.machines.checker.domain.interpreters.utm.UTM;
import io.github.lionell.machines.checker.model.Problem;
import io.github.lionell.machines.checker.model.Submission;
import io.github.lionell.machines.checker.model.SubmissionStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lionell on 5/9/16.
 *
 * @author Ruslan Sakevych
 */

public class Checker implements Runnable {
    private SubmissionService submissionService;
    private ProblemService problemService;
    private Submission submission;

    public Checker(SubmissionService submissionService,
                   ProblemService problemService,
                   Submission submission) {
        this.submissionService = submissionService;
        this.problemService = problemService;
        this.submission = submission;
    }

    @Override
    public void run() {
        StringMachineChecker checker = new StringMachineChecker();

        Problem problem = problemService.get(submission.getProblem());

        checker.loadTest(problem.getTests());

        String solution = submission.getSolution();

        Pattern pattern = Pattern.compile("\\.([^.]*)$");
        Matcher matcher = pattern.matcher(solution);

        String ext = "";

        if (matcher.find()) {
            ext = matcher.group(1);
        }

        StringMachine machine;

        switch (ext.toLowerCase()) {
            case "ma":
                machine = Markov.Parser.parse(solution);
                break;
            case "utm":
                machine = UTM.Parser.parse(solution);
                break;
            default:
                machine = null;
        }

        checker.runTests(machine);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        switch (checker.getVerdict()) {
            case OK:
                submission.setStatus(SubmissionStatus.OK);
                break;
            case WRONG_ANSWER:
                submission.setStatus(SubmissionStatus.WA);
                break;
            case RUNTIME_ERROR:
                submission.setStatus(SubmissionStatus.RE);
                break;
            default:
                submission.setStatus(SubmissionStatus.TL);
        }

        submissionService.update(submission);
    }
}
