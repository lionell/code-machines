package io.github.lionell.machines.checker.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created by lionell on 4/12/16.
 *
 * @author Ruslan Sakevych
 */

@Entity
@Table(name = "submissions")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String solution;

    @NotNull
    private String problem;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;

    public Submission() {
    }

    public Submission(String solution, String problem, SubmissionStatus status) {
        this.solution = solution;
        this.problem = problem;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Submission{" +
                "id=" + id +
                ", solution='" + solution + '\'' +
                ", problem='" + problem + '\'' +
                '}';
    }
}
