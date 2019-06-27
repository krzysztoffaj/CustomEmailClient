package com.krzysztoffaj.customemailclient.repositories.dbrepositories;

import com.krzysztoffaj.customemailclient.entities.Email;
import com.krzysztoffaj.customemailclient.repositories.EmailRepository;
import com.krzysztoffaj.customemailclient.repositories.UserRepository;

public class DbEmailRepository extends DbGenericRepository<Email> implements EmailRepository {

    private UserRepository userRepo;

    public DbEmailRepository(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
}
