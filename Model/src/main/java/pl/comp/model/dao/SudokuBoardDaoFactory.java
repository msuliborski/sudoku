package pl.comp.model.dao;

import pl.comp.model.exceptions.DaoException;

public class SudokuBoardDaoFactory {

    public Dao getFileDao(String fileName) throws DaoException {
        return new FileSudokuBoardDao(fileName);
    }

    public final Dao getDatabaseDao(String name) throws DaoException {
        return new JdbcSudokuBoardDao(name);
    }
}
