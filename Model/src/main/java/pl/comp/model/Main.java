package pl.comp.model;

import pl.comp.model.dao.SudokuBoardDaoFactory;
import pl.comp.model.exceptions.SudokuException;
import pl.comp.model.logs.FileAndConsoleLoggerFactory;
import pl.comp.model.solvers.BacktrackingSudokuSolver;
import pl.comp.model.solvers.SudokuSolver;
import pl.comp.model.sudoku.SudokuBoard;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {

    private static final Logger logger = FileAndConsoleLoggerFactory.getConfiguredLogger(Main.class.getName());

    public static void main(final String[] args) {

        SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();

        SudokuBoard sudoku = new SudokuBoard();

        sudoku.fillSudoku(2);



        logger.log(Level.INFO, "Sudoku to solve:");
        logger.log(Level.INFO, sudoku.toString());

        logger.log(Level.INFO, "Now we are solving sudoku...");
        SudokuSolver solver = new BacktrackingSudokuSolver();
        solver.solveSudoku(sudoku);

        logger.log(Level.INFO, "Here is solved sudoku:");
        logger.log(Level.INFO, sudoku.toString());

    }


}
