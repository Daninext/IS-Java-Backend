package ru.itmo.tests;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import ru.itmo.data.entity.Owner;
import ru.itmo.data.entity.RoleType;
import ru.itmo.data.entity.User;

import java.util.Arrays;
import java.util.Date;

@TestConfiguration
public class SpringSecurityWebAuxTestConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        User adminUser = new User("admin", "root", RoleType.ADMIN, null);

        User basicUser = new User("art3m", "123", RoleType.USER, new Owner(1, "Artem", new Date()));

        return new InMemoryUserDetailsManager(Arrays.asList(adminUser, basicUser));
    }
}
