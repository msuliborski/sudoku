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

import static java.util.logging.Level.SEVERE;

public class JdbcSudokuBoardDao implements Dao<SudokuBoard>, AutoCloseable {

    private static final Logger logger = FileAndConsoleLoggerFactory.getConfiguredLogger(JdbcSudokuBoardDao.class.getName());
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:SudokuBoards.db";

    static {
        try {
            Class.forName(DRIVER);
        } catch(ClassNotFoundException cnfex) {
//            logger.log(SEVERE, getDaoMessage(DaoException.NO_JDBC_DRIVER), cnfex);
        }
    }
    private static final String CREATE_BOARDS_TABLE = "CREATE TABLE IF NOT EXISTS boards (" +
            "[boardName] VARCHAR(255)," +
            "[creationDate] VARCHAR(255)" +
            ")";
    private static final String CREATE_FIELDS_TABLE = "CREATE TABLE IF NOT EXISTS fields (" +
            "[x] INTEGER," +
            "[y] INTEGER," +
            "[value] INTEGER," +
            "[isDefault] BOOLEAN," +
            "[boardName] INTEGER" +
            ")";

    private static final String DROP_TABLE_BOARDS = "DROP TABLE IF EXISTS boards";
    private static final String DROP_TABLE_FIELDS = "DROP TABLE IF EXISTS fields";
    private static final String READ_ALL_BOARDS = "SELECT * FROM boards";
    private static final String READ_ALL_FIELDS = "SELECT * FROM fields";
    private static final String READ_QUERY_FIELD = "SELECT * FROM fields WHERE [boardName]=?";
    private static final String WRITE_QUERY_BOARD = "INSERT INTO boards([boardName], [creationDate]) VALUES(?, strftime('%d/%m/%Y %H:%M:%S', 'now', 'localtime'))";
    private static final String WRITE_QUERY_FIELD = "INSERT INTO fields([x], [y], [value], [isDefault], [boardName]) VALUES(?, ?, ?, ?, ?)";
    private static final String DELETE_QUERY_BOARD = "DELETE FROM boards WHERE [boardName]=?";
    private static final String DELETE_ALL_BOARD = "DELETE FROM boards";
    private static final String DELETE_QUERY_FIELDS = "DELETE FROM fields WHERE [boardName]=?";
    private static final String DELETE_ALL_FIELDS = "DELETE FROM fields";

    private Connection conn;
    private Statement stat;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private String boardName;


    public JdbcSudokuBoardDao(String name) throws SudokuException {

        if (name == null) {
            throw new DaoException(DaoException.NULL_NAME);
        }

        this.boardName = name;

        try {
            conn = DriverManager.getConnection(DB_URL);
            stat = conn.createStatement();
            stat.execute(CREATE_BOARDS_TABLE);
            stat.execute(CREATE_FIELDS_TABLE);
        } catch (SQLException se) {
//            throw new DaoException(DaoException.SQL_ERROR, se);
            System.out.println(se.toString() + "awdwadwadwad");
        }


    }

    void resetTables() {
        try {
            stat.execute(DROP_TABLE_FIELDS);
            stat.execute(DROP_TABLE_BOARDS);
            stat.execute(CREATE_BOARDS_TABLE);
            stat.execute(CREATE_FIELDS_TABLE);
            stat.execute(DELETE_ALL_BOARD);
            stat.execute(DELETE_ALL_FIELDS);
        } catch (SQLException se) {
//            throw new DaoException(DaoException.SQL_ERROR, se);
            System.out.println(se.toString() + "awdwadwadwad");
        }
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
            System.out.println("   read ");
            SudokuBoard sudokuBoard = new SudokuBoard();
            pstmt = conn.prepareStatement(READ_QUERY_FIELD);
            pstmt.setString(1, boardName);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                //System.out.println("  read petla ");

//                System.out.println(rs.getInt(1) + "  " + rs.getInt(2) + "  " + rs.getInt(3) + "  " + rs.getInt(4) + "  ");
                int x = rs.getInt(1);
                int y = rs.getInt(2);
                sudokuBoard.setFieldValue(x, y, rs.getInt(3));
                sudokuBoard.getField(x, y).setDefault(rs.getBoolean(4));
            }
            return sudokuBoard;
        } catch (SQLException se) {
            System.out.println(se.toString() + "        read");
//            throw new DaoException(DaoException.SQL_ERROR);
        }
        return null;
    }


    @Override
    public void write(SudokuBoard sudokuBoard) throws DaoException { //add

        System.out.println("  write ");

        //System.out.println(sudokuBoard.toString());


        if (sudokuBoard == null) {
            throw new DaoException(DaoException.NULL_BOARD);
        }
        try {
            delete();

            PreparedStatement ps = conn.prepareStatement(WRITE_QUERY_BOARD);
            ps.setString(1, boardName);
            ps.execute();

//            PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM boards");
//            ResultSet rss = ps2.executeQuery();
//            while (rss.next()){
//                System.out.println("asd");
//            }



            pstmt = conn.prepareStatement(READ_ALL_BOARDS);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("  znalazlem boarda o id " + rs.getInt(1));
            }

            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                    pstmt = conn.prepareStatement(WRITE_QUERY_FIELD);
                    pstmt.setInt(1, x);
                    pstmt.setInt(2, y);
                    pstmt.setInt(3, sudokuBoard.getFieldValue(x, y));
                    pstmt.setBoolean(4, sudokuBoard.getField(x, y).isDefault());
                    pstmt.setString(5, boardName);
                    pstmt.execute();
                }
            }
            System.out.println("write end");
        } catch (SQLException se) {
            System.out.println(se.toString() + "        write");
//            throw new DaoException(DaoException.SQL_ERROR);
        }
    }

//
//    public static List<String[]> getAllBoardsAsStrings() throws DaoException {
//
//        try {
//            List<String[]> list = new ArrayList<>();
//            JdbcSudokuBoardDao jdbcSudokuBoardDao = new JdbcSudokuBoardDao("sudoku");
//            jdbcSudokuBoardDao.pstmt = jdbcSudokuBoardDao.conn.prepareStatement(READ_ALL_BOARDS);
//            jdbcSudokuBoardDao.rs = jdbcSudokuBoardDao.pstmt.executeQuery();
//            while (jdbcSudokuBoardDao.rs.next()) {
//                System.out.println("jedrek: " + jdbcSudokuBoardDao.rs.getString(1) + "  " + jdbcSudokuBoardDao.rs.getString(2));
//                String[] array = new String[2];
//                array[0] = jdbcSudokuBoardDao.rs.getString(1);
//                array[1] = jdbcSudokuBoardDao.rs.getString(2);
//                list.add(array);
//            }
//            return Collections.unmodifiableList(list);
//        } catch (SQLException | SudokuException e) {
//            //System.out.println(e.toString()+"        getall");
////            throw new DaoException(DaoException.SQL_ERROR);
//        }
//        return null;
//    }

    //
    public List<String[]> getAllBoardsAsStrings() {
        try {
            List<String[]> list = new ArrayList<>();
            pstmt = conn.prepareStatement(READ_ALL_BOARDS);
            rs = pstmt.executeQuery();
            while (rs.next()) {
//                System.out.println("jedrek: " + rs.getString(1) + "   " + rs.getString(2) + "   " + rs.getString(3));
                String[] array = new String[2];
                array[0] = rs.getString(1);
                array[1] = rs.getString(2);
                list.add(array);
            }
            return Collections.unmodifiableList(list);
        } catch (SQLException e) {
            System.out.println(e.toString() + "        getall");
//            throw new DaoException(DaoException.SQL_ERROR);
        }
        return null;
    }


    public void delete() throws DaoException {

        //System.out.println("  delete ");
        try {
            pstmt = conn.prepareStatement(DELETE_QUERY_BOARD);
            pstmt.setString(1, boardName);
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement(DELETE_QUERY_FIELDS);
            pstmt.setString(1, boardName);
            pstmt.executeUpdate();

        } catch (SQLException se) {

            System.out.println(se.toString() + "        delete");
//            throw new DaoException(DaoException.SQL_ERROR);
        }
    }
}

