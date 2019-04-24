package com.app.services;

import com.app.models.Email;
import com.app.models.EmailMarks;
import com.app.models.User;
import com.app.repositories.EmailRepository;
import com.app.repositories.UserRepository;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestEmailService {

    private final static int INBOX_EMAIL_COUNT = 2;
    private final static int DELETED_EMAIL_COUNT = 1;
    private final static int DRAFT_WITH_TESTING_STRING_COUNT = 1;

    private final List<User> exampleUsers = Arrays.asList(
            new User(1, "William", "Murphy", "william.murphy@gmail.com", true),
            new User(2, "Benedict", "Garfield", "ben.garf@yahoo.com", false),
            new User(3, "Jack", "Norton", "jack.norton@microsoft.com", false),
            new User(4, "Jack", "Rivers", "jack.rivers@bbc.com", true),
            new User(5, "Steve", "Plant", "steve.plant@oracle.com", true)
    );

    private final List<Email> exampleEmails = Arrays.asList(
            new Email(1, exampleUsers.get(1), new HashSet<>(Arrays.asList(exampleUsers.get(1), exampleUsers.get(2))),
                      "test1", "Inbox", String.valueOf(EmailMarks.UNMARKED), "2018-04-01 08:00:00", "Testing correct receivers"),
            new Email(2, exampleUsers.get(3), new HashSet<>(Arrays.asList(exampleUsers.get(3), exampleUsers.get(4), exampleUsers.get(1))),
                      "test2", "Inbox", String.valueOf(EmailMarks.UNMARKED), "2018-04-01 08:00:00", "Testing single receiver"),
            new Email(3, exampleUsers.get(2), new HashSet<>(Collections.singletonList(exampleUsers.get(0))),
                      "test3", "Sent", String.valueOf(EmailMarks.UNREAD), "2018-04-01 08:00:00", "Testing sent mailbox"),
            new Email(4, exampleUsers.get(0), new HashSet<>(Arrays.asList(exampleUsers.get(3), exampleUsers.get(3))),
                      "test4", "Deleted", String.valueOf(EmailMarks.MARKED), "2018-04-01 08:00:00", "Testing duplicated receivers"),
            new Email(5, exampleUsers.get(4), new HashSet<>(Arrays.asList(exampleUsers.get(2), exampleUsers.get(4))),
                      "test5", "Draft", String.valueOf(EmailMarks.UNMARKED), "2018-04-01 08:00:00", "Testing draft mailbox")
    );

    @Test
    public void emailsShouldBeDividedIntoMailboxes() {
        EmailService service = getEmailService();

        List<Email> inboxEmails = service.getEmailsInMailbox("Inbox");

        assertEquals("Invalid number of emails in inbox",
                     INBOX_EMAIL_COUNT,
                     inboxEmails.size());
    }

    @Test
    public void searchOptionShouldWorkOnlyInOneMailbox() {
        EmailService service = getEmailService();

        List<Email> emailsFound = service.findEmailByText("Draft", "Testing");

        assertEquals("Invalid number of emails found by text. Probably looked beyond one mailbox.",
                     DRAFT_WITH_TESTING_STRING_COUNT,
                     emailsFound.size());
    }

    @Test
    public void deletingEmailShouldChangeItsMailbox() {
        EmailService service = getEmailService();

        service.deleteEmail(exampleEmails.get(0));
        List<Email> inboxEmailsAfterwards = service.getEmailsInMailbox("Inbox");
        List<Email> deletedEmailsAfterwards = service.getEmailsInMailbox("Deleted");

        assertEquals("Invalid number of emails. Deleting an email haven't moved it from the \"Inbox\" mailbox.",
                     INBOX_EMAIL_COUNT - 1,
                     inboxEmailsAfterwards.size());
        assertEquals("Invalid number of emails. Deleting an email haven't moved it to the \"Deleted\" mailbox.",
                     DELETED_EMAIL_COUNT + 1,
                     deletedEmailsAfterwards.size());
    }

    private EmailService getEmailService() {
        EmailRepository mockEmailRepo = mock(EmailRepository.class);
        when(mockEmailRepo.getAll()).thenReturn(exampleEmails);

        UserRepository mockUserRepo = mock(UserRepository.class);
        when(mockUserRepo.getAll()).thenReturn(exampleUsers);

        return new DefaultEmailService(mockEmailRepo, new DefaultUserService(mockUserRepo));
    }
}
