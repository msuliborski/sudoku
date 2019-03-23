package LCH_MON_1015_03;

public class BacktrackingSudokuSolver implements SudokuSolver {


    @Override
    public boolean solveSudoku(SudokuBoard board) {

        if (!board.areThereZeros()) return true;

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {

                if (board.get(x, y) == 0) {
                    for (int i = 1; i < 10; i++) {
                        if (board.checkIfNumberFits(x, y, i)) {
                            board.set(x, y, i);
                            if (!solveSudoku(board)) board.set(x, y, 0);
                            else return true;
                        }
                    }
                    board.set(x, y, 0);
                    return false;
                }
            }
        }
        return false;
    }

}
