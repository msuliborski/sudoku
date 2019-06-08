package pl.comp.model.dao;

import pl.comp.model.dao.Dao;
import pl.comp.model.dao.FileSudokuBoardDao;
import pl.comp.model.exceptions.DaoException;
import pl.comp.model.exceptions.SudokuException;

public class SudokuBoardDaoFactory {

    public Dao getFileDao(String fileName) throws DaoException {
        return new FileSudokuBoardDao(fileName);
    }

    public final Dao getDatabaseDao(final String boardName, boolean[][] wasGenerated) throws SudokuException {
        return new JdbcSudokuBoardDao(boardName);
    }
}
