package pl.comp.model.dao;

import pl.comp.model.exceptions.DaoException;
import pl.comp.model.exceptions.SudokuException;
import pl.comp.model.logs.FileAndConsoleLoggerFactory;
import pl.comp.model.sudoku.SudokuBoard;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class JdbcSudokuBoardDao implements Dao<SudokuBoard>, AutoCloseable {

    private static final Logger logger = FileAndConsoleLoggerFactory.getConfiguredLogger(JdbcSudokuBoardDao.class.getName());
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:SudokuBoards.db";

    private static final String CREATE_BOARDS_TABLE = "CREATE TABLE IF NOT EXISTS boards (" +
            "[boardID] INTEGER PRIMARY KEY," +
            "[name] VARCHAR(255)," +
            "[creationDate] VARCHAR(255)" +
            ")";
    private static final String CREATE_FIELDS_TABLE = "CREATE TABLE IF NOT EXISTS fields (" +
            "[boardID] INTEGER PRIMARY KEY AUTOINCREMENT," +
            "[x] INTEGER," +
            "[y] INTEGER," +
            "[value] INTEGER," +
            "[isDefault] BOOLEAN," +
            "FOREIGN KEY(boardID) REFERENCES boards(boardID)" +
            ")";

        private static final String READ_ALL_BOARDS = "SELECT * FROM boards";
    private static final String READ_QUERY_FIELD = "SELECT * FROM fields WHERE [boardID]=?";
    private static final String WRITE_QUERY_BOARD = "INSERT INTO boards([name], [creationDate]) VALUES(?, strftime('%d/%m/%Y %H:%M:%S', 'now', 'localtime'))";
    private static final String WRITE_QUERY_FIELD = "INSERT INTO fields([x], [y], [value], [isDefault]) VALUES(?, ?, ?, ?)";
//    private static final String DELETE_QUERY_BOARD = "DELETE FROM boards WHERE [name]=?";
//    private static final String DELETE_QUERY_FIELDS = "DELETE FROM fields WHERE [boardName]=?";

    private Connection conn;
    private Statement stat;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private int boardID;


    public JdbcSudokuBoardDao(final String name) throws SudokuException {
        if (name == null) {
            throw new DaoException(DaoException.NULL_NAME);
        }
//        if (wasGenerated == null) {
//            throw new DaoException(DaoException.NULL_BOARD);
//        }
        try {
            conn = DriverManager.getConnection(DB_URL);
            stat = conn.createStatement();
            stat.execute(CREATE_BOARDS_TABLE);
            stat = conn.createStatement();
            stat.execute(CREATE_FIELDS_TABLE);
        } catch (SQLException se) {
//            throw new DaoException(DaoException.SQL_ERROR, se);
            System.out.println(se.toString());
        }
        this.boardID = name.hashCode();
    }

    @Override
    public void close() throws SQLException {
    }

    @Override
    public final void finalize() throws Exception {
        close();
    }

    @Override
    public SudokuBoard read() throws DaoException {
        try {
            SudokuBoard sudokuBoard = new SudokuBoard();
            pstmt = conn.prepareStatement(READ_QUERY_FIELD);
            pstmt.setString(1, String.valueOf(boardID));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                int x = rs.getInt(1);
                int y = rs.getInt(2);
                sudokuBoard.setFieldValue(x, y, rs.getInt(3));
                sudokuBoard.getField(x, y).setDefault(rs.getBoolean(4));
            }
            return sudokuBoard;
        } catch (SQLException se) {
            throw new DaoException(DaoException.SQL_ERROR);
        }
    }


    @Override
    public void write(SudokuBoard sudokuBoard) throws DaoException { //add
        if (sudokuBoard == null) {
            throw new DaoException(DaoException.NULL_BOARD);
        }
        try {
//            delete();
            pstmt = conn.prepareStatement(WRITE_QUERY_BOARD);
            pstmt.setString(1, String.valueOf(boardID));
            pstmt.execute();

            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                    pstmt = conn.prepareStatement(WRITE_QUERY_FIELD);
                    pstmt.setInt(1, x);
                    pstmt.setInt(2, y);
                    pstmt.setInt(3, sudokuBoard.getFieldValue(x, y));
                    pstmt.setBoolean(4, sudokuBoard.getField(x, y).isDefault());
                    pstmt.execute();
                }
            }
        } catch (SQLException se) {
            throw new DaoException(DaoException.SQL_ERROR);
        }
    }


    public static List<String[]> getAllBoardsAsStrings() throws DaoException {
        try  {
            List<String[]> list = new ArrayList<>();
            JdbcSudokuBoardDao jdbcSudokuBoardDao = new JdbcSudokuBoardDao("temp");
            jdbcSudokuBoardDao.pstmt = jdbcSudokuBoardDao.conn.prepareStatement(READ_ALL_BOARDS);
            jdbcSudokuBoardDao.rs = jdbcSudokuBoardDao.pstmt.executeQuery();
            while (jdbcSudokuBoardDao.rs.next()) {
                String[] array = new String[2];
                array[0] = jdbcSudokuBoardDao.rs.getString(1);
                array[1] = jdbcSudokuBoardDao.rs.getString(2);
                list.add(array);
            }
            return Collections.unmodifiableList(list);
        } catch (SQLException | SudokuException e) {
            throw new DaoException(DaoException.SQL_ERROR);
        }
    }



//
//    public void delete() throws DaoException {
//        try {
//            pstmt = conn.prepareStatement(DELETE_QUERY_BOARD);
//            pstmt.setString(1, boardName);
//            pstmt.executeUpdate();
//            pstmt = conn.prepareStatement(DELETE_QUERY_FIELDS);
//            pstmt.setString(1, boardName);
//            pstmt.executeUpdate();
//
//        } catch (SQLException se) {
//            throw new DaoException(DaoException.SQL_ERROR);
//        }
//    }
}
