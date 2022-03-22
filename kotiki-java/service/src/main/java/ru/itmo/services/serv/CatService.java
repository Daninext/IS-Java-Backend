package ru.itmo.services.serv;

import org.hibernate.Session;
import ru.itmo.data.dao.CatDAO;
import ru.itmo.data.entity.Cat;
import ru.itmo.services.Controller;

import java.util.Collections;
import java.util.List;

public class CatService extends Controller implements CatDAO {

    @Override
    public void add(Cat cat) {
        Session session = getSessionFactory().getCurrentSession();
        session.beginTransaction();

        session.save(cat);

        session.getTransaction().commit();
    }

    @Override
    public Cat getById(int id) {
        Session session = getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Cat result = session.get(Cat.class, id);

        session.getTransaction().commit();

        return result;
    }

    @Override
    public List<Cat> getAll() {
        Session session = getSessionFactory().getCurrentSession();
        session.beginTransaction();

        List<Cat> result = session.createQuery("select a from Cat a", Cat.class).getResultList();

        session.getTransaction().commit();

        return Collections.unmodifiableList(result);
    }

    @Override
    public void remove(Cat cat) {
        Session session = getSessionFactory().getCurrentSession();
        session.beginTransaction();

        session.remove(cat);

        session.getTransaction().commit();
    }
}
