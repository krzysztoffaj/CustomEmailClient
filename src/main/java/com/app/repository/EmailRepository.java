package com.app.repository;

import com.app.common.Email;

public interface EmailRepository extends GenericRepository<Email> {
    void addEmailUserEntry(Email email);
}
