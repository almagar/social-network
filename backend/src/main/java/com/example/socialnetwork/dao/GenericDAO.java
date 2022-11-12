package com.example.socialnetwork.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.util.Streamable;

import java.util.Optional;

/**
 * Generic data access object for other DAOs to extend from.
 * @param <T> the entity class.
 * @param <ID> the primary key type of the entity.
 */
@NoRepositoryBean
interface GenericDAO<T, ID> extends Repository<T, ID> {
    /**
     * Persist the entity.
     * @param entity the entity to persist.
     * @return the object that was persisted.
     * @param <S>
     */
    <S extends T> S save(S entity);

    /**
     * Find the entity by its primary key.
     * @param id the primary key.
     * @return an {@link Optional<T>} of the found entity.
     */
    Optional<T> findById(ID id);

    /**
     * Find all entities of {@link T}.
     * @return a {@link Streamable<T>} of the found entities.
     */
    Streamable<T> findAll();

    /**
     * A page of entities.
     * @param pageable the page information.
     * @return the page of entities.
     */
    Page<T> findAll(Pageable pageable);

    /**
     * The count of entities.
     * @return the count.
     */
    long count();

    /**
     * Delete the entity.
     * @param entity the entity to delete.
     */
    void delete(T entity);

    /**
     * Checks if an entity exists or not.
     * @param id the entity to check for.
     * @return true if the entity exists, false otherwise.
     */
    boolean existsById(ID id);
}
