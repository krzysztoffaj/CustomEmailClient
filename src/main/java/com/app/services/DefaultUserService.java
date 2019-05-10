package com.app.services;

import com.app.models.User;
import com.app.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("userService")
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
        final List<User> users = userRepository.getAll();
        if (users != null) {
            users.sort(Comparator.comparing(User::getEmailAddress));
            return users;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<User> getUsersInAddressBook() {
        return getUsers()
                .stream()
                .filter(User::isInAddressBook)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findUserByText(String text) {
        List<User> usersFound = new ArrayList<>();

        getUsers()
                .stream()
                .filter(User::isInAddressBook)
                .forEach(user -> {
                    String searchRange =
                            user.getFirstName() +
                            user.getLastName() +
                            user.getEmailAddress();
                    if (searchRange.toLowerCase().contains(text.toLowerCase())) {
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
    public String getFullUserInfo(User user) {
        return MessageFormat.format("{0} {1} <{2}>",
                                    user.getFirstName(),
                                    user.getLastName(),
                                    user.getEmailAddress());
    }

    @Override
    public String listReceiversEmailAddresses(Set<User> receivers) {
        StringBuilder builder = new StringBuilder();
        int receiversCount = 0;
        for (User receiver : receivers) {
            receiversCount++;
            if (receiversCount < receivers.size()) {
                builder.append(receiver.getEmailAddress()).append(", ");
            } else {
                builder.append(receiver.getEmailAddress());
            }
        }
        return builder.toString();
    }

    @Override
    public String listReceiversFullInfo(Set<User> receivers) {
        StringBuilder builder = new StringBuilder();
        int receiversCount = 0;
        for (User receiver : receivers) {
            receiversCount++;
            if (receiversCount < receivers.size()) {
                builder.append(getFullUserInfo(receiver)).append(", ");
            } else {
                builder.append(getFullUserInfo(receiver));
            }
        }
        return builder.toString();
    }

    @Override
    public User getUserWithEmailAddressOrCreateNewOne(String emailAddress) {
        return getUsers()
                .stream()
                .filter(usr -> usr.getEmailAddress().equals(emailAddress))
                .findFirst()
                .orElseGet(() -> {
                    User newUser = new User();

                    newUser.setFirstName("");
                    newUser.setLastName("");
                    newUser.setEmailAddress(emailAddress);
                    newUser.setInAddressBook(true);
                    userRepository.add(newUser);

                    return newUser;
                });
    }
}
