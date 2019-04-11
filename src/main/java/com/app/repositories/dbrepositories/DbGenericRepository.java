package com.app.repositories.dbrepositories;

import com.app.models.EntityId;
import com.app.repositories.GenericRepository;

import java.util.List;

public abstract class DbGenericRepository<T extends EntityId> implements GenericRepository<T> {
    @Override
    public T get(int id) {
        return null;
    }

    @Override
    public List<T> getAll() {
        return null;
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
