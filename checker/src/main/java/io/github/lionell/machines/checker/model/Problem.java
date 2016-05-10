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
 * Created by lionell on 5/10/16.
 *
 * @author Ruslan Sakevych
 */

@Entity
@Table(name = "problems")
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String tests;

    @NotNull
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProblemDifficulty difficulty;

    public Problem() {
    }

    public Problem(String tests, String name, ProblemDifficulty difficulty) {
        this.tests = tests;
        this.name = name;
        this.difficulty = difficulty;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTests() {
        return tests;
    }

    public void setTests(String tests) {
        this.tests = tests;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProblemDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(ProblemDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "id=" + id +
                ", tests='" + tests + '\'' +
                ", name='" + name + '\'' +
                ", difficulty=" + difficulty +
                '}';
    }
}

