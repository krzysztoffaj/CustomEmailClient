package com.app.services;

import com.app.models.User;
import com.app.repositories.UserRepository;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class TestUserService {

    private final List<User> exampleUsers = Arrays.asList(
            new User(1, "William", "Murphy", "william.murphy@gmail.com", true),
            new User(2, "Benedict", "Garfield", "ben.garf@yahoo.com", false),
            new User(3, "Jack", "Norton", "jack.norton@microsoft.com", false),
            new User(4, "Jack", "Rivers", "jack.rivers@bbc.com", true),
            new User(5, "Steve", "Plant", "steve.plant@oracle.com", true)
    );

    @Test
    public void specificGetterShouldFilterOutUsersInAddressBook() {
        // given
        UserService service = getUserService();
        List<User> expectedUsers = new ArrayList<>(
                Arrays.asList(exampleUsers.get(0), exampleUsers.get(3), exampleUsers.get(4)));

        // when
        List<User> usersInAddressBook = service.getUsersInAddressBook();

        // then
        assertThat("Invalid users returned. Probably looked beyond an address book.",
                   usersInAddressBook,
                   containsInAnyOrder(expectedUsers));
    }

    @Test
    @Parameters(method = "getSearchUserCases")
    public void searchOptionShouldOnlyWorkInAddressBook(String text, int[] userIndices) {
        // given
        UserService service = getUserService();
        List<User> expectedUsers = new ArrayList<>();
        for (int index : userIndices) {
            expectedUsers.add(exampleUsers.get(index));
        }

        // when
        List<User> usersFound = service.findUserByText(text);

        // then
        assertThat("Invalid users found by text. Probably looked beyond an address book or search criteria.",
                   usersFound,
                   containsInAnyOrder(expectedUsers));
    }

    @Test
    @Parameters(method = "getFindUserOrCreateNewCases")
    public void notFindingUserByEmailAddressShouldCreateNewOne(String emailAddress, int index) {
        // given
        UserService service = getUserService();
        int usersCountBefore = exampleUsers.size();

        // when
        User userFoundOrCreated = service.getUserWithEmailAddressOrCreateNewOne(emailAddress);
        int usersCountAfter = service.getUsers().size();

        // then
        assertEquals("Invalid email address search did not create a new user.",
                     usersCountBefore,
                     usersCountAfter);
    }

    @Test
    public void deletingUserShouldRemoveItFromAddressBook() {
        // given
        UserService service = getUserService();
        int countBeforeDeletion = service.getUsersInAddressBook().size();

        // when
        service.deleteUser(exampleUsers.get(0));
        int countAfterDeletion = service.getUsersInAddressBook().size();

        // then
        assertEquals("Invalid number of users in address book. Deletion might have not removed him from address book.",
                     countBeforeDeletion,
                     countAfterDeletion);
    }

    private UserService getUserService() {
        UserRepository mockUserRepo = mock(UserRepository.class);
        when(mockUserRepo.getAll()).thenReturn(exampleUsers);

        return new DefaultUserService(mockUserRepo);
    }

    private Object[] getSearchUserCases() {
        return new Object[]{
                new Object[]{"Jack", new int[]{3}},
                new Object[]{"@", new int[]{0, 3, 4}},
                new Object[]{"Benedict", new int[]{}},
                new Object[]{" ", new int[]{}},
                new Object[]{"Steve", new int[]{4}},
                new Object[]{"Plant", new int[]{4}},
                new Object[]{"oracle", new int[]{4}}
        };
    }

    private Object[] getFindUserOrCreateNewCases() {
        return new Object[]{
                new Object[]{"steve.plant@oracle.com", exampleUsers.size()},
                new Object[]{"jack.norton@microsoft.com", exampleUsers.size()},
                new Object[]{"non-existing", exampleUsers.size() + 1},
                new Object[]{" ", exampleUsers.size() + 1},
                new Object[]{"", exampleUsers.size() + 1},
                new Object[]{"ack.norton@microsoft.com", exampleUsers.size() + 1}
        };
    }

    public static <T> org.hamcrest.Matcher<java.lang.Iterable<? extends T>> containsInAnyOrder(Collection<T> items) {
        return org.hamcrest.collection.IsIterableContainingInAnyOrder.<T>containsInAnyOrder((T[]) items.toArray());
    }
}
