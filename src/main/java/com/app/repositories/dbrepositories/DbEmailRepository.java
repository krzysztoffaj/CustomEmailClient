package com.app.repositories.dbrepositories;

import com.app.models.Email;
import com.app.models.User;
import com.app.repositories.EmailRepository;
import com.app.repositories.UserRepository;

import java.util.Set;

public class DbEmailRepository extends DbGenericRepository<Email> implements EmailRepository {

    private UserRepository userRepo;

    public DbEmailRepository(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
}
