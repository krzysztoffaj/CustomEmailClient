package com.app.repository.dbrepository;

import com.app.common.Email;
import com.app.common.User;
import com.app.repository.EmailRepository;

import java.util.Set;

public class DbEmailRepository extends DbGenericRepository<Email> implements EmailRepository {
    @Override
    public Set<User> getReceiversFromEmailUserEntity(int emailId) {
        return null;
    }

    @Override
    public void addEmailUserEntity(Email email) {

    }
}
