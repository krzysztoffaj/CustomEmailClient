package com.app.services;

import com.app.common.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();

    List<User> findByText(String text);

    void addUser(User user);

    void editUser(User user);

    void deleteUser(User user);

    String displayedUser(User user);
}
