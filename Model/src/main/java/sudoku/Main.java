package sudoku;

public class Main {
    public static void main(final String[] args) {

        SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();

        FileSudokuBoardDao file = (FileSudokuBoardDao) factory.getFileDao("objects");

        SudokuBoard sudoku;
//
//        sudoku.fillSudoku();
//
//        System.out.println();
//        System.out.println("Sudoku to solve:");
//        System.out.println();
//
//        System.out.println(sudoku.toString());
//


        sudoku = file.read();

        System.out.println(sudoku.toString());

        System.out.println();
        System.out.println("Now we are solving sudoku...");
        System.out.println();

        SudokuSolver solver = new BacktrackingSudokuSolver();
        solver.solveSudoku(sudoku);

        System.out.println();
        System.out.println("Here is solved sudoku:");
        System.out.println();

        System.out.println(sudoku.toString());

    }
}
