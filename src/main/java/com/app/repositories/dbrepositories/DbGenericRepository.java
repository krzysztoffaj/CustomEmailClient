package com.app.repositories.dbrepositories;

import com.app.HibernateUtilities;
import com.app.models.Email;
import com.app.models.EntityId;
import com.app.models.User;
import com.app.repositories.GenericRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import static com.app.HibernateUtilities.getSessionFactory;

public abstract class DbGenericRepository<T extends EntityId> implements GenericRepository<T> {

    @Override
    @SuppressWarnings({"unchecked"})
    public T get(int id) {
        Session session = getSessionFactory().openSession();

        String queryString = String.format("FROM %s T WHERE T.id = :id", getInstanceSimpleName());
        T object = (T) session.createQuery(queryString)
                .setParameter("id", id)
                .getSingleResult();

        session.close();

        return object;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<T> getAll() {
        Session session = getSessionFactory().openSession();

        String queryString = String.format("FROM %s", getInstanceSimpleName());
        List<T> all = (List<T>) session.createQuery(queryString).list();

        session.close();
        return all;
    }

    @Override
    public void add(T item) {

    }

    @Override
    public void update(T item) {

    }

    @Override
    public void delete(T item) {

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

    private String getInstanceSimpleName() {
        return getGenericInstance().getClass().getSimpleName();
    }
}
