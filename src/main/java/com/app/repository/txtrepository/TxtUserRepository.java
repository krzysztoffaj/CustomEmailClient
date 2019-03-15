package com.app.repository.txtrepository;

import com.app.common.User;
import com.app.repository.UserRepository;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

public class TxtUserRepository extends TxtGenericRepository<User> implements UserRepository {

    @Override
    public User castType(File file) {
        User user = new User(Integer.parseInt(file.getName()));
        List<String> userFile = readAllLinesLazily(file);

        user.setFirstName(userFile.get(0));
        user.setLastName(userFile.get(1));
        user.setEmailAddress(userFile.get(2));

        return user;
    }

    @Override
    public void addItem(User user, PrintWriter writer) {
        writer.println(user.getFirstName());
        writer.println(user.getLastName());
        writer.println(user.getEmailAddress());
    }
}
