package com.app.repositories.dbrepositories;

import com.app.models.EntityId;
import com.app.repositories.GenericRepository;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import static com.app.HibernateUtilities.getSessionFactory;

public abstract class DbGenericRepository<T extends EntityId> implements GenericRepository<T> {

    @Override
    @SuppressWarnings({"unchecked"})
    public T get(int id) {
        Session session = getSessionFactory().openSession();

        T object = (T) session.get(getGenericInstance().getClass(), id);

        session.close();
        return object;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<T> getAll() {
        Session session = getSessionFactory().openSession();

//        CriteriaQuery<T> criteria = (CriteriaQuery<T>) session.getCriteriaBuilder().createQuery(getGenericInstance().getClass());
//        criteria.from(getGenericInstance().getClass());
//        List<T> all = session.createQuery(criteria).getResultList();

        List<T> all = session.createCriteria(getGenericInstance().getClass()).list();

        session.close();
        return all;
    }

    @Override
    public void add(T item) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();

        session.save(item);

        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void update(T item) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();

        session.update(item);

        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(T item) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();

        session.delete(item);

        session.getTransaction().commit();
        session.close();
    }

    @SuppressWarnings({"deprecation", "unchecked"})
    private T getGenericInstance() {
        T instance = null;
        try {
            instance = (T) ((Class) ((ParameterizedType) this.getClass().
                    getGenericSuperclass()).getActualTypeArguments()[0])
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return instance;
    }
}
