package ru.itmo.cats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("ru.itmo")
@ComponentScan("ru.itmo")
@EnableJpaRepositories("ru.itmo.data")
public class Main {
    public static void main (String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
