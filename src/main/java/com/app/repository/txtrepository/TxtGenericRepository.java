package com.app.repository.txtrepository;

import com.app.common.EntityId;
import com.app.repository.GenericRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public abstract class TxtGenericRepository<T extends EntityId> implements GenericRepository<T> {
    private final String workingPath = String.valueOf(Paths.get("").toAbsolutePath());
    private final String txtRepositoryDataPath = String.valueOf(Paths.get(workingPath, "txtRepositoryData"));
    private final String genericTypePath = String.valueOf(Paths.get(txtRepositoryDataPath, getInstanceSimpleName()));

    public abstract T castType(File file);

    public abstract void addItem(T item, PrintWriter writer);

    @Override
    public List<T> getAll() {
        List<T> all = new ArrayList<>();
        try {
            Files.list(Paths.get(genericTypePath))
                    .forEach(path -> all.add(castType(new File(String.valueOf(path)))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return all;
    }

    @Override
    public T get(int id) {
        String desiredFilePath = String.valueOf(Paths.get(genericTypePath, String.valueOf(id)));
        return castType(new File(desiredFilePath));
    }

    @Override
    public T add(T item) {
        if(item.getId() == 0) {
            item.setId(getNextId());
        }
        try (PrintWriter writer = new PrintWriter(String.valueOf(Paths.get(genericTypePath, String.valueOf(item.getId()))))) {
            addItem(item, writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public Iterable<T> addRange(Iterable<T> items) {
        return null;
    }

    @Override
    public T update(T item) {
        return add(item);
    }

    @Override
    public void delete(T item) {
        try {
            Files.delete(Paths.get(genericTypePath, String.valueOf(item.getId())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    List<String> readAllLinesLazily(File file) {
        List<String> fileContent = new ArrayList<>();
        try {
            Files.lines(file.toPath()).forEach(fileContent::add);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileContent;
    }

    @SuppressWarnings({"deprecation", "unchecked"})
    private T getGenericInstance() {
        T instance = null;
        try {
            instance = (T) ((Class) ((ParameterizedType) this.getClass().
                    getGenericSuperclass()).getActualTypeArguments()[0])
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return instance;
    }

    private String getInstanceSimpleName() {
        return getGenericInstance().getClass().getSimpleName().toLowerCase();
    }

    private int getNextId() {
        try {
            return Files.list(Paths.get(genericTypePath))
                    .mapToInt(path -> Integer.parseInt(String.valueOf(path.getFileName())))
                    .max()
                    .getAsInt()
                    + 1;
        } catch (IOException e) {
            return -1;
        }
    }

    public String getTxtRepositoryDataPath() {
        return txtRepositoryDataPath;
    }

    //    private Email getMockedEmail(int id) {
//        Email email = new Email(id);
//
//        email.setSender("whatever@gmail.com");
//        email.setReceivers(Set.of("tak@gmail.com", "nie@gmail.com"));
//        email.setSubject("Mock testing");
//        email.setMailbox("Deleted");
//        email.setMark(String.valueOf(EmailMarks.UNMARKED));
//        email.setDateTime(String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())));
//        email.setBody("How you doin'?");
//
//        return email;
//    }
}