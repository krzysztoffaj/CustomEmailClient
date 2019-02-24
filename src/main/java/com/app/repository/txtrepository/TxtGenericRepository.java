package com.app.repository.txtrepository;

import com.app.common.EntityId;
import com.app.repository.GenericRepository;

import java.nio.file.Paths;
import java.util.List;

public class TxtGenericRepository<T extends EntityId> implements GenericRepository<T> {
    @Override
    public List<T> getAll() {
        return null;
    }

    @Override
    public T get(int id) {
        final String workingDirectory = String.valueOf(Paths.get("").toAbsolutePath());
        final String emailDirectory = String.valueOf(Paths.get(workingDirectory, "emails"));

        
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
    public T findByText(String text) {
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
