package com.app.services;

import com.app.models.Email;
import com.app.models.User;
import com.app.repositories.EmailRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service("emailService")
public class DefaultEmailService implements EmailService {
    private EmailRepository emailRepository;
    private UserService userService;

    public DefaultEmailService(EmailRepository emailRepository, UserService userService) {
        this.emailRepository = emailRepository;
        this.userService = userService;
    }

    @Override
    public List<Email> getEmailsInMailbox(String mailbox) {
        List<Email> emails = emailRepository.getAll();
        if (emails != null) {
            return emails
                    .stream()
                    .filter(x -> x.getMailbox().equals(mailbox))
                    .sorted(Comparator.comparing(Email::getDateTime).reversed())
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Email> findEmailByText(String mailbox, String text) {
        List<Email> emailsFound = new ArrayList<>();

        getEmailsInMailbox(mailbox)
                .stream()
                .filter(x -> x.getMailbox().equals(mailbox))
                .forEach(email -> {
                    String searchRange =
                            email.getSender().getEmailAddress() +
                            userService.listReceiversFullInfo(email.getReceivers()) +
                            email.getSubject() +
                            email.getBody();
                    if (searchRange.toLowerCase().contains(text.toLowerCase())) {
                        emailsFound.add(email);
                    }
                });

        return emailsFound;
    }

    @Override
    public void sendEmail(Email email) {
        emailRepository.add(email);
    }

    @Override
    public void saveEmail(Email email) {
        email.setMailbox("Saved");
        emailRepository.update(email);
    }

    @Override
    public void deleteEmail(Email email) {
        email.setMailbox("Deleted");
        emailRepository.update(email);
    }

    @Override
    public void saveDraft(Email email) {
        email.setMailbox("Draft");
        emailRepository.add(email);
    }

    @Override
    public Email createEmailCopy(Email email) {
        Email emailCopy = new Email();
        try {
            for (Field field : email.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                field.set(emailCopy, field.get(email));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        emailCopy.setId(0);

        return emailCopy;
    }

    @Override
    public Email prepareReplyEmail(Email email) {
        Email emailCopy = createEmailCopy(email);

        emailCopy.setSubject("RE: " + email.getSubject());
        emailCopy.setReceivers(Set.of(email.getSender()));

        return emailCopy;
    }

    @Override
    public Email prepareReplyToAllEmail(Email email) {
        Email emailCopy = createEmailCopy(email);

        emailCopy.setSubject("RE: " + email.getSubject());
        Set<User> receivers = new HashSet<>();
        receivers.add(email.getSender());
        receivers.addAll(email.getReceivers());
        emailCopy.setReceivers(receivers);

        return emailCopy;
    }

    @Override
    public Email prepareForwardEmail(Email email) {
        Email emailCopy = createEmailCopy(email);

        emailCopy.setSubject("FW: " + email.getSubject());
        emailCopy.setReceivers(new HashSet<>());

        return emailCopy;
    }

    @Override
    public String getOriginalEmailDetails(Email email) {
        return "\n\n" +
               "__________________________________________________\n\n" +
               "From:\t" + userService.getFullUserInfo(email.getSender()) + "\n" +
               "To:\t\t" + userService.listReceiversFullInfo(email.getReceivers()) + "\n" +
               "Subject:\t" + email.getSubject() + "\n" +
               "Date:\t" + email.getDateTime() + "\n\n" +
               email.getBody();
    }

    @Override
    public String getEmailDetails(Email email) {
        return "From:\t" + userService.getFullUserInfo(email.getSender()) + "\n" +
               "To:\t\t" + userService.listReceiversFullInfo(email.getReceivers()) + "\n" +
               "Subject:\t" + email.getSubject() + "\n" +
               "Date:\t" + email.getDateTime();
    }

    @Override
    public String getEmailInfoOnList(Email email, String mailbox) {
        if (mailbox.equals("Sent") || mailbox.equals("Draft")) {
            return userService.listReceiversFullInfo(email.getReceivers()) + "\n" +
                   email.getSubject() + "\n" +
                   email.getDateTime();
        } else {
            return userService.getFullUserInfo(email.getSender()) + "\n" +
                   email.getSubject() + "\n" +
                   email.getDateTime();
        }
    }

    @Override
    public Set<User> getReceiversFromTextField(String input) {
        Set<User> receivers = new HashSet<>();
        for (String emailAddress : input.split("\\s*,\\s*")) {
            receivers.add(userService.getUserWithEmailAddressOrCreateNew(emailAddress));
        }
        return receivers;
    }
}