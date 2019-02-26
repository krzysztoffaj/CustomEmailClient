package com.app.repository.txtrepository;

import com.app.common.User;
import com.app.repository.UserRepository;

import java.io.File;

public class TxtUserRepository extends TxtGenericRepository<User> implements UserRepository {

    @Override
    public User castType(File file) {
        return null;
    }
}
