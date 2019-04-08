package sudoku;

import java.util.*;

public class SudokuBoard {

    class Pair {
        public int x;
        public int y;

        Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private List<List<SudokuField>> board = new ArrayList<>(9);
    private List<Verifier> verifier = new ArrayList<>(27);

    SudokuBoard() {

        var boxes = new ArrayList<SudokuBox>(9);
        var columns = new ArrayList<SudokuColumn>(9);
        var rows = new ArrayList<SudokuRow>(9);

        for (int i = 0; i < 9; i++) {
            board.add(new ArrayList<>(9));
            //fixed.add(Array.asList(new SudokuField()[9]))
            columns.add(new SudokuColumn());
            boxes.add(new SudokuBox());
            rows.add(new SudokuRow());
        }
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                board.get(x).add(new SudokuField());

                rows.get(y).addField(board.get(x).get(y));
                columns.get(x).addField(board.get(x).get(y));
            }
        }
        int boxNum = 0;
        for (int x = 0; x < 9; x += 3) {
            for (int y = 0; y < 9; y += 3) {

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        boxes.get(boxNum).addField(board.get(x + i).get(y + j));
                    }
                }
                boxNum++;
            }
        }
        verifier.addAll(boxes);
        verifier.addAll(columns);
        verifier.addAll(rows);

        for (int i = 0; i < 9; i++) {
            board.set(i, Collections.unmodifiableList(board.get(i)));
        }
        board = Collections.unmodifiableList(board);

        verifier = Collections.unmodifiableList(verifier);

        //verifier.add(new SudokuColumn());
        //board.get(1).add(new SudokuField());
    }

    public int get(int x, int y) {
        return board.get(x).get(y).getFieldValue();
    }

    public void set(int x, int y, int value) {
        board.get(x).get(y).setFieldValue(value);
    }

    void fillSudoku() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        solver.solveSudoku(this);
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

            board.get(x).get(y).setFieldValue(0);

            list.remove(index);
        }
    }


    public boolean verify() {
        for (Verifier v : verifier) {
            if (!v.verify()) {
                return false;
            }
        }

        return true;
    }

    public boolean areThereZeros() {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (board.get(x).get(y).getFieldValue() == 0) {
                    return true;
                }
            }
        }

        return false;
    }


    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("---------------------------------");
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                sb.append(board.get(x).get(y).getFieldValue()).append(" ");
                if (y % 3 == 2 && y != 8) {
                    sb.append("| ");
                }
            }
            System.out.println();
            if (x % 3 == 2 && x != 8) {
                sb.append("- - - - - - - - - - -\n");
            }
        }
        sb.append("---------------------------------");
        sb.append("\n");
        return sb.toString();
    }

    public void display() {
        System.out.println();
        System.out.println("---------------------------------");
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                System.out.print(board.get(x).get(y).getFieldValue() + " ");
                if (y % 3 == 2 && y != 8) {
                    System.out.print("| ");
                }
            }
            System.out.println();
            if (x % 3 == 2 && x != 8) {
                System.out.println("- - - - - - - - - - -");
            }
        }
        System.out.println("---------------------------------");
        System.out.println();
    }
}
