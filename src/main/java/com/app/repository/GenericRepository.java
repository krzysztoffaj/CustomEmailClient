package com.app.repository;

import com.app.common.EntityId;

import java.util.List;

public interface GenericRepository<T extends EntityId> {
    T get(int id);

    List<T> getAll();

    void add(T item);

    void update(T item);

    void delete(T item);
}
