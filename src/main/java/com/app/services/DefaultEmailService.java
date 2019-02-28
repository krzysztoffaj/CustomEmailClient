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
        List<Email> allEmails = getEmails();
        List<Email> itemsFound = new ArrayList<>();

        StringBuilder builder;
        for (Email email : allEmails) {
            builder = new StringBuilder();
            builder.append(email.getSender());
            builder.append(email.getReceiversFormatted());
            builder.append(email.getSubject());
            builder.append(email.getBody());
            if(builder.toString().contains(text)) {
                itemsFound.add(email);
            }
        }

        for (Email email : itemsFound) {
            System.out.println(email.getId());
        }

        return itemsFound;
    }
}
