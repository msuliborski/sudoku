package pl.comp.model.dao;

import org.junit.Test;
import pl.comp.model.exceptions.DaoException;
import pl.comp.model.exceptions.SudokuException;
import pl.comp.model.logs.FileAndConsoleLoggerFactory;
import pl.comp.model.solvers.BacktrackingSudokuSolver;
import pl.comp.model.solvers.SudokuSolver;
import pl.comp.model.sudoku.SudokuBoard;

import java.io.IOException;
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
        } catch (IOException | DaoException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadFromNonExistingFile() {
        try (FileSudokuBoardDao dao = new FileSudokuBoardDao("nonExistingFile")) {
            assertThrows(DaoException.class, () -> dao.read());
        } catch (IOException | DaoException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testWriteNullBoard() {
        try (FileSudokuBoardDao dao = new FileSudokuBoardDao("nonExistingFile")) {
            assertThrows(DaoException.class, () -> dao.write(null));
        } catch (IOException | DaoException e) {
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
        } catch (DaoException | IOException e) {
            e.printStackTrace();
        }
    }

//    @Test
//    public void readAndWriteFileWithWasGeneratedTest() {
//        SudokuBoard sudokuBoard1 = new SudokuBoard();
//        SudokuSolver solver = new BacktrackingSudokuSolver();
//        solver.solveSudoku(sudokuBoard1);
//        boolean[][] wasGenerated1 = new boolean[9][9];
//        for (int i = 0; i < wasGenerated1.length; i++) {
//            for (int j = 0; j < wasGenerated1[0].length; j++) {
//                wasGenerated1[i][j] = true;
//            }
//        }
//        logger.log(Level.INFO, sudokuBoard1.toString());
//        SudokuBoardDaoFactory sudokuBoardDaoFactory = new SudokuBoardDaoFactory();
//        try (FileSudokuBoardDao dao = (FileSudokuBoardDao) sudokuBoardDaoFactory.getFileDao("sudoku.ser", wasGenerated1)) {
//            dao.write(sudokuBoard1);
//            SudokuBoard sudokuBoard2 = dao.read();
//            boolean[][] wasGenerated2 = dao.getWasGenerated();
//            assertEquals(sudokuBoard1, sudokuBoard2);
//            assertEquals(wasGenerated1.length, wasGenerated2.length);
//            for (int i = 0; i < wasGenerated1.length; i++) {
//                assertArrayEquals(wasGenerated1[i], wasGenerated2[i]);
//            }
//        } catch (IOException | DaoException e) {
//            e.printStackTrace();
//        }
//    }
}