package pl.comp.model.dao;

import pl.comp.model.exceptions.DaoException;

public class SudokuBoardDaoFactory {

    public Dao getFileDao(final String fileName) throws DaoException {
        return new FileSudokuBoardDao(fileName);
    }

    public final Dao getDatabaseDao(final String name) throws DaoException {
        return new JdbcSudokuBoardDao(name);
    }
}
