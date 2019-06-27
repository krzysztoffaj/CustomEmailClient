package com.krzysztoffaj.customemailclient.repositories.txtrepositories;

import com.krzysztoffaj.customemailclient.entities.User;
import com.krzysztoffaj.customemailclient.repositories.UserRepository;

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
        user.setInAddressBook(Boolean.parseBoolean(userFile.get(3)));

        return user;
    }

    @Override
    public void addEntity(User user, PrintWriter writer) {
        writeUserToFile(user, writer);
    }

    @Override
    public void updateEntity(User user, PrintWriter writer) {
        writeUserToFile(user, writer);
    }

    private void writeUserToFile(User user, PrintWriter writer) {
        writer.println(user.getFirstName());
        writer.println(user.getLastName());
        writer.println(user.getEmailAddress());
        writer.println(user.isInAddressBook());
    }
}
