package io.github.lionell.machines.checker.domain;

import io.github.lionell.machines.checker.service.SubmissionQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lionell on 4/12/16.
 *
 * @author Ruslan Sakevych
 */

@Component("handler")
public class TaskHandler extends Thread {
    private final Logger LOGGER = LoggerFactory.getLogger(TaskHandler.class);

    @Autowired
    private SubmissionQueue queue;

    @Override
    public void run() {
        try {
            queue.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
