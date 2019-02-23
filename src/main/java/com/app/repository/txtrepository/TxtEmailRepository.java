package com.app.repository.txtrepository;

import com.app.common.Email;
import com.app.repository.EmailRepository;

import java.util.List;

public class TxtEmailRepository extends TxtGenericRepository<Email> implements EmailRepository {
    @Override
    public List<Email> getAll() {
        return super.getAll();
    }

    @Override
    public Email get(int id) {
        return super.get(id);
    }

    @Override
    public Email add(Email item) {
        return super.add(item);
    }

    @Override
    public Iterable<Email> addRange(Iterable<Email> items) {
        return super.addRange(items);
    }

    @Override
    public Email update(Email item) {
        return super.update(item);
    }

    @Override
    public void remove(Email item) {
        super.remove(item);
    }
}
