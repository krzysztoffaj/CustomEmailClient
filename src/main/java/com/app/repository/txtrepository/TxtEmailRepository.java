package com.app.repository.txtrepository;

import com.app.common.Email;
import com.app.common.User;
import com.app.repository.EmailRepository;
import com.app.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TxtEmailRepository extends TxtGenericRepository<Email> implements EmailRepository {
    private UserRepository userRepo;

    public TxtEmailRepository(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Email castType(File file) {
        Email email = new Email(Integer.parseInt(file.getName()));
        List<String> emailFile = readAllLinesLazily(file);

        email.setSender(userRepo.get(Integer.parseInt(emailFile.get(0))));
        email.setReceivers(getReceiversFromEmailUserFile(emailUserFile(file)));
        email.setSubject(emailFile.get(1));
        email.setMailbox(emailFile.get(2));
        email.setMark(emailFile.get(3));
        email.setDateTime((emailFile.get(4)));
        email.setBody(prepareEmailBody(emailFile));

        return email;
    }

    @Override
    public void addItem(Email email, PrintWriter writer) {
        writer.println(email.getSender());
        writer.println(email.getReceivers());
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
        for (int i = 7; i < emailFile.size(); i++) {
            stringBuilder.append(emailFile.get(i)).append("\n");
        }
        return stringBuilder.toString();
    }

    private File emailUserFile(File email) {
        String txtRepoPath = email.getParentFile().getParentFile().getPath();
        return new File(String.valueOf(Paths.get(txtRepoPath, "email_user", email.getName())));
    }

    private Set<User> getReceiversFromEmailUserFile(File emailUserFile) {
        Set<User> receivers = new HashSet<>();
        try {
            Files.readAllLines(Paths.get(emailUserFile.getPath()))
                    .forEach(line -> receivers.add(userRepo.get(Integer.parseInt(line))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receivers;
    }
}
