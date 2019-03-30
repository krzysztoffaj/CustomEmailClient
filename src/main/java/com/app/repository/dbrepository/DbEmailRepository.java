package com.app.repository.dbrepository;

import com.app.common.Email;
import com.app.repository.EmailRepository;

public class DbEmailRepository extends DbGenericRepository<Email> implements EmailRepository {
    @Override
    public void addEmailUserEntry(Email email) {

    }
}
