package io.github.lionell.machines.checker.service;

import io.github.lionell.machines.checker.model.Submission;
import io.github.lionell.machines.checker.model.SubmissionStatus;
import io.github.lionell.machines.checker.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lionell on 4/12/16.
 *
 * @author Ruslan Sakevych
 */

@Service
public class DummySubmissionService implements SubmissionService,
        ApplicationEventPublisherAware {

    private final SubmissionRepository repository;
    private ApplicationEventPublisher publisher;

    @Autowired
    public DummySubmissionService(SubmissionRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Submission> getAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Submission get(long id) {
        return repository.findOne(id);
    }

    @Transactional
    public Submission create(Submission submission) {
        submission.setStatus(SubmissionStatus.WAITING);

        publisher.publishEvent(new SubmissionEvent(this, submission));

        return repository.save(submission);
    }

    @Transactional
    public Submission update(Submission submission) {
        return repository.save(submission);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }
}
