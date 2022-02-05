package com.hariansyah.hotelbooking.api.services;

import java.util.List;

public interface CommonJDBCService<T, ID> {
    public List<T> findAll();
    public T findById(ID id);
    public Boolean save(T entity);
    public Boolean edit(T entity);
    public Boolean removeById(ID id);
}
