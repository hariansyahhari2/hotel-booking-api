package com.hariansyah.hotelbooking.api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CommonService<T, ID> {

    public T save(T entity);

    public T removeById(ID id);

    public T findById(ID id);

    public List<T> findAll();

    public Page<T> findAll(T search, int page, int size, Sort.Direction direction);
}
