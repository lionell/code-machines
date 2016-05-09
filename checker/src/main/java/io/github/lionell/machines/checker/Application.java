package io.github.lionell.machines.checker;

import io.github.lionell.machines.checker.domain.TaskHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by lionell on 4/12/16.
 *
 * @author Ruslan Sakevych
 */

@SpringBootApplication
@ImportResource("spring.xml")
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        TaskHandler handler = (TaskHandler) ctx.getBean("handler");
        handler.run();
    }
}
