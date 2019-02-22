package com.app.repository.dbrepository;

import com.app.common.EntityId;
import com.app.repository.GenericRepository;

import java.util.List;

public class DbGenericRepository<T extends EntityId> implements GenericRepository<T> {
    @Override
    public List<T> getAll() {
        return null;
    }

    @Override
    public T get(int id) {
        return null;
    }

    @Override
    public T add(T item) {
        return null;
    }

    @Override
    public Iterable<T> addRange(Iterable<T> items) {
        return null;
    }

    @Override
    public T update(T item) {
        return null;
    }

    @Override
    public void remove(T item) {

    }
}
