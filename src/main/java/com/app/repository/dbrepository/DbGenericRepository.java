package com.app.repository.dbrepository;

import com.app.common.EntityId;
import com.app.repository.GenericRepository;

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
