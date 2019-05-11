package pl.comp.model;

import java.io.IOException;

public interface Dao<T> {
    T read() throws IOException;
    void write(T obj);
}
