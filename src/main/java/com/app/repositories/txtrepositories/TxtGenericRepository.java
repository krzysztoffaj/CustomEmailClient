package com.app.repositories.txtrepositories;

import com.app.models.Email;
import com.app.models.EntityId;
import com.app.repositories.GenericRepository;

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

    public abstract void addEntity(T entity, PrintWriter writer);

    public abstract void updateEntity(T entity, PrintWriter writer);

    @Override
    public T get(int id) {
        String desiredFilePath = String.valueOf(Paths.get(genericTypePath, String.valueOf(id)));
        return castType(new File(desiredFilePath));
    }

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
    public void add(T entity) {
        if (entity.getId() == 0) {
            entity.setId(getNextId());
        }
        try (PrintWriter writer = new PrintWriter(String.valueOf(Paths.get(genericTypePath, String.valueOf(entity.getId()))))) {
            addEntity(entity, writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(T entity) {
        try (PrintWriter writer = new PrintWriter(String.valueOf(Paths.get(genericTypePath, String.valueOf(entity.getId()))))) {
            updateEntity(entity, writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(T entity) {
        try {
            Files.delete(Paths.get(genericTypePath, String.valueOf(entity.getId())));
            if (getGenericInstance() instanceof Email) {
                Files.delete(Paths.get(txtRepositoryDataPath, "email_user", String.valueOf(entity.getId())));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String getTxtRepositoryDataPath() {
        return txtRepositoryDataPath;
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

    private int getNextId() {
        try {
            return Files.list(Paths.get(genericTypePath))
                           .mapToInt(path -> Integer.parseInt(String.valueOf(path.getFileName())))
                           .max()
                           .getAsInt() + 1;
        } catch (IOException e) {
            return -1;
        }
    }

    private String getInstanceSimpleName() {
        return getGenericInstance().getClass().getSimpleName().toLowerCase();
    }
}