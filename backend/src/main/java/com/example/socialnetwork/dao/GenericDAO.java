package com.example.socialnetwork.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.util.Streamable;

import java.util.Optional;

@NoRepositoryBean
public interface GenericDAO<T, ID> extends Repository<T, ID> {
    <S extends T> S save(S entity);
    Optional<T> findById(ID id);
    Streamable<T> findAll();
    Page<T> findAll(Pageable pageable);
    long count();
    void delete(T entity);
    boolean existsById(ID id);
}
