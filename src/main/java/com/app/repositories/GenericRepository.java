package com.app.repositories;

import com.app.models.EntityId;

import java.util.List;

public interface GenericRepository<T extends EntityId> {
    T get(int id);

    List<T> getAll();

    void add(T item);

    void update(T item);

    void delete(T item);
}
