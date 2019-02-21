package com.app.services;

import com.app.common.Email;
import com.app.repository.IEmailRepository;

import java.util.List;

public class EmailService implements IEmailService{
    private IEmailRepository emailRepository;

    public EmailService(IEmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @Override
    public List<Email> getEmails() {
        return emailRepository.getAll();
    }
}
