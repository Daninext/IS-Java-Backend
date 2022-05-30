package ru.itmo.kotiki.ownersservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import ru.itmo.kotiki.servicedata.configs.RabbitConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EntityScan("ru.itmo.kotiki.servicedata")
@ComponentScan("ru.itmo.kotiki")
@EnableJpaRepositories("ru.itmo.kotiki.servicedata")
@Import(RabbitConfiguration.class)
public class OwnersServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OwnersServiceApplication.class, args);
    }

}
