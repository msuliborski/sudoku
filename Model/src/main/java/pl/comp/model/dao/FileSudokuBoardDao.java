package pl.comp.model.dao;

import pl.comp.model.exceptions.DaoException;
import pl.comp.model.sudoku.SudokuBoard;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSudokuBoardDao implements Dao<SudokuBoard>, AutoCloseable{

    private String fileName;
    private ObjectInputStream iStream;
    private ObjectOutputStream oStream;

    FileSudokuBoardDao(String name)
    {
        fileName = name;
    }

    @Override
    public SudokuBoard read() throws DaoException {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName + ".bin"))) {
            return (SudokuBoard) inputStream.readObject();
        } catch (IOException ioe) {
            throw new DaoException(DaoException.MISSING_FILE, ioe);
        } catch (ClassNotFoundException cnfe) {
            throw new DaoException(DaoException.INVALID_CAST, cnfe);
        }
    }

    @Override
    public void write(SudokuBoard obj) throws DaoException {
        if (obj == null) {
            throw new DaoException(DaoException.NULL_BOARD);
        }
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName + ".bin"))) {
            outputStream.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public final void close() throws IOException {
    }

    @Override
    public final void finalize() throws Exception {
        close();
    }
}
