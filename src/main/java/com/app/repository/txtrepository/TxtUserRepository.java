package com.app.repository.txtrepository;

import com.app.common.User;
import com.app.repository.UserRepository;

import java.io.File;
import java.io.PrintWriter;

public class TxtUserRepository extends TxtGenericRepository<User> implements UserRepository {

    @Override
    public User castType(File file) {
        return null;
    }

    @Override
    public void addItem(User item, PrintWriter writer) {

    }
}
