package com.app.services;

import com.app.common.Email;
import com.app.repository.EmailRepository;

import java.util.*;

public class DefaultEmailService implements EmailService {
    private EmailRepository emailRepository;

    public DefaultEmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
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
                    email.getReceiversFormatted() +
                    email.getSubject() +
                    email.getBody();
            if (searchRange.contains(text)) {
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
        emailCopy.setReceivers(Collections.singletonList(email.getSender()));
        return emailCopy;
    }

    @Override
    public Email prepareReplyToAllEmail(Email email) {
        Email emailCopy = createEmailCopy(email);
        emailCopy.setSubject("RE: " + email.getSubject());
        List<String> receivers = new ArrayList<>();
        receivers.add(email.getSender());
        receivers.addAll(email.getReceivers());
        emailCopy.setReceivers(receivers);
        return emailCopy;
    }

    @Override
    public Email prepareForwardEmail(Email email) {
        Email emailCopy = createEmailCopy(email);
        emailCopy.setSubject("FW: " + email.getSubject());
        emailCopy.setReceivers(new ArrayList<>());
        return emailCopy;
    }
}