package com.app.services;

import com.app.common.Email;
import com.app.common.User;
import com.app.repository.EmailRepository;

import java.util.*;

public class DefaultEmailService implements EmailService {
    private EmailRepository emailRepository;
    private UserService userService;

    public DefaultEmailService(EmailRepository emailRepository, UserService userService) {
        this.emailRepository = emailRepository;
        this.userService = userService;
    }

    @Override
    public List<Email> getEmails() {
        final List<Email> emails = emailRepository.getAll();
        emails.sort(Comparator.comparing(Email::getDateTime).reversed());
        return emails;
    }

    @Override
    public List<Email> findByText(String text) {
        List<Email> emailsFound = new ArrayList<>();

        getEmails().forEach(email -> {
            String searchRange =
                    email.getSender() +
                    email.getReceivers().toString() +
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

        emailCopy.setSender(email.getSender());
        emailCopy.setReceivers(email.getReceivers());
        emailCopy.setSubject(email.getSubject());
        emailCopy.setMailbox(email.getMailbox());
        emailCopy.setMark(email.getMark());
        emailCopy.setDateTime(email.getDateTime());
        emailCopy.setBody(email.getBody());

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
    public String originalEmailDetails(Email email) {
        return "\n\n" +
               "__________________________________________________\n\n" +
               "From:\t" + email.getSender() + "\n" +
               "Subject:\t" + email.getSubject() + "\n" +
               "Date:\t" + email.getDateTime() + "\n\n" +
               email.getBody();
    }

    @Override
    public String emailDetails(Email email) {
        return "From:\t" + userService.displayedUser(email.getSender()) + "\n" +
               "To:\t\t" + userService.getReceiversFormatted(email.getReceivers()) + "\n" +
               "Subject:\t" + email.getSubject() + "\n" +
               "Date:\t" + email.getDateTime();
    }

    @Override
    public String emailInfoOnList(Email email, String mailbox) {
        if (mailbox.equals("Sent") || mailbox.equals("Draft")) {
            return userService.getReceiversFormatted(email.getReceivers()) + "\n" +
                   email.getSubject() + "\n" +
                   email.getDateTime();
        } else {
            return userService.displayedUser(email.getSender()) + "\n" +
                   email.getSubject() + "\n" +
                   email.getDateTime();
        }
    }

    @Override
    public Set<User> getReceiversFromTextField(String input) {
        Set<User> receivers = new HashSet<>();
        for (String emailAddress : input.split("\\s*,\\s*")) {
            receivers.add(userService.getUserWithEmailAddressOrCreate(emailAddress));
        }
        return receivers;
    }
}