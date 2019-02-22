package com.app.services;

import com.app.common.Email;
import com.app.repository.EmailRepository;

import java.util.List;

public class DefaultEmailService implements EmailService {
    private EmailRepository emailRepository;

    public DefaultEmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @Override
    public List<Email> getEmails() {
        return emailRepository.getAll();
    }
}
