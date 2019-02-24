package com.app.repository;

import com.app.common.EntityId;

import java.util.List;

public interface GenericRepository<T extends EntityId> {
    List<T> getAll();

    T get(int id);

    T add(T item);

    Iterable<T> addRange(Iterable<T> items);

    T findByText(String text);

    T update(T item);

    void remove(T item);
}
