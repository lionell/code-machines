package io.github.lionell.machines.checker.domain.utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Random;

/**
 * Created by lionell on 3/22/16.
 *
 * @author Ruslan Sakevych
 */
public class TestGenerator {
    private static final int TESTS_COUNT = 100;
    private static final int UPPER_BOUND = 1000;
    private static final Random random = new Random();
    private static final Generator NONE_GENERATOR = () -> {
        int a = random.nextInt(UPPER_BOUND) + UPPER_BOUND;
        return repeat('1', a) + "->" + repeat('1', a);
    };
    private static final Generator ONE_TO_NINE_GENERATOR = () -> {
        int a = random.nextInt(UPPER_BOUND) + UPPER_BOUND;
        return repeat('1', a) + "->" + repeat('9', a);
    };

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        TestGenerator.genTests(ONE_TO_NINE_GENERATOR, "res/tests/one_to_nine_" + TESTS_COUNT + "x" + UPPER_BOUND + ".txt");
        long finish = System.currentTimeMillis();
        System.out.println("Generated in: " + ((finish - start) / 1000.) + "s.");
    }

    private static void genTests(Generator generator, String path) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(path), "utf-8"))) {
            for (int i = 0; i < TESTS_COUNT; ++i) {
                writer.write(generator.next() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String repeat(char c, int n) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; ++i) {
            builder.append(c);
        }
        return builder.toString();
    }

    private interface Generator {
        String next();
    }
}
