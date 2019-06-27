package com.krzysztoffaj.customemailclient.repositories;

import com.krzysztoffaj.customemailclient.entities.EntityId;

import java.util.List;

public interface GenericRepository<T extends EntityId> {
    T get(int id);

    List<T> getAll();

    void add(T entity);

    void update(T entity);

    void delete(T entityit);
}
