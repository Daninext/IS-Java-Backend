package ru.itmo.services.serv;

import org.hibernate.Session;
import ru.itmo.data.dao.OwnerDAO;
import ru.itmo.data.entity.Owner;
import ru.itmo.services.Controller;

import java.util.Collections;
import java.util.List;

public class OwnerService extends Controller implements OwnerDAO {

    @Override
    public void add(Owner owner) {
        Session session = getSessionFactory().getCurrentSession();
        session.beginTransaction();

        session.save(owner);

        session.getTransaction().commit();
    }

    @Override
    public Owner getById(int id) {
        Session session = getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Owner result = session.get(Owner.class, id);

        session.getTransaction().commit();

        return result;
    }

    @Override
    public List<Owner> getAll() {
        Session session = getSessionFactory().getCurrentSession();
        session.beginTransaction();

        List<Owner> result = session.createQuery("select a from Owner a", Owner.class).getResultList();

        session.getTransaction().commit();

        return Collections.unmodifiableList(result);
    }

    @Override
    public void remove(Owner owner) {
        Session session = getSessionFactory().getCurrentSession();
        session.beginTransaction();

        session.remove(owner);

        session.getTransaction().commit();
    }
}
