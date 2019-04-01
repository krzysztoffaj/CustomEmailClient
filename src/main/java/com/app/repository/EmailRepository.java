package com.app.repository;

import com.app.common.Email;
import com.app.common.User;

import java.util.Set;

public interface EmailRepository extends GenericRepository<Email> {
    Set<User> getReceiversFromEmailUserEntity(int emailId);

    void addEmailUserEntity(Email email);
}
