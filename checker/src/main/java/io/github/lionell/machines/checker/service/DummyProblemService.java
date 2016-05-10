package io.github.lionell.machines.checker.service;

import io.github.lionell.machines.checker.model.Problem;
import io.github.lionell.machines.checker.repository.ProblemRepository;
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
public class DummyProblemService implements ProblemService {
    private final ProblemRepository repository;

    @Autowired
    public DummyProblemService(ProblemRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Problem> getAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Problem get(long id) {
        return repository.findOne(id);
    }
}
