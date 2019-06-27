package com.krzysztoffaj.customemailclient.services;

import com.krzysztoffaj.customemailclient.repositories.EmailRepository;
import com.krzysztoffaj.customemailclient.repositories.UserRepository;
import com.krzysztoffaj.customemailclient.infrastructure.EmailBuilder;
import com.krzysztoffaj.customemailclient.infrastructure.EmailMarks;
import com.krzysztoffaj.customemailclient.entities.Email;
import com.krzysztoffaj.customemailclient.entities.User;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.*;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class TestEmailService {

    private final List<User> exampleUsers = Arrays.asList(
            new User(1, "William", "Murphy", "william.murphy@gmail.com", true),
            new User(2, "Benedict", "Garfield", "ben.garf@yahoo.com", false),
            new User(3, "Jack", "Norton", "jack.norton@microsoft.com", false),
            new User(4, "Jack", "Rivers", "jack.rivers@bbc.com", true),
            new User(5, "Steve", "Plant", "steve.plant@oracle.com", true)
    );

    private final List<Email> exampleEmails = Arrays.asList(
            new Email(1, exampleUsers.get(1), new HashSet<>(Arrays.asList(exampleUsers.get(1), exampleUsers.get(2))),
                      "test1", "Inbox", String.valueOf(EmailMarks.UNMARKED), LocalDateTime.parse("2018-04-01T08:05:33"), "Testing correct receivers"),
            new Email(2, exampleUsers.get(3), new HashSet<>(Arrays.asList(exampleUsers.get(3), exampleUsers.get(4), exampleUsers.get(1))),
                      "test2", "Inbox", String.valueOf(EmailMarks.UNMARKED), LocalDateTime.parse("2019-01-12T15:35:27"), "Testing single receiver"),
            new Email(3, exampleUsers.get(2), new HashSet<>(Collections.singletonList(exampleUsers.get(0))),
                      "test3", "Draft", String.valueOf(EmailMarks.UNREAD), LocalDateTime.parse("2018-07-08T09:10:11"), "Whatever"),
            new Email(4, exampleUsers.get(0), new HashSet<>(Arrays.asList(exampleUsers.get(3), exampleUsers.get(3))),
                      "test4", "Deleted", String.valueOf(EmailMarks.MARKED), LocalDateTime.parse("2019-04-23T20:14:53"), "Testing duplicated receivers"),
            new Email(5, exampleUsers.get(4), new HashSet<>(Arrays.asList(exampleUsers.get(2), exampleUsers.get(4))),
                      "test5", "Draft", String.valueOf(EmailMarks.UNMARKED), LocalDateTime.parse("2018-12-10T10:10:10"), "Testing draft mailbox")
    );

    @Test
    @Parameters(method = "getMailboxEmailIndicesPairs")
    public void emailsShouldBeDividedIntoMailboxes(String mailbox, int[] emailIndices) {
        // given
        EmailService service = getEmailService();
        List<Email> expectedEmails = new ArrayList<>();
        for (int index : emailIndices) {
            expectedEmails.add(exampleEmails.get(index));
        }

        // when
        List<Email> inboxEmails = service.getEmailsInMailbox(mailbox);

        // then
        assertThat("Invalid emails returned. Probably looked beyond one mailbox.",
                   inboxEmails,
                   containsInAnyOrder(expectedEmails));
    }

    @Test
    @Parameters(method = "getIndicesForSortingTest")
    public void emailsShouldBeSortedByDateTime(int[] indices) {
        // given
        String mailbox = "Inbox";
        List<Email> expectedEmails = new ArrayList<>();
        for (int i = 5; i > 0; i--) {
            expectedEmails.add(new EmailBuilder()
                                       .withMailbox(mailbox)
                                       .withDateTime(LocalDateTime.parse(String.format("2018-04-0%sT08:00:00", i)))
                                       .build());
        }

        List<Email> disorderedEmails = new ArrayList<>();
        for (int index : indices) {
            disorderedEmails.add(expectedEmails.get(index));
        }

        EmailService service = getEmailServiceWithCustomEmailMock(disorderedEmails);

        // when
        List<Email> inboxEmails = service.getEmailsInMailbox(mailbox);

        // then
        assertEquals("Emails not sorted from newest to oldest.",
                     expectedEmails,
                     inboxEmails);
    }

    @Test
    @Parameters(method = "getSearchEmailCases")
    public void searchOptionShouldWorkOnlyInOneMailbox(String mailbox, String searchText, int[] emailIndices) {
        // given
        EmailService service = getEmailService();
        List<Email> expectedEmails = new ArrayList<>();
        for (int index : emailIndices) {
            expectedEmails.add(exampleEmails.get(index));
        }

        // when
        List<Email> emailsFound = service.findEmailByText(mailbox, searchText);

        // then
        assertThat("Invalid emails found by text. Probably looked beyond one mailbox.",
                   emailsFound,
                   containsInAnyOrder(expectedEmails));
    }

    @Test
    public void deletingEmailShouldOnlyChangeItsMailbox() {
        // given
        EmailService service = getEmailService();

        // when
        service.deleteEmail(exampleEmails.get(0));

        // then
        assertEquals("Invalid mailbox. Deleting an email haven't moved it from \"Inbox\".",
                     "Deleted",
                     exampleEmails.get(0).getMailbox());
    }

    @Test
    @Parameters({"0", "1", "2", "3", "4"})
    public void emailCopyShouldHaveSamePropertyValuesAsOriginalExceptId(int emailId) {
        // given
        EmailService service = getEmailService();

        // when
        Email emailCopy = service.createEmailCopy(exampleEmails.get(emailId));

        // then
        assertThat("Email copy differentiates from its original.",
                   exampleEmails.get(emailId),
                   sameBeanAs(emailCopy).ignoring("id"));
    }

    private EmailService getEmailService() {
        EmailRepository mockEmailRepo = mock(EmailRepository.class);
        when(mockEmailRepo.getAll()).thenReturn(exampleEmails);

        UserRepository mockUserRepo = mock(UserRepository.class);
        when(mockUserRepo.getAll()).thenReturn(exampleUsers);

        return new DefaultEmailService(mockEmailRepo, new DefaultUserService(mockUserRepo));
    }

    private EmailService getEmailServiceWithCustomEmailMock(List<Email> reorderedEmails) {
        EmailRepository mockEmailRepo = mock(EmailRepository.class);
        when(mockEmailRepo.getAll()).thenReturn(reorderedEmails);

        UserService mockUserService = mock(UserService.class);
        return new DefaultEmailService(mockEmailRepo, mockUserService);
    }

    private Object[] getIndicesForSortingTest() {
        return new int[][]{
                {3, 2, 1, 0, 4},
                {4, 0, 2, 1, 3},
                {3, 4, 1, 2, 0},
                {3, 2, 4, 1, 0},
                {1, 3, 2, 4, 0},
        };
    }

    private Object[] getMailboxEmailIndicesPairs() {
        return new Object[]{
                new Object[]{"Inbox", new int[]{0, 1}},
                new Object[]{"Draft", new int[]{2, 4}},
                new Object[]{"Deleted", new int[]{3}}
        };
    }

    private Object[] getSearchEmailCases() {
        return new Object[]{
                new Object[]{"Inbox", "Testing", new int[]{0, 1}},
                new Object[]{"Inbox", "correct", new int[]{0}},
                new Object[]{"Draft", "te", new int[]{2, 4}},
                new Object[]{"Draft", "Testing", new int[]{4}},
                new Object[]{"Deleted", "deleted", new int[]{}},
                new Object[]{"Deleted", "duplicated", new int[]{3}}
        };
    }


    public static <T> org.hamcrest.Matcher<java.lang.Iterable<? extends T>> containsInAnyOrder(Collection<T> items) {
        return org.hamcrest.collection.IsIterableContainingInAnyOrder.<T>containsInAnyOrder((T[]) items.toArray());
    }
}
