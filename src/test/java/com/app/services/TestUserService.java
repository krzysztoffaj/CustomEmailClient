package com.app.services;

import com.app.models.Email;
import com.app.models.User;
import com.app.repositories.EmailRepository;
import com.app.repositories.UserRepository;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUserService {

    private final static int ALL_USERS_IN_ADDRESS_BOOK = 3;
    private final static int JACKS_IN_ADDRESS_BOOK = 1;

    private static final List<User> exampleUsers = Arrays.asList(
            new User(1, "William", "Murphy", "william.murphy@gmail.com", true),
            new User(2, "Benedict", "Garfield", "ben.garf@yahoo.com", false),
            new User(3, "Jack", "Norton", "jack.norton@microsoft.com", false),
            new User(4, "Jack", "Rivers", "jack.rivers@bbc.com", true),
            new User(5, "Steve", "Plant", "steve.plant@oracle.com", true)
    );

    @Test
    public void specificGetterShouldFilterOutUsersInAddressBook() {
        UserService service = getUserService();

        final List<User> usersInAddressBook = service.getUsersInAddressBook();

        assertEquals("Invalid number of users found. Probably looked beyond address book.", ALL_USERS_IN_ADDRESS_BOOK, usersInAddressBook.size());
    }

    @Test
    public void searchOptionShouldOnlyWorkInAddressBook() {
        UserService service = getUserService();

        final List<User> usersFound = service.findUserByText("Jack");

        assertEquals("Invalid number of users found. Probably looked beyond address book.", JACKS_IN_ADDRESS_BOOK, usersFound.size());
    }

    @Test
    public void deletingUserShouldOnlyRemoveHimFromAddressBook() {
        UserService service = getUserService();
        final int usersCountBeforeDeletion = exampleUsers.size();

        service.deleteUser(exampleUsers.get(0));
        List<User> usersInAddressBook = service.getUsersInAddressBook();

        assertEquals("Invalid number of users. Probably permanently deleted a user record.", usersCountBeforeDeletion, exampleUsers.size());
        assertEquals("Invalid number of users in address book. Deletion might have not removed him from address book.", ALL_USERS_IN_ADDRESS_BOOK - 1, usersInAddressBook.size());
    }

    private UserService getUserService() {
        UserRepository mockUserRepo = mock(UserRepository.class);
        when(mockUserRepo.getAll()).thenReturn(exampleUsers);

        return new DefaultUserService(mockUserRepo);
    }
}
