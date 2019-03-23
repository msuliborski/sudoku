package LCH_MON_1015_03;

import static org.junit.jupiter.api.Assertions.*;


public class SudokuBoardTest {


    SudokuBoardTest() {

    }

    @org.junit.jupiter.api.Test
    void testIfFillBoardGeneratesCorrectDigitsLayout() {
        SudokuBoard sudoku = new SudokuBoard();

        sudoku.fillSudoku();

        boolean test = false;

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                for (int i = 0; i < 9; i++) {
                    if ((sudoku.get(x, i) == sudoku.get(x, y) && i != y) || (sudoku.get(i, y) == sudoku.get(x, y) && i != x))
                        test = false;
                }
                int newRow = (x / 3) * 3;
                int newColumn = (y / 3) * 3;

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (sudoku.get(newRow + i, newColumn + j) == sudoku.get(x, y) && newRow + i != x && newColumn + j != y)
                            test = false;
                    }
                }
                test = true;
            }
        }
        assertTrue(test);
    }

    @org.junit.jupiter.api.Test
    void testIfTwoSubsequentCallOfFillBoardGeneratesDifferentDigitsLayout() {
        SudokuBoard sudoku = new SudokuBoard();

        StringBuilder boardstring1 = new StringBuilder();
        StringBuilder boardstring2 = new StringBuilder();

        sudoku.fillSudoku();
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                boardstring1.append(sudoku.get(x, y));
            }
        }

        sudoku.fillSudoku();
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                boardstring2.append(sudoku.get(x, y));
            }
        }

        assertNotEquals(boardstring1.toString(), boardstring2.toString());

    }


    /*
    * fillBoard generates correct digits layout (check all rows, columns and boxes)
two subsequent calls of fillBoard generates different digits layout on the board
*/
}
