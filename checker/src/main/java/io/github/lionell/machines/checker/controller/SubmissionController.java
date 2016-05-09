package io.github.lionell.machines.checker.controller;

import io.github.lionell.machines.checker.model.Submission;
import io.github.lionell.machines.checker.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lionell on 4/12/16.
 *
 * @author Ruslan Sakevych
 */

@RestController
@RequestMapping("/submission")
public class SubmissionController {
    private final SubmissionService submissionService;

    @Autowired
    public SubmissionController(final SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Submission> get() {
        return submissionService.getAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public Submission get(@PathVariable String id) {
        return submissionService.get(Long.parseLong(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public Submission create(@RequestBody Submission submission) {
        return submissionService.create(submission);
    }
}
