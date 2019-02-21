package com.app.repository.dbRepository;

import com.app.common.IEntityId;
import com.app.repository.IGenericRepository;

import java.util.List;

public class DbGenericRepository<T extends IEntityId> implements IGenericRepository<T> {
    @Override
    public List<T> getAll() {
        using(var context = new DbContext())
        {
            var items = context.Set<T>().ToList();
            return items;
        }
    }

    @Override
    public T get(int id) {
        return null;
    }

    @Override
    public T add(T item) {

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
        var itemToDelete = context.Set<T>.FirstOrDefault(x => x.getId() = item.getId());
        cotnext.Set<T>().remove(itemToDelete);
        context.SaveChanges();
    }
}
