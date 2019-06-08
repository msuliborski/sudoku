package pl.comp.view;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.comp.model.dao.JdbcSudokuBoardDao;
import pl.comp.model.dao.SudokuBoardDaoFactory;
import pl.comp.model.exceptions.DaoException;
import pl.comp.model.exceptions.SudokuException;
import pl.comp.model.logs.FileAndConsoleLoggerFactory;
import pl.comp.model.sudoku.SudokuBoard;

import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbSaveDialogue implements Initializable {
    public TextField saveNameField;
    public Button saveDbButton;
    private final Logger logger = FileAndConsoleLoggerFactory.getConfiguredLogger(DbSaveDialogue.class.getName());
    private final ResourceBundle messagesBundle = ResourceBundle.getBundle("pl.compprog.messages");

    private Locale englishLocale = new Locale("en", "EN");
    private ResourceBundle englishBundle = ResourceBundle.getBundle("i18n.SudokuBundle", englishLocale);
    private ResourceBundle polishBundle = ResourceBundle.getBundle("i18n.SudokuBundle");
    private ResourceBundle currentBundle = englishBundle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        saveNameField.setPromptText(MainView.getInstance().getCurrentBundle().getString("save_name"));
        saveDbButton.setText(MainView.getInstance().getCurrentBundle().getString("save"));
    }

    private enum Language {ENGLISH, POLISH}

    private boolean isBoardEmpty(SudokuBoard boardToTest) {
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                if(boardToTest.getFieldValue(i, j) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void saveToDb(ActionEvent actionEvent) {
        SudokuBoardDaoFactory sudokuBoardDaoFactory = new SudokuBoardDaoFactory();
        if (!saveNameField.getText().equals("") && !isBoardEmpty(MainView.getSudokuBoard())) {
            try (JdbcSudokuBoardDao dao = (JdbcSudokuBoardDao) sudokuBoardDaoFactory.getDatabaseDao(saveNameField.getText())) {
                dao.write(MainView.getSudokuBoard());
            } catch (SudokuException aex) {
                logger.log(Level.SEVERE, messagesBundle.getString(DaoException.MISSING_FILE), aex);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Stage stage = (Stage) saveDbButton.getScene().getWindow();
            stage.close();
        }
    }
}
