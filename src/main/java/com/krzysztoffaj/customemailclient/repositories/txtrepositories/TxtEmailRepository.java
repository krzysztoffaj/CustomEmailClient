package com.krzysztoffaj.customemailclient.repositories.txtrepositories;

import com.krzysztoffaj.customemailclient.entities.Email;
import com.krzysztoffaj.customemailclient.entities.User;
import com.krzysztoffaj.customemailclient.repositories.EmailRepository;
import com.krzysztoffaj.customemailclient.repositories.UserRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
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
        email.setReceivers(getReceiversFromEmailUserEntity(email.getId()));
        email.setSubject(emailFile.get(1));
        email.setMailbox(emailFile.get(2));
        email.setMark(emailFile.get(3));
        email.setDateTime(LocalDateTime.parse(emailFile.get(4)));
        email.setBody(prepareEmailBody(emailFile));

        return email;
    }

    @Override
    public void addEntity(Email email, PrintWriter writer) {
        writeEmailToFile(email, writer);
        addEmailUserEntity(email);
    }

    @Override
    public void updateEntity(Email email, PrintWriter writer) {
        writeEmailToFile(email, writer);
    }

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
