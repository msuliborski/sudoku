package pl.comp.model.dao;

import pl.comp.model.dao.Dao;
import pl.comp.model.dao.FileSudokuBoardDao;

public class SudokuBoardDaoFactory {

    public Dao getFileDao(String fileName)
    {
        return new FileSudokuBoardDao(fileName);
    }
}
