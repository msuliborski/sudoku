package pl.comp.model.dao;

import org.junit.Test;
import pl.comp.model.exceptions.DaoException;
import pl.comp.model.logs.FileAndConsoleLoggerFactory;
import pl.comp.model.solvers.BacktrackingSudokuSolver;
import pl.comp.model.solvers.SudokuSolver;
import pl.comp.model.sudoku.SudokuBoard;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class FileSudokuBoardDaoTest {

    private static final Logger logger = FileAndConsoleLoggerFactory.getConfiguredLogger(FileSudokuBoardDaoTest.class.getName());

    @Test
    public void testCreateDaoWithNullName() {
        assertThrows(DaoException.class, () -> new FileSudokuBoardDao(null));
    }

    @Test
    public void testWriteToNonExistingFile() {
        try (FileSudokuBoardDao dao = new FileSudokuBoardDao("fileToCreate")) {
            SudokuBoard sudokuBoard = new SudokuBoard();
            assertDoesNotThrow(() -> dao.write(sudokuBoard));
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadFromNonExistingFile() {
        try (FileSudokuBoardDao dao = new FileSudokuBoardDao("nonExistingFile")) {
            assertThrows(DaoException.class, () -> dao.read());
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWriteNullBoard() {
        try (FileSudokuBoardDao dao = new FileSudokuBoardDao("nonExistingFile")) {
            assertThrows(DaoException.class, () -> dao.write(null));
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readAndWriteFileTest() {
        SudokuBoard sudokuBoard1 = new SudokuBoard();
        SudokuSolver solver = new BacktrackingSudokuSolver();
        solver.solveSudoku(sudokuBoard1);
        logger.log(Level.INFO, sudokuBoard1.toString());
        SudokuBoardDaoFactory sudokuBoardDaoFactory = new SudokuBoardDaoFactory();
        try (FileSudokuBoardDao dao = (FileSudokuBoardDao) sudokuBoardDaoFactory.getFileDao("sudoku")) {
            dao.write(sudokuBoard1);
            SudokuBoard sudokuBoard2 = dao.read();
            assertEquals(sudokuBoard1, sudokuBoard2);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
}