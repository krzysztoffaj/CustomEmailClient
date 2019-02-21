package com.app.repository;

import com.app.common.IEntityId;

import java.util.ArrayList;
import java.util.List;

public interface IGenericRepository<T extends IEntityId> {
    List<T> getAll();
    T get(int id);
    T add(T item);
    Iterable<T> addRange(Iterable<T> items);
    T update(T item);
    void remove(T item);
}
