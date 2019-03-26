package com.app.services;

import com.app.common.User;
import com.app.repository.UserRepository;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DefaultUserService implements UserService {
    private UserRepository userRepository;

    public DefaultUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(int id) {
        return userRepository.get(id);
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

    @Override
    public void addUser(User user) {
        userRepository.add(user);
    }

    @Override
    public void editUser(User user) {
        userRepository.update(user);
    }

    @Override
    public void deleteUser(User user) {
        user.setInAddressBook(false);
        userRepository.update(user);
    }

    @Override
    public String displayedUser(User user) {
        return MessageFormat.format("{0} {1} <{2}>",
                user.getFirstName(),
                user.getLastName(),
                user.getEmailAddress());
    }

    @Override
    public String getReceiversFormatted(Set<User> receivers) {
        StringBuilder builder = new StringBuilder();
        int receiversCount = 0;
        for (User receiver : receivers) {
            receiversCount++;
            if(receiversCount < receivers.size()) {
                builder.append(displayedUser(receiver)).append(", ");
            } else {
                builder.append(displayedUser(receiver));
            }
        }
        return builder.toString();
    }

    @Override
    public boolean checkIfExistsWithEmailAddress(String emailAddress) {
        return getUsers().stream().anyMatch(usr -> usr.getEmailAddress().equals(emailAddress));
    }
}
