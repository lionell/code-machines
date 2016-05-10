package io.github.lionell.machines.checker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
        SpringApplication.run(Application.class, args);
    }
}
