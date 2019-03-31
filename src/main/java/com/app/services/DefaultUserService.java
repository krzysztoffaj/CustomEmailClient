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

//    private String getReceiversSeparatedByComma(Set<User> receivers, String receiverDisplayPattern) {
//        StringBuilder builder = new StringBuilder();
//        int receiversCount = 0;
//        for (User receiver : receivers) {
//            receiversCount++;
//            if (receiversCount < receivers.size()) {
//                builder.append(receiverDisplayPattern).append(", ");
//            } else {
//                builder.append(receiverDisplayPattern);
//            }
//        }
//        return builder.toString();
//    }

    @Override
    public User getUserWithEmailAddressOrCreate(String emailAddress) {
        return getUsers().stream()
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
