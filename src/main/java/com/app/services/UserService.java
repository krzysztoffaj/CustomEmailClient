package com.app.services;

import com.app.common.User;
import com.app.repository.IUserRepository;

import java.util.List;

public class UserService implements IUserService {
    private IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.getAll();
    }
}
