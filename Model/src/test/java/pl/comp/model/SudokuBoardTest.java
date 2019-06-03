package pl.comp.model;

import org.junit.Test;
import pl.comp.model.dao.FileSudokuBoardDao;
import pl.comp.model.dao.SudokuBoardDaoFactory;
import pl.comp.model.sudoku.SudokuBoard;

import java.util.logging.Logger;

import static org.junit.Assert.*;


public class SudokuBoardTest {

    @Test
    public void testIfFillBoardGeneratesCorrectDigitsLayout() {
        SudokuBoard sudoku = new SudokuBoard();

        sudoku.fillSudoku(2);

        boolean test = false;

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                for (int i = 0; i < 9; i++) {
                    if ((sudoku.getFieldValue(x, i) == sudoku.getFieldValue(x, y) && i != y) || (sudoku.getFieldValue(i, y) == sudoku.getFieldValue(x, y) && i != x))
                        test = false;
                }
                int newRow = (x / 3) * 3;
                int newColumn = (y / 3) * 3;

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (sudoku.getFieldValue(newRow + i, newColumn + j) == sudoku.getFieldValue(x, y) && newRow + i != x && newColumn + j != y)
                            test = false;
                    }
                }
                test = true;
            }
        }
        assertTrue(test);
    }

    @Test
    public void testIfTwoSubsequentCallOfFillBoardGeneratesDifferentDigitsLayout() {
        SudokuBoard sudoku = new SudokuBoard();

        StringBuilder boardstring1 = new StringBuilder();
        StringBuilder boardstring2 = new StringBuilder();

        sudoku.fillSudoku(2);
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                boardstring1.append(sudoku.getFieldValue(x, y));
            }
        }

        sudoku.fillSudoku(2);
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                boardstring2.append(sudoku.getFieldValue(x, y));
            }
        }

        assertNotEquals(boardstring1.toString(), boardstring2.toString());

    }


    private static final Logger LOGGER = Logger.getLogger("testLogger");
    @Test
    public void testIfSerializationWorks() {

        SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();

        FileSudokuBoardDao file = (FileSudokuBoardDao) factory.getFileDao("sudoku");

        SudokuBoard sudoku = new SudokuBoard(2);

        file.write(sudoku);

        SudokuBoard sudokuFromFile = file.read();

        assertEquals(sudoku, sudokuFromFile);



    }

    @Test
    public void testIfCloneWorks() {

        SudokuBoard sudokuBoard1 = new SudokuBoard(1);
        int help = sudokuBoard1.getFieldValue(0, 0);
        sudokuBoard1.setFieldValue(0, 0, 0);
        SudokuBoard sudokuBoard2 = (SudokuBoard) sudokuBoard1.clone();

        assertNotSame(sudokuBoard1, sudokuBoard2);
        assertEquals(sudokuBoard1, sudokuBoard2);


        sudokuBoard1.setFieldValue(0, 0, help);


        assertNotSame(sudokuBoard1, sudokuBoard2);
        assertNotEquals(sudokuBoard1, sudokuBoard2);
    }
}
