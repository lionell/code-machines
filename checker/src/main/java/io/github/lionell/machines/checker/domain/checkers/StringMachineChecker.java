package io.github.lionell.machines.checker.domain.checkers;

import io.github.lionell.machines.checker.domain.exceptions.CheckerException;
import io.github.lionell.machines.checker.domain.interpreters.StringMachine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by lionell on 3/21/16.
 *
 * @author Ruslan Sakevych
 */
public class StringMachineChecker {
    private static final Pattern TEST_REGEX
            = Pattern.compile("^(.*)->(.*)$");
    private List<Test> tests
            = new ArrayList<>();
    private List<Verdict> verdicts;

    public void loadTest(String path) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            throw new CheckerException("Can't load file with tests!");
        }
        lines
                .stream()
                .map(TEST_REGEX::matcher)
                .filter(Matcher::matches)
                .map(m -> new Test(m.group(1).trim(), m.group(2).trim()))
                .forEach(tests::add);
    }

    public void loadTestsRecursively(String path) {
        File directory = new File(path);
        File[] subFiles = directory.listFiles();
        if (subFiles == null) {
            return;
        }
        for (File file : subFiles) {
            if (file.isFile()) {
                loadTest(file.getPath());
            } else if (file.isDirectory()) {
                loadTestsRecursively(file.getPath());
            }
        }
    }

    public void runTestsParallel(StringMachine machine) {
        ExecutorService executor
                = Executors.newFixedThreadPool(Runtime.getRuntime()
                .availableProcessors());
        List<Future<String>> futures
                = setUp(machine)
                .stream()
                .map(executor::submit)
                .collect(Collectors.toList());

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new CheckerException("Time limit exceeded while checking!");
        }
        List<String> outputs = new ArrayList<>();
        for (Future<String> f : futures) {
            try {
                outputs.add(f.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new CheckerException("Time limit exceeded while checking!");
            }
        }
        checkTests(outputs);
    }

    public void runTests(StringMachine machine) {
        checkTests(setUp(machine)
                .stream()
                .map(StringMachine::call)
                .collect(Collectors.toList()));
    }

    private List<StringMachine> setUp(StringMachine machine) {
        return tests
                .stream()
                .map(t -> machine.copy().init(t.getInput()))
                .collect(Collectors.toList());
    }

    private void checkTests(List<String> outputs) {
        IntFunction<Verdict> getVerdict = i -> {
            if (outputs.get(i) == null) {
                return Verdict.RUNTIME_ERROR;
            }
            return tests.get(i).getOutput().equals(outputs.get(i))
                    ? Verdict.OK
                    : Verdict.WRONG_ANSWER;
        };
        verdicts = IntStream
                .range(0, tests.size())
                .mapToObj(getVerdict)
                .collect(Collectors.toList());
    }

    public List<Verdict> getVerdicts() {
        return verdicts;
    }

    private static class Test {
        private String input;
        private String output;

        private Test(String input, String output) {
            this.input = input;
            this.output = output;
        }

        private String getInput() {
            return input;
        }

        private String getOutput() {
            return output;
        }
    }

    public enum Verdict {
        OK,
        WRONG_ANSWER,
        RUNTIME_ERROR,
    }
}
