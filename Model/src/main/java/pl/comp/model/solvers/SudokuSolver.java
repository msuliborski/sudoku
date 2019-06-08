package pl.comp.model.solvers;

import pl.comp.model.sudoku.SudokuBoard;

public interface SudokuSolver {
    boolean solveSudoku(SudokuBoard board);
}
