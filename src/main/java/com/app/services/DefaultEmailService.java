package com.app.services;

import com.app.common.Email;
import com.app.repository.EmailRepository;

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
//        emails.sort(Comparator.comparing(Email::getDate).reversed());
//        emailRepository.get(1);

        return emails;
    }
}
