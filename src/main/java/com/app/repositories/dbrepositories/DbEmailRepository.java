package com.app.repositories.dbrepositories;

import com.app.models.Email;
import com.app.models.User;
import com.app.repositories.EmailRepository;

import java.util.Set;

//@Repository("dbEmailRepository")
public class DbEmailRepository extends DbGenericRepository<Email> implements EmailRepository {
    @Override
    public Set<User> getReceiversFromEmailUserEntity(int emailId) {
        return null;
    }

    @Override
    public void addEmailUserEntity(Email email) {

    }
}
