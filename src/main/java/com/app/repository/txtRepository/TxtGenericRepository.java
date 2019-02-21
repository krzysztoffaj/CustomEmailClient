package com.app.repository.txtRepository;

import com.app.common.IEntityId;
import com.app.repository.IGenericRepository;

import java.util.List;

public class TxtGenericRepository<T extends IEntityId> implements IGenericRepository<T> {
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
