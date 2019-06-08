package pl.comp.view;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import pl.comp.model.dao.JdbcSudokuBoardDao;
import pl.comp.model.dao.SudokuBoardDaoFactory;
import pl.comp.model.exceptions.DaoException;
import pl.comp.model.logs.FileAndConsoleLoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbDeleteDialogue implements Initializable {

    private final Logger logger = FileAndConsoleLoggerFactory.getConfiguredLogger(DbSaveDialogue.class.getName());

    public ComboBox dbComboBoxList;
    public Button deleteDbButton;

    private static final String BUNDLE_NAME = "interfaceLanguage";

    @SuppressWarnings("Duplicates")
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResourceBundle bundle;
        if(MainView.isEnglish)
            bundle = ResourceBundle.getBundle(BUNDLE_NAME);
        else
            bundle = ResourceBundle.getBundle(BUNDLE_NAME, new Locale("pl"));

        deleteDbButton.setText(bundle.getString("deleteButton"));

        try {
            JdbcSudokuBoardDao dao = new JdbcSudokuBoardDao("sudoku");
            List<String[]> allBoards = dao.getAllBoardsAsStrings();
            for(int i = 0; i < allBoards.size(); i++) {
                String boardName = allBoards.get(i)[0];
                String date = allBoards.get(i)[1];
                System.out.println(date + " " +boardName);
                dbComboBoxList.getItems().addAll(boardName + ' ' + date);
            }
        } catch(DaoException e) {
            logger.log(Level.SEVERE, DaoException.MISSING_FILE, e);
        }

        dbComboBoxList.setPromptText(MainView.getInstance().getCurrentBundle().getString("choose_delete"));
        deleteDbButton.setText(MainView.getInstance().getCurrentBundle().getString("deleteButton"));
    }

    public void deleteFromDb(ActionEvent actionEvent) {
        SudokuBoardDaoFactory sudokuBoardDaoFactory = new SudokuBoardDaoFactory();
        if (dbComboBoxList.getValue() != null) {
            String boardName = (String) dbComboBoxList.getValue();
            boardName = boardName.substring(0, boardName.length() - 20);
            try (JdbcSudokuBoardDao dao = (JdbcSudokuBoardDao) sudokuBoardDaoFactory.getDatabaseDao(boardName)) {
                dao.delete();
            } catch(DaoException e) {
                logger.log(Level.SEVERE, DaoException.NO_SUCH_RECORD, e);
            }

            Stage stage = (Stage) deleteDbButton.getScene().getWindow();
            stage.close();
        }
    }
}
