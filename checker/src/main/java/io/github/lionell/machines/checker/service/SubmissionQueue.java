package io.github.lionell.machines.checker.service;

import io.github.lionell.machines.checker.model.Submission;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by lionell on 4/14/16.
 *
 * @author Ruslan Sakevych
 */

@Service
public class SubmissionQueue {
    private final BlockingQueue<Submission> queue = new LinkedBlockingQueue<>();

    public SubmissionQueue() {
    }

    public void add(Submission s) {
        queue.add(s);
    }

    public Submission get() throws InterruptedException {
        return queue.take();
    }
}
