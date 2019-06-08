package pl.comp.view;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import pl.comp.model.dao.JdbcSudokuBoardDao;
import pl.comp.model.dao.SudokuBoardDaoFactory;
import pl.comp.model.exceptions.DaoException;
import pl.comp.model.exceptions.SudokuException;
import pl.comp.model.logs.FileAndConsoleLoggerFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static pl.comp.model.dao.JdbcSudokuBoardDao.getAllBoardsAsStrings;


public class DbDeleteDialogue implements Initializable {
    public ComboBox dbComboBoxList;
    public Button deleteDbButton;
    private final Logger logger = FileAndConsoleLoggerFactory.getConfiguredLogger(DbSaveDialogue.class.getName());
    private final ResourceBundle messagesBundle = ResourceBundle.getBundle("pl.compprog.messages");
    List<String[]> allBoards;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            allBoards = getAllBoardsAsStrings();
        } catch(DaoException ex) {
            logger.log(Level.SEVERE, messagesBundle.getString(DaoException.OPEN_ERROR), ex);
        }

        for(int i = 0; i < allBoards.size(); i++) {
            String boardName = allBoards.get(i)[0];
            String date = allBoards.get(i)[1];
            dbComboBoxList.getItems().addAll(
                    boardName + ' ' + date
            );
        }

        dbComboBoxList.setPromptText(MainView.getInstance().getCurrentBundle().getString("choose_delete"));
        deleteDbButton.setText(MainView.getInstance().getCurrentBundle().getString("delete"));
    }

    public void deleteFromDb(ActionEvent actionEvent) {
        SudokuBoardDaoFactory sudokuBoardDaoFactory = new SudokuBoardDaoFactory();
        if (dbComboBoxList.getValue() != null) {
            String boardName = (String) dbComboBoxList.getValue();
            boardName = boardName.substring(0, boardName.length() - 20);
            try (JdbcSudokuBoardDao dao = (JdbcSudokuBoardDao) sudokuBoardDaoFactory.getDatabaseDao(boardName)) {
                dao.delete();
            } catch (DaoException | SQLException ex) {
                logger.log(Level.SEVERE, messagesBundle.getString(DaoException.OPEN_ERROR), ex);
            }catch (SudokuException aex) {
                logger.log(Level.SEVERE, messagesBundle.getString(DaoException.MISSING_FILE), aex);
            }

            Stage stage = (Stage) deleteDbButton.getScene().getWindow();
            stage.close();
        }
    }
}
