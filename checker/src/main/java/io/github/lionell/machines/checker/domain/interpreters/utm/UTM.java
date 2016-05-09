package io.github.lionell.machines.checker.domain.interpreters.utm;

import io.github.lionell.machines.checker.domain.interpreters.StringMachine;
import io.github.lionell.machines.checker.domain.interpreters.utm.exceptions.ParserException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by lionell on 3/15/16.
 *
 * @author Ruslan Sakevych
 */
public class UTM implements StringMachine {
    private static final int THRESHOLD = 10000;
    private final char blankSymbol;
    private final String initialState;
    private final Set<String> terminalStates;
    private List<Character> tape;
    private Map<StateTapeSymbolPair, Transition> transitions = new HashMap<>();

    public UTM(char blankSymbol, String initialState, Set<String> terminalStates, Set<Transition> transitions) {
        this.blankSymbol = blankSymbol;
        this.initialState = initialState;
        this.terminalStates = terminalStates;
        for (Transition t : transitions) {
            this.transitions.put(t.from, t);
        }
    }

    public UTM(char blankSymbol, String initialState, Set<String> terminalStates,
               Map<StateTapeSymbolPair, Transition> transitions) {
        this.blankSymbol = blankSymbol;
        this.initialState = initialState;
        this.terminalStates = terminalStates;
        this.transitions = transitions;
    }

    @Override
    public String call() {
        if (tape.isEmpty()) {
            tape.add(blankSymbol);
        }
        ListIterator<Character> tapeIterator = tape.listIterator();
        tapeIterator.next();
        tapeIterator.previous();

        StateTapeSymbolPair curST = new StateTapeSymbolPair(initialState, tape.get(0));
        int stepsCount = 0;
        while (transitions.containsKey(curST)
                && !terminalStates.contains(curST.getState())
                && stepsCount < THRESHOLD) {
            Transition t = transitions.get(curST);
            tapeIterator.set(t.getTo().getTapeSymbol());
            curST.setState(t.getTo().getState());
            switch (t.getDirection()) {
                case LEFT:
                    if (!tapeIterator.hasPrevious()) {
                        tapeIterator.add(blankSymbol);
                    }
                    curST.setTapeSymbol(tapeIterator.previous());
                    break;
                case RIGHT:
                    tapeIterator.next();
                    if (!tapeIterator.hasNext()) {
                        tapeIterator.add(blankSymbol);
                        tapeIterator.previous();
                    }
                    curST.setTapeSymbol(tapeIterator.next());
                    tapeIterator.previous();
                    break;
                case NONE:
                    curST.setTapeSymbol(t.getTo().getTapeSymbol());
                    break;
            }
            stepsCount++;
        }

        return stepsCount < THRESHOLD ? formatOutput() : null;
    }

    private String formatOutput() {
        StringBuilder builder = new StringBuilder();
        tape
                .stream()
                .filter(c -> c != blankSymbol)
                .forEachOrdered(builder::append);
        return builder.toString();
    }

    @Override
    public UTM copy() {
        return new UTM(blankSymbol, initialState, terminalStates, transitions);
    }

    @Override
    public UTM init(String input) {
        tape = new LinkedList<>();
        for (char c : input.toCharArray()) {
            tape.add(c);
        }
        return this;
    }

    public static class StateTapeSymbolPair {
        private String state;
        private char tapeSymbol;

        public StateTapeSymbolPair(String state, char tapeSymbol) {
            this.state = state;
            this.tapeSymbol = tapeSymbol;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public char getTapeSymbol() {
            return tapeSymbol;
        }

        public void setTapeSymbol(char tapeSymbol) {
            this.tapeSymbol = tapeSymbol;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StateTapeSymbolPair that = (StateTapeSymbolPair) o;

            return tapeSymbol == that.tapeSymbol && (state != null ? state.equals(that.state) : that.state == null);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = state != null ? state.hashCode() : 0;
            result = prime * result + (int) tapeSymbol;
            return result;
        }
    }

    public static class Transition {
        private StateTapeSymbolPair from;
        private StateTapeSymbolPair to;
        private Direction direction;

        public Transition(StateTapeSymbolPair from, StateTapeSymbolPair to, Direction direction) {
            this.from = from;
            this.to = to;
            this.direction = direction;
        }

        public StateTapeSymbolPair getFrom() {
            return from;
        }

        public StateTapeSymbolPair getTo() {
            return to;
        }

        public Direction getDirection() {
            return direction;
        }
    }

    public enum Direction {
        LEFT,
        RIGHT,
        NONE,
    }

    public static class Parser {
        private static final Pattern BLANK_SYMBOL_REGEX
                = Pattern.compile("^\\s*BLANK_SYMBOL\\s*=\\s*(.)\\s*$");
        private static final Pattern INITIAL_STATE_REGEX
                = Pattern.compile("^\\s*INITIAL_STATE\\s*=\\s*(\\w+)\\s*$");
        private static final Pattern TERMINAL_STATES_REGEX
                = Pattern.compile("^\\s*TERMINAL_STATES\\s*=\\s*\\{([\\w,\\s]+)\\}\\s*$");
        private static final Pattern TRANSITION_REGEX
                = Pattern.compile("^\\s*(\\w+)\\s*:\\s*(.)\\s*->\\s*(\\w+)\\s*:\\s*(.)\\s*,\\s*(L|R|N)\\s*$");

        public static UTM parse(String path) {
            List<String> lines;
            try {
                lines = Files.readAllLines(Paths.get(path));
            } catch (IOException e) {
                throw new ParserException("Can't open source file!");
            }
            char blankSymbol = parseBlankSymbol(lines);
            String initialState = parseInitialState(lines);
            Set<String> terminalStates = parseTerminalStates(lines);
            Set<Transition> transitions = parseTransitions(lines);

            return new UTM(blankSymbol, initialState, terminalStates, transitions);
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

        private static String parseInitialState(List<String> lines) {
            String initialState = parseByRegex(lines, INITIAL_STATE_REGEX);
            if (initialState.isEmpty()) {
                throw new ParserException("Initial state definition expected!");
            }
            return initialState;
        }

        private static Set<String> parseTerminalStates(List<String> lines) {
            String states = parseByRegex(lines, TERMINAL_STATES_REGEX);
            if (states.isEmpty()) {
                throw new ParserException("Terminal states definition expected!");
            }
            Set<String> terminalStates = new HashSet<>();
            for (String state : states.split(",")) {
                terminalStates.add(state.trim());
            }
            if (terminalStates.isEmpty()) {
                throw new ParserException("At least one terminal state expected!");
            }
            return terminalStates;
        }

        private static Set<Transition> parseTransitions(List<String> lines) {
            Set<Transition> transitions = new HashSet<>();
            for (String l : lines) {
                Matcher m = TRANSITION_REGEX.matcher(l);
                if (m.matches()) {
                    StateTapeSymbolPair from = new StateTapeSymbolPair(m.group(1), m.group(2).charAt(0));
                    StateTapeSymbolPair to = new StateTapeSymbolPair(m.group(3), m.group(4).charAt(0));
                    Direction direction;
                    switch (m.group(5)) {
                        case "L":
                            direction = Direction.LEFT;
                            break;
                        case "R":
                            direction = Direction.RIGHT;
                            break;
                        case "N":
                            direction = Direction.NONE;
                            break;
                        default:
                            throw new ParserException("Unknown direction!");
                    }
                    transitions.add(new Transition(from, to, direction));
                }
            }
            return transitions;
        }
    }
}
