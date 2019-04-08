package com.app.repository.txtrepository;

import com.app.common.Email;
import com.app.common.User;
import com.app.repository.EmailRepository;
import com.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository("txtEmailRepository")
public class TxtEmailRepository extends TxtGenericRepository<Email> implements EmailRepository {
    private UserRepository userRepo;

    @Autowired
    public TxtEmailRepository(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Email castType(File file) {
        Email email = new Email(Integer.parseInt(file.getName()));
        List<String> emailFile = readAllLinesLazily(file);

        email.setSender(userRepo.get(Integer.parseInt(emailFile.get(0))));
        email.setReceivers(getReceiversFromEmailUserEntity(email.getId()));
        email.setSubject(emailFile.get(1));
        email.setMailbox(emailFile.get(2));
        email.setMark(emailFile.get(3));
        email.setDateTime((emailFile.get(4)));
        email.setBody(prepareEmailBody(emailFile));

        return email;
    }

    @Override
    public void addItem(Email email, PrintWriter writer) {
        writeEmailToFile(email, writer);
        addEmailUserEntity(email);
    }

    @Override
    public void updateItem(Email email, PrintWriter writer) {
        writeEmailToFile(email, writer);
    }

    @Override
    public Set<User> getReceiversFromEmailUserEntity(int emailId) {
        Set<User> receivers = new HashSet<>();
        Path emailUserFilePath = Paths.get(getTxtRepositoryDataPath(), "email_user", String.valueOf(emailId));
        try {
            Files.readAllLines(emailUserFilePath)
                    .forEach(line -> receivers.add(userRepo.get(Integer.parseInt(line))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receivers;
    }

    @Override
    public void addEmailUserEntity(Email email) {
        String newEntityPath = String.valueOf(Paths.get(getTxtRepositoryDataPath(), "email_user", String.valueOf(email.getId())));
        try (PrintWriter writer = new PrintWriter(newEntityPath)) {
            email.getReceivers()
                    .forEach(receiver -> writer.println(receiver.getId()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String prepareEmailBody(List<String> emailFile) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 7; i < emailFile.size(); i++) {
            stringBuilder.append(emailFile.get(i)).append("\n");
        }
        return stringBuilder.toString();
    }

    private void writeEmailToFile(Email email, PrintWriter writer) {
        writer.println(email.getSender().getId());
        writer.println(email.getSubject());
        writer.println(email.getMailbox());
        writer.println(email.getMark());
        writer.println(email.getDateTime());
        writer.println();
        writer.println();
        writer.println(email.getBody());
    }
}
