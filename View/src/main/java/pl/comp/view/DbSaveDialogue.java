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
    private static final String BUNDLE_NAME = "interfaceLanguage";
    private ResourceBundle bundle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(MainView.isEnglish)
            bundle = ResourceBundle.getBundle(BUNDLE_NAME);
        else
            bundle = ResourceBundle.getBundle(BUNDLE_NAME, new Locale("pl"));
        saveNameField.setPromptText(bundle.getString("saveName"));
        saveDbButton.setText(bundle.getString("saveButton"));
    }

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

    public void saveToDb(ActionEvent actionEvent) throws SudokuException {
        String name = saveNameField.getText();
        SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();
        if (MainView.getSudokuBoard() != null) {
            factory.getDatabaseDao(name).write(MainView.getSudokuBoard());
            MainView.getInstance().getLoadableBoards();
        }
    }
}
