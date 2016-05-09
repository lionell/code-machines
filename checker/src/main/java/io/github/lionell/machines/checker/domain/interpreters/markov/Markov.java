package io.github.lionell.machines.checker.domain.interpreters.markov;

import io.github.lionell.machines.checker.domain.interpreters.StringMachine;
import io.github.lionell.machines.checker.domain.interpreters.markov.exceptions.ParserException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by lionell on 3/22/16.
 *
 * @author Ruslan Sakevych
 */
public class Markov implements StringMachine {
    private static final int THRESHOLD = 10000;
    private char blankSymbol;
    private List<Rule> rules;
    private String inputString;

    public Markov(char blankSymbol, List<Rule> rules) {
        this.blankSymbol = blankSymbol;
        this.rules = rules;
    }

    @Override
    public String call() {
        Rule curRule = findNextRule();
        int stepsCount = 0;
        while (curRule != null && !curRule.isTerminal() && stepsCount < THRESHOLD) {
            inputString = inputString.replaceFirst(curRule.getFrom(), curRule.getTo());
            curRule = findNextRule();
            stepsCount++;
        }
        return stepsCount < THRESHOLD
                ? inputString
                : null;
    }

    @Override
    public Markov init(String input) {
        inputString = input;
        return this;
    }

    @Override
    public StringMachine copy() {
        return new Markov(blankSymbol, rules);
    }

    private Rule findNextRule() {
        for (Rule r : rules) {
            if (inputString.contains(r.getFrom())) {
                return r;
            }
        }
        return null;
    }

    public static class Rule {
        private String from;
        private String to;
        private boolean terminal;

        public Rule(String from, String to, boolean terminal) {
            this.from = from;
            this.to = to;
            this.terminal = terminal;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public boolean isTerminal() {
            return terminal;
        }
    }

    public static class Parser {
        private static final Pattern BLANK_SYMBOL_REGEX
                = Pattern.compile("^\\s*BLANK_SYMBOL\\s*=\\s*(.)\\s*$");
        private static final Pattern RULE_REGEX
                = Pattern.compile("^\\s*([^#\\s]+)\\s*->\\s*(\\.)?\\s*([^\\s]+)\\s*$");

        public static Markov parse(String path) {
            List<String> lines;
            try {
                lines = Files.readAllLines(Paths.get(path));
            } catch (IOException e) {
                throw new ParserException("Can't open source file!");
            }
            char blankSymbol = parseBlankSymbol(lines);
            List<Rule> rules = parseRules(lines);

            return new Markov(blankSymbol, rules);
        }

        private static String parseByRegex(List<String> lines, Pattern pattern) {
            Optional<Matcher> m = lines.stream().map(pattern::matcher).filter(Matcher::matches).findFirst();
            return m.isPresent()
                    ? m.get().group(1)
                    : "";
        }

        private static char parseBlankSymbol(List<String> lines) {
            String blankSymbol = parseByRegex(lines, BLANK_SYMBOL_REGEX);
            if (blankSymbol.isEmpty()) {
                throw new ParserException("Blank symbol definition expected!");
            }
            return blankSymbol.charAt(0);
        }

        private static List<Rule> parseRules(List<String> lines) {
            return lines
                    .stream()
                    .map(RULE_REGEX::matcher)
                    .filter(Matcher::matches)
                    .map(m -> new Rule(m.group(1), m.group(3), ".".equals(m.group(2))))
                    .collect(Collectors.toList());
        }


    }
}
