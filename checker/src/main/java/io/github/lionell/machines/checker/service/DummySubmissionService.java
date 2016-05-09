package io.github.lionell.machines.checker.service;

import io.github.lionell.machines.checker.model.Submission;
import io.github.lionell.machines.checker.model.SubmissionStatus;
import io.github.lionell.machines.checker.repository.SubmissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lionell on 4/12/16.
 *
 * @author Ruslan Sakevych
 */

@Service
public class DummySubmissionService implements SubmissionService {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(DummySubmissionService.class);

    private final SubmissionQueue queue;

    private final SubmissionRepository repository;

    @Autowired
    public DummySubmissionService(SubmissionQueue queue,
                                  SubmissionRepository repository) {
        this.queue = queue;
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Submission> getAll() {
        LOGGER.debug("Get all");

        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Submission get(long id) {
        LOGGER.debug("Get {}", id);

        return repository.findOne(id);
    }

    @Transactional
    public Submission create(final Submission submission) {
        submission.setStatus(SubmissionStatus.WAITING);

        LOGGER.debug("Add {}", submission);
        queue.add(submission);

        return repository.save(submission);
    }
}
