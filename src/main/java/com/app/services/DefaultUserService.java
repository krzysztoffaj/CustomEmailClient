package com.app.services;

import com.app.common.User;
import com.app.repository.UserRepository;

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
}
