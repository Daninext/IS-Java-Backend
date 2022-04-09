package ru.itmo.services.serv;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.itmo.data.dao.CatDAO;
import ru.itmo.data.entity.Cat;
import ru.itmo.data.entity.Owner;

import java.util.Collections;
import java.util.List;

public class CatService implements CatDAO {
    private SessionFactory sessionFactory;

    public CatService() {
        sessionFactory = new Configuration()
                .addAnnotatedClass(Cat.class)
                .addAnnotatedClass(Owner.class)
                .buildSessionFactory();
    }

    @Override
    public void add(Cat cat) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        session.save(cat);

        session.getTransaction().commit();
    }

    @Override
    public void addFriend(Cat cat, Cat friend) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        cat.addFriend(friend);
        friend.addFriend(cat);
        session.update(cat);
        session.update(friend);

        session.getTransaction().commit();
    }

    @Override
    public void removeFriend(Cat cat, Cat friend) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        cat.removeFriend(friend);
        friend.removeFriend(cat);
        session.update(cat);
        session.update(friend);

        session.getTransaction().commit();
    }

    @Override
    public Cat getById(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        Cat result = session.get(Cat.class, id);

        session.getTransaction().commit();

        return result;
    }

    @Override
    public List<Cat> getAll() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        List<Cat> result = session.createQuery("select a from Cat a", Cat.class).getResultList();

        session.getTransaction().commit();

        return Collections.unmodifiableList(result);
    }

    @Override
    public void remove(Cat cat) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        for (Cat friend: cat.getFriends()) {
            friend.removeFriend(cat);
            session.update(friend);
        }

        cat.clearFriends();
        session.update(cat);
        session.remove(cat);

        session.getTransaction().commit();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
