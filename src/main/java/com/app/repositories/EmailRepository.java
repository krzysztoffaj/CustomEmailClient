package com.app.repositories;

import com.app.models.Email;
import com.app.models.User;

import java.util.Set;

public interface EmailRepository extends GenericRepository<Email> {
    Set<User> getReceiversFromEmailUserEntity(int emailId);

    void addEmailUserEntity(Email email);
}
