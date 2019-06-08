package pl.comp.model.dao;

import pl.comp.model.exceptions.DaoException;

public interface Dao<T> {
    T read() throws DaoException;
    void write(T obj) throws DaoException;
}
