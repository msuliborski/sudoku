package sudoku;

import java.io.*;

public class FileSudokuBoardDao implements Dao<SudokuBoard>, AutoCloseable{

    private String fileName;
    private ObjectInputStream iStream;
    private ObjectOutputStream oStream;

    FileSudokuBoardDao(String name)
    {
        fileName = name;
    }

    @Override
    public SudokuBoard read() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName + ".bin"))) {
            return (SudokuBoard) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void write(SudokuBoard obj) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName + ".bin"))) {
            outputStream.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        System.out.println("Closing!");
    }
}
