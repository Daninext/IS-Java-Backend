package ru.itmo.services;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.itmo.data.entity.Cat;
import ru.itmo.data.entity.Owner;

public class Controller {
    private SessionFactory sessionFactory;

    public Controller() {
        sessionFactory = new Configuration()
                .addAnnotatedClass(Owner.class)
                .addAnnotatedClass(Cat.class)
                .buildSessionFactory();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void close() {
        sessionFactory.close();
    }
}
