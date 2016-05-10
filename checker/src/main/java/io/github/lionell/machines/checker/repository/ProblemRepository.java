package io.github.lionell.machines.checker.repository;

import io.github.lionell.machines.checker.model.Problem;
import io.github.lionell.machines.checker.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lionell on 4/13/16.
 *
 * @author Ruslan Sakevych
 */

public interface ProblemRepository extends JpaRepository<Problem, Long> {
}
