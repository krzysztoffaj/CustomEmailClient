package com.app.services;

import com.app.repository.IEmailRepository;
import com.app.common.Email;

import java.util.List;

public class EmailService implements IEmailService{
    private IEmailRepository emailRepository;

    public EmailService(IEmailRepository emailRepository) {
        this.emailRepository = emailRepository;

    }
    public List<Email> getEmails(){
        return emailRepository.getAll();
    }



}
