package com.app.services;

import com.app.common.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> getUsers();

    List<User> findByText(String text);

    void addUser(User user);

    void editUser(User user);

    void deleteUser(User user);

    String displayedUser(User user);

    String getReceiversFormatted(Set<User> receivers);
}
