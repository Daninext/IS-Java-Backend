package ru.itmo.services.serv;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.itmo.data.dao.OwnerDAO;
import ru.itmo.data.entity.Cat;
import ru.itmo.data.entity.Owner;

import java.util.Collections;
import java.util.List;

public class OwnerService implements OwnerDAO {
    private SessionFactory sessionFactory;

    public OwnerService() {
        sessionFactory = new Configuration()
                .addAnnotatedClass(Cat.class)
                .addAnnotatedClass(Owner.class)
                .buildSessionFactory();
    }

    @Override
    public void add(Owner owner) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        session.save(owner);

        session.getTransaction().commit();
    }

    @Override
    public Owner getById(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        Owner result = session.get(Owner.class, id);

        session.getTransaction().commit();

        return result;
    }

    @Override
    public List<Owner> getAll() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        List<Owner> result = session.createQuery("select a from Owner a", Owner.class).getResultList();

        session.getTransaction().commit();

        return Collections.unmodifiableList(result);
    }

    @Override
    public void remove(Owner owner) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        session.remove(owner);

        session.getTransaction().commit();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
