package com.app.repository.txtrepository;

import com.app.common.Email;
import com.app.common.EntityId;
import com.app.repository.GenericRepository;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class TxtGenericRepository<T extends EntityId> implements GenericRepository<T> {
    private final String workingDirectory = String.valueOf(Paths.get("").toAbsolutePath());
    private final String genericTypeDirectory = String.valueOf(Paths.get(workingDirectory, "txtRepositoryData", getInstanceSimpleName()));

    public abstract T castType(File file);

    @Override
    public List<T> getAll() {
        List<T> all = new ArrayList<>();
//        try {
//            Files.walk(Paths.get(genericTypeDirectory + "/inbox/"))
//                    .filter(p -> p.toString().endsWith(".txt"))
//                    .forEach(x -> all.add(getGenericInstance()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        for (T t : all) {
//            System.out.println(t);
//        }


        all.add(get(3));

        return all;
    }

    @Override
    public T get(int id) {
        File[] files = new File(genericTypeDirectory).listFiles();
        T object;
        for (File file : files) {
            if(id == Integer.parseInt(file.getName())) {
                object = castType(file);
                return object;
            }
        }

        return null;
    }

    @Override
    public T add(T item) {
        return null;
    }

    @Override
    public Iterable<T> addRange(Iterable<T> items) {
        return null;
    }

    @Override
    public T findByText(String text) {
        return null;
    }

    @Override
    public T update(T item) {
        return null;
    }

    @Override
    public void remove(T item) {

    }

    List<String> getAllLines(File file) {
        List<String> fileContent = new ArrayList<>();
        try {
            Files.lines(file.toPath()).forEach(fileContent::add);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileContent;
    }

    @SuppressWarnings("deprecated")
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
}
