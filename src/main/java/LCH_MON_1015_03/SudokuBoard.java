package LCH_MON_1015_03;

import java.util.ArrayList;
import java.util.Random;

public class SudokuBoard {

    private int[][] board;

    SudokuBoard() {
        board = new int[9][9];
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                board[x][y] = 0;
            }
        }
    }

    public int[][] getBoard()
    {
        return board;
    }

    void fillSudoku() {
        this.randomSolveSudoku();
        this.randomNumbersRemove();
    }

    private void randomNumbersRemove() {
        ArrayList<Pair> list = new ArrayList<Pair>(81);
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                list.add(new Pair(x, y));
            }
        }

        Random rand = new Random();

        for (int i = 0; i < 51; i++) {
            int index = rand.nextInt(list.size());

            int x = list.get(index).x;
            int y = list.get(index).y;

            board[x][y] = 0;

            list.remove(index);
        }
    }


    boolean randomSolveSudoku() {

        if (!areThereZeros()) return true;

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {

                if (board[x][y] == 0) {
                    var list = new ArrayList<Integer>(10);
                    for (int i = 1; i < 10; i++)
                        list.add(i);

                    Random rand = new Random();
                    while (list.size() > 0) {
                        int index = rand.nextInt(list.size());
                        int i = list.get(index);
                        list.remove(index);
                        if (checkIfNumberFits(x, y, i)) {
                            board[x][y] = i;
                            if (!randomSolveSudoku()) board[x][y] = 0;
                            else return true;
                        }
                    }
                    board[x][y] = 0;
                    return false;
                }
            }
        }
        return false;

    }

    public boolean solveSudoku() {

        if (!areThereZeros()) return true;

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {


                if (board[x][y] == 0) {

                    for (int i = 1; i < 10; i++) {
                        if (checkIfNumberFits(x, y, i)) {
                            board[x][y] = i;
                            if (!solveSudoku()) board[x][y] = 0;
                            else return true;
                        }
                    }
                    board[x][y] = 0;
                    return false;
                }
            }
        }
        return false;
    }

    public boolean checkIfNumberFits(int x, int y, int num) {
        for (int i = 0; i < board.length; i++) {
            if ((board[x][i] == num && i != y) || (board[i][y] == num && i != x)) return false;
        }
        int newRow = (x / 3) * 3;
        int newColumn = (y / 3) * 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if ((board[newRow + i][newColumn + j] == num) && newRow + i != x && newColumn + j != y)
                    return false;
            }
        }
        return true;
    }

    private boolean areThereZeros() {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (board[x][y] == 0) return true;
            }
        }
        return false;
    }

    public void display() {
        System.out.println();
        System.out.println("---------------------------------");
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                System.out.print(board[x][y] + " ");
                if (y % 3 == 2 && y != 8) System.out.print("| ");
            }
            System.out.println();
            if (x % 3 == 2 && x != 8) System.out.println("- - - - - - - - - - -");
        }
        System.out.println("---------------------------------");
        System.out.println();
    }
}
