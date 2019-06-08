package pl.comp.view;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.comp.model.dao.JdbcSudokuBoardDao;
import pl.comp.model.exceptions.SudokuException;
import pl.comp.model.logs.FileAndConsoleLoggerFactory;
import pl.comp.model.sudoku.SudokuBoard;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class DbSaveDialogue implements Initializable {

    private final Logger logger = FileAndConsoleLoggerFactory.getConfiguredLogger(DbSaveDialogue.class.getName());

    public TextField saveNameField;
    public Button saveDbButton;

    private static final String BUNDLE_NAME = "interfaceLanguage";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResourceBundle bundle;
        if(MainView.isEnglish)
            bundle = ResourceBundle.getBundle(BUNDLE_NAME);
        else
            bundle = ResourceBundle.getBundle(BUNDLE_NAME, new Locale("pl"));

        saveNameField.setPromptText(bundle.getString("saveName"));
        saveDbButton.setText(bundle.getString("saveButton"));
    }

    public void saveToDb(ActionEvent actionEvent) throws SudokuException {
        String name = saveNameField.getText();
        if (MainView.getSudokuBoard() != null) {

            JdbcSudokuBoardDao dao = new JdbcSudokuBoardDao(name);
            dao.write(MainView.getSudokuBoard());

            Stage stage = (Stage) saveDbButton.getScene().getWindow();
            stage.close();
        }
    }
}
