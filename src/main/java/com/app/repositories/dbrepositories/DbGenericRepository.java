package com.app.repositories.dbrepositories;

import com.app.models.EntityId;
import com.app.repositories.GenericRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@Transactional
public abstract class DbGenericRepository<T extends EntityId> implements GenericRepository<T> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings({"unchecked"})
    public T get(int id) {
        return (T) entityManager.find(getGenericInstance().getClass(), id);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<T> getAll() {
        return entityManager.createQuery("SELECT t FROM " + getInstanceSimpleName() + " t").getResultList();
    }

    @Override
    public void add(T entity) {
        entityManager.persist(entity);
    }

    @Override
    public void update(T entity) {
        entityManager.merge(entity);
    }

    @Override
    public void delete(T entity) {
        entityManager.remove(entityManager.contains(entity)
                                     ? entity
                                     : entityManager.merge(entity));
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
        return getGenericInstance().getClass().getSimpleName();
    }
}
