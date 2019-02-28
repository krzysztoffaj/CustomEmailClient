package com.app.repository.txtrepository;

import com.app.common.Email;
import com.app.repository.EmailRepository;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class TxtEmailRepository extends TxtGenericRepository<Email> implements EmailRepository {

    @Override
    public Email castType(File file) {
        Email email = new Email(Integer.parseInt(file.getName()));
        List<String> emailFile = readAllLinesLazily(file);

        email.setSender(emailFile.get(0));
        email.setReceivers(Arrays.asList(emailFile.get(1).split("\\s*,\\s*")));
        email.setSubject(emailFile.get(2));
        email.setMailbox(emailFile.get(3));
        email.setMark(emailFile.get(4));
        email.setDateTime((emailFile.get(5)));
        email.setBody(prepareEmailBody(emailFile));

        return email;
    }

    @Override
    public void addItem(Email email, PrintWriter writer) {
        writer.println(email.getSender());
        writer.println(email.getReceiversFormatted());
        writer.println(email.getSubject());
        writer.println(email.getMailbox());
        writer.println(email.getMark());
        writer.println(email.getDateTime());
        writer.println();
        writer.println();
        writer.println(email.getBody());
    }

    private String prepareEmailBody(List<String> emailFile) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 8; i < emailFile.size(); i++) {
            stringBuilder.append(emailFile.get(i)).append("\n");
        }
        return stringBuilder.toString();
    }
}
