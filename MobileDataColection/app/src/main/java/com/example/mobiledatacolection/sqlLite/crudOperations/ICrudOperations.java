package com.example.mobiledatacolection.sqlLite.crudOperations;

import com.example.mobiledatacolection.model.FormsFill;

import java.util.List;

public interface ICrudOperations<T,W,V> {
    List<T> readAll();
    T read(W w) throws Exception;
    void write(List<T> t) throws Exception;

    int update(T status);

    void delete(T t);
}
