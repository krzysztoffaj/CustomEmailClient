package com.app.repositories.dbrepositories;

import com.app.models.EntityId;
import com.app.repositories.GenericRepository;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

import static com.app.HibernateUtilities.getSessionFactory;

public abstract class DbGenericRepository<T extends EntityId> implements GenericRepository<T> {
    @Override
    public T get(int id) {

        return null;
    }

    @Override
    public List<T> getAll() {
        Session sessionObj = getSessionFactory().openSession();

        sessionObj.close();
        return new ArrayList<>();
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
}
