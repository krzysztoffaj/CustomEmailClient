package com.app.services;

import com.app.common.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    User getUser(int id);

    List<User> getUsers();

    List<User> findByText(String text);

    void addUser(User user);

    void editUser(User user);

    void deleteUser(User user);

    String getFullUserInfo(User user);

    String listReceiversEmailAddresses(Set<User> receivers);

    String listReceiversFullInfo(Set<User> receivers);

    User getUserWithEmailAddressOrCreate(String emailAddress);
}
