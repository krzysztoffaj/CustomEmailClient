package com.app.services;

import com.app.common.Email;
import com.app.repository.EmailRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
}
git