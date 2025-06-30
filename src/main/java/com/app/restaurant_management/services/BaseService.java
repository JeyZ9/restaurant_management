package com.app.restaurant_management.services;

import com.app.restaurant_management.commons.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface BaseService<T, ID> {
    List<T> getAll() throws CustomException;
    T getById(ID id);
    T create(T entity) throws JsonProcessingException;
    T update(ID id, T entity);
    boolean delete(ID id) throws CustomException;
}
