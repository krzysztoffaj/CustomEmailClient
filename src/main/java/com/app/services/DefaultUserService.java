package com.app.services;

import com.app.common.User;
import com.app.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class DefaultUserService implements UserService {
    private UserRepository userRepository;

    public DefaultUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.getAll();
    }

    @Override
    public List<User> findByText(String text) {
        List<User> usersFound = new ArrayList<>();

        getUsers().forEach(user -> {
            String searchRange =
                    user.getFirstName() +
                    user.getLastName() +
                    user.getEmailAddress();
            if (searchRange.contains(text)) {
                usersFound.add(user);
            }
        });

        return usersFound;
    }
}
