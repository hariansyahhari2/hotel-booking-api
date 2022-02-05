package com.hariansyah.hotelbooking.api.repositories;

import java.util.List;

public interface CommonJDBCRepository<T, ID> {
    public List<T> findAll();
    public T findById(Integer id);
    public Boolean save(T entity);
    public Boolean edit(T entity);
    public Boolean remove (ID id);
}
