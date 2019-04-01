package LCH_MON_1015_03;

import java.util.ArrayList;
import java.util.Random;

public class SudokuBoard {

    class Pair {
        public int x;
        public int y;

        Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private SudokuField[][] board = new SudokuField[9][9];
    private SudokuBox[] boxes = new SudokuBox[9];
    private SudokuColumn[] columns = new SudokuColumn[9];
    private SudokuRow[] rows = new SudokuRow[9];

    SudokuBoard() {
        for (int i = 0; i < 9; i++) {
            columns[i] = new SudokuColumn();
            boxes[i] = new SudokuBox();
            rows[i] = new SudokuRow();
        }
        for (int x = 0; x < 9; x++) {

            for (int y = 0; y < 9; y++) {
                board[x][y] = new SudokuField();
                board[x][y].setFieldValue(0);

                rows[y].addField(board[x][y]);
                columns[x].addField(board[x][y]);
            }
        }
        int boxNum = 0;
        for (int x = 0; x < 9; x += 3) {
            for (int y = 0; y < 9; y += 3) {

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        boxes[boxNum].addField(board[x + i][y + j]);
                    }
                }
                boxNum++;
            }
        }
    }

    public int get(int x, int y) {
        return board[x][y].getFieldValue();
    }

    public void set(int x, int y, int value) {
        board[x][y].setFieldValue(value);
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

            board[x][y].setFieldValue(0);

            list.remove(index);
        }
    }


    boolean randomSolveSudoku() {

        if (!areThereZeros()) return true;

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {

                if (board[x][y].getFieldValue() == 0) {
                    var list = new ArrayList<Integer>(10);
                    for (int i = 1; i < 10; i++)
                        list.add(i);

                    Random rand = new Random();
                    while (list.size() > 0) {
                        int index = rand.nextInt(list.size());
                        int i = list.get(index);
                        list.remove(index);
                        board[x][y].setFieldValue(i);
                        if (checkIfNumberFits()) {

                            if (!randomSolveSudoku()) board[x][y].setFieldValue(0);
                            else return true;
                        }
                    }
                    board[x][y].setFieldValue(0);
                    return false;
                }
            }
        }
        return false;
    }

    public boolean checkIfNumberFits() {
        boolean test = false;
        for (int i = 0; i < 9; i++) {
            if (columns[i].verify() && rows[i].verify() && boxes[i].verify()) test = true;
            else return false;
        }
        return test;
    }

    public boolean areThereZeros() {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (board[x][y].getFieldValue() == 0) return true;
            }
        }
        return false;
    }

    public void display() {
        System.out.println();
        System.out.println("---------------------------------");
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                System.out.print(board[x][y].getFieldValue() + " ");
                if (y % 3 == 2 && y != 8) System.out.print("| ");
            }
            System.out.println();
            if (x % 3 == 2 && x != 8) System.out.println("- - - - - - - - - - -");
        }
        System.out.println("---------------------------------");
        System.out.println();
    }
}
