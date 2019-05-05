package com.app.services;

import com.app.infrastructure.EmailBuilder;
import com.app.models.Email;
import com.app.infrastructure.EmailMarks;
import com.app.models.User;
import com.app.repositories.EmailRepository;
import com.app.repositories.UserRepository;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
                      "test1", "Inbox", String.valueOf(EmailMarks.UNMARKED), "2018-04-01 08:05:33", "Testing correct receivers"),
            new Email(2, exampleUsers.get(3), new HashSet<>(Arrays.asList(exampleUsers.get(3), exampleUsers.get(4), exampleUsers.get(1))),
                      "test2", "Inbox", String.valueOf(EmailMarks.UNMARKED), "2019-01-12 15:35:27", "Testing single receiver"),
            new Email(3, exampleUsers.get(2), new HashSet<>(Collections.singletonList(exampleUsers.get(0))),
                      "test3", "Draft", String.valueOf(EmailMarks.UNREAD), "2018-07-08 09:10:11", "Whatever"),
            new Email(4, exampleUsers.get(0), new HashSet<>(Arrays.asList(exampleUsers.get(3), exampleUsers.get(3))),
                      "test4", "Deleted", String.valueOf(EmailMarks.MARKED), "2019-04-23 20:14:53", "Testing duplicated receivers"),
            new Email(5, exampleUsers.get(4), new HashSet<>(Arrays.asList(exampleUsers.get(2), exampleUsers.get(4))),
                      "test5", "Draft", String.valueOf(EmailMarks.UNMARKED), "2018-12-10 10:10:10", "Testing draft mailbox")
    );

    @Test
    public void emailsShouldBeDividedIntoMailboxes() {
        String mailbox = "Inbox";
        EmailService service = getEmailService();

        List<Email> inboxEmails = service.getEmailsInMailbox(mailbox);

        assertEquals("Invalid collection returned.",
                     Arrays.asList(exampleEmails.get(0), exampleEmails.get(1)),
                     inboxEmails);
    }

    @Test
    public void emailsShouldBeSorted() {
        // Arrange
        String mailbox = "Inbox";
        List<Email> expectedEmails = new ArrayList<>();
        for (int i = 5; i > 0; i--) {
            expectedEmails.add(new EmailBuilder()
                                       .withMailbox(mailbox)
                                       .withDateTime(String.format("2018-04-0%s 08:00:00", i))
                                       .build());
        }

        List<Email> reorderedEmails = new ArrayList<>();
        int[] indices = {1, 0, 3, 2, 4};
        for (int index : indices) {
            reorderedEmails.add(expectedEmails.get(index));
        }

        EmailService service = getEmailServiceWithCustomEmailMock(reorderedEmails);

        // Act
        List<Email> inboxEmails = service.getEmailsInMailbox(mailbox);

        // Assert
//        assertEquals("Invalid number of emails in inbox",
//                     INBOX_EMAIL_COUNT,
//                     inboxEmails.size());
        assertEquals(expectedEmails, inboxEmails);
    }

    @Test
    public void searchOptionShouldWorkOnlyInOneMailbox() {
        final int DRAFT_WITH_TESTING_STRING_COUNT = 1;
        EmailService service = getEmailService();

        List<Email> emailsFound = service.findEmailByText("Draft", "Testing");

        assertEquals("Invalid number of emails found by text. Probably looked beyond one mailbox.",
                     DRAFT_WITH_TESTING_STRING_COUNT,
                     emailsFound.size());
    }

    @Test
    public void deletingEmailShouldChangeItsMailbox() {
        final int inboxEmailCount = 2;
        final int deletedEmailCount = 1;
        EmailService service = getEmailService();

        service.deleteEmail(exampleEmails.get(0));
        List<Email> inboxEmailsAfterwards = service.getEmailsInMailbox("Inbox");
        List<Email> deletedEmailsAfterwards = service.getEmailsInMailbox("Deleted");

        assertEquals("Invalid number of emails. Deleting an email haven't moved it from the \"Inbox\" mailbox.",
                     inboxEmailCount - 1,
                     inboxEmailsAfterwards.size());
        assertEquals("Invalid number of emails. Deleting an email haven't moved it to the \"Deleted\" mailbox.",
                     deletedEmailCount + 1,
                     deletedEmailsAfterwards.size());
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
}
