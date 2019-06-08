package pl.comp.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.util.converter.NumberStringConverter;
import pl.comp.model.dao.JdbcSudokuBoardDao;
import pl.comp.model.exceptions.DaoException;
import pl.comp.model.exceptions.SudokuException;
import pl.comp.model.sudoku.SudokuBoard;
import pl.comp.model.dao.SudokuBoardDaoFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainView implements Initializable {

    private static SudokuBoard sudokuBoard;
    private Stage stage;

    public GridPane grid;
    public Button verifyButton;
    public Menu newGame;
    public MenuItem startEasy;
    public MenuItem startMedium;
    public MenuItem startHard;
    public Menu file;
    public MenuItem save;
    public MenuItem load;
    public Menu databse;
    public MenuItem dbsave;
    public Menu dbload;
    public Menu language;
    private static final String BUNDLE_NAME = "interfaceLanguage";
    private List<List<TextField>> boardTextFields = new ArrayList<>();
    private SimpleIntegerProperty[][] boardIntegerProperties = new SimpleIntegerProperty[9][9];
    private static boolean isEnglish = true;
    ResourceBundle bundle;



    private static MainView instance;

    public MainView() {
        if (instance == null) {
            instance = this;
        } else {
            throw new RuntimeException("MainView already created!");
        }
    }

    public static SudokuBoard getSudokuBoard() {
        return sudokuBoard;
    }

    public void setSudokuBoard(SudokuBoard sb) {
        sudokuBoard = sb;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        bundle = ResourceBundle.getBundle(BUNDLE_NAME);

        for (int i = 0; i < 9; i++)
            boardTextFields.add(new ArrayList<>());

        sudokuBoard = new SudokuBoard();

        for (int x = 0; x < 9; x++)
            for (int y = 0; y < 9; y++) {
                TextField emptyTextField = new TextField();
                emptyTextField.setText("");
                emptyTextField.setEditable(true);
                emptyTextField.setAlignment(Pos.CENTER);
                emptyTextField.setPrefHeight(100);
                emptyTextField.setPrefWidth(100);
                emptyTextField.setFont(Font.font("Verdana", 36));
                emptyTextField.setTextFormatter(new TextFormatter<String>((TextFormatter.Change change) -> {
                    if (change.getText().matches("[0-9]*") && !(change.getControlNewText().length() > 1)) {
                        return change;
                    } else {
                        return null;
                    }
                }));


                int finalX = x;
                int finalY = y;


                boardIntegerProperties[x][y] = new SimpleIntegerProperty();
                Bindings.bindBidirectional(emptyTextField.textProperty(), boardIntegerProperties[x][y], new NumberStringConverter());

                emptyTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue.equals("")) {
                        sudokuBoard.setFieldValue(finalY, finalX, 0);
                    } else {
                        sudokuBoard.setFieldValue(finalY, finalX, Integer.parseInt(newValue));
                    }
                });

                int finalX1 = x;
                int finalY1 = y;
                boardIntegerProperties[x][y].addListener((observable, oldValue, newValue) -> {
                    System.out.println(newValue.intValue() + "");
                    if (newValue.intValue() == 0) {
                        emptyTextField.setText("");
                    } else {
                        emptyTextField.setText(String.valueOf(newValue));
                    }
                    if (sudokuBoard.isFieldDefault(finalY1, finalX1)) {
                        emptyTextField.setStyle("-fx-text-fill: grey;");
                        emptyTextField.setEditable(false);
                    } else {
                        emptyTextField.setStyle("-fx-text-fill: black;");
                        emptyTextField.setEditable(true);
                    }
                });

                boardTextFields.get(x).add(emptyTextField);
                grid.add(emptyTextField, x, y);
            }
        for (int x = 0; x < 9; x++)
            for (int y = 0; y < 9; y++) {
                boardTextFields.get(x).get(y).setText("");
            }
        getLoadableBoards();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    public static MainView getInstance() {
        return instance;
    }

    public void getLoadableBoards() {
        try {
            List<String[]> loadables = JdbcSudokuBoardDao.getAllBoardsAsStrings();
            if (loadables.size() > 0) {
                dbload.getItems().removeAll();
                for (String[] subMenu : loadables) {
                    dbload.getItems().add(new MenuItem(subMenu[0] + " " + subMenu[1]));
                }
            }

        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    private void reinitializeBoard() {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                boardIntegerProperties[y][x].set(sudokuBoard.getFieldValue(x, y));
            }
        }
    }


    public void startGameEasy(ActionEvent actionEvent) {
        startGame(1);
    }

    public void startGameMedium(ActionEvent actionEvent) {
        startGame(2);
    }

    public void startGameHard(ActionEvent actionEvent) {
        startGame(3);
    }

    public void startGame(int difficulty) {
        sudokuBoard = new SudokuBoard(difficulty);
        //this.updateGridView();
        reinitializeBoard();
        verifyButton.setText(bundle.getString("verifyButton"));
        verifyButton.setTextFill(Color.BLACK);
    }

    public void verify(ActionEvent actionEvent) {
        if (sudokuBoard != null) {
            if (sudokuBoard.verify() && !sudokuBoard.areThereZeros()) {
                verifyButton.setText(bundle.getString("correct"));
                verifyButton.setTextFill(Color.GREEN);
            } else if (sudokuBoard.verify()) {
                verifyButton.setText(bundle.getString("nologicerrors"));
                verifyButton.setTextFill(Color.BLUE);
            } else {

                verifyButton.setText(bundle.getString("wrong"));
                verifyButton.setTextFill(Color.RED);
            }
        } else {
            verifyButton.setText(bundle.getString("newgameb"));
            verifyButton.setTextFill(Color.RED);
        }
    }

    public void saveGame() throws DaoException {
        SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();
        if (sudokuBoard != null) {
            factory.getFileDao("sudoku").write(sudokuBoard);
        }
    }

    public void loadGame() throws DaoException {
        SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();
        sudokuBoard = (SudokuBoard) factory.getFileDao("sudoku").read();
        reinitializeBoard();
    }

    public void dbsaveGame() throws SudokuException {
        //zapytaj o nazwe
        String name = "default";
        SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();
        if (sudokuBoard != null) {
            factory.getDatabaseDao(name).write(sudokuBoard);
        }
    }

    public void dbloadGame() throws SudokuException {
        SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();
        sudokuBoard = (SudokuBoard) factory.getFileDao("sudoku").read();
        reinitializeBoard();
    }

    public void setPl() {
        isEnglish = false;
        updateNames();
    }

    public void setEng() {
        isEnglish = true;
        updateNames();
    }

    void updateNames() {

        if (isEnglish) {
            bundle = ResourceBundle.getBundle(BUNDLE_NAME);
        } else {
            bundle = ResourceBundle.getBundle(BUNDLE_NAME, new Locale("pl"));
        }
        newGame.setText(bundle.getString("newGame"));
        startEasy.setText(bundle.getString("startEasy"));
        startMedium.setText(bundle.getString("startMedium"));
        startHard.setText(bundle.getString("startHard"));
        file.setText(bundle.getString("file"));
        save.setText(bundle.getString("save"));
        load.setText(bundle.getString("load"));
        language.setText(bundle.getString("language"));
        verifyButton.setText(bundle.getString("verifyButton"));
    }

    public void loadFromDbAction(ActionEvent actionEvent) throws IOException {
        URL location = getClass().getResource("/pl/compprog/gui/DbLoadDialogue.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        fxmlLoader.setResources(ResourceBundle.getBundle("i18n.SudokuBundle", new Locale("en", "EN")));
        Parent root = fxmlLoader.load();

        Scene secondScene = new Scene(root);
        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle(bundle.getString("db_load_dialogue"));
        newWindow.setScene(secondScene);
        // Specifies the modality for new window.
        newWindow.initModality(Modality.WINDOW_MODAL);
        // Specifies the owner Window (parent) for new window
        newWindow.initOwner(stage);
        // Set position of second window, related to primary window.
        newWindow.setX(stage.getX() + 50);
        newWindow.setY(stage.getY() + 200);
        newWindow.show();
    }

    @SuppressWarnings("Duplicates")
    public void saveToDbAction(ActionEvent actionEvent) throws IOException {
        URL location = getClass().getResource("/pl/compprog/gui/DbSaveDialogue.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        fxmlLoader.setResources(ResourceBundle.getBundle("i18n.SudokuBundle", new Locale("en", "EN")));
        Parent root = fxmlLoader.load();

        Scene secondScene = new Scene(root);
        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle(bundle.getString("db_save_dialogue"));
        newWindow.setScene(secondScene);
        // Specifies the modality for new window.
        newWindow.initModality(Modality.WINDOW_MODAL);
        // Specifies the owner Window (parent) for new window
        newWindow.initOwner(stage);
        // Set position of second window, related to primary window.
        newWindow.setX(stage.getX() + 50);
        newWindow.setY(stage.getY() + 200);
        newWindow.show();
    }

    @SuppressWarnings("Duplicates")
    public void deleteFromDbAction(ActionEvent actionEvent) throws IOException {
        URL location = getClass().getResource("/pl/compprog/gui/DbDeleteDialogue.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        fxmlLoader.setResources(ResourceBundle.getBundle("i18n.SudokuBundle", new Locale("en", "EN")));
        Parent root = fxmlLoader.load();
        Scene secondScene = new Scene(root);
        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle(bundle.getString("db_delete_dialogue"));
        newWindow.setScene(secondScene);
        // Specifies the modality for new window.
        newWindow.initModality(Modality.WINDOW_MODAL);
        // Specifies the owner Window (parent) for new window
        newWindow.initOwner(stage);
        // Set position of second window, related to primary window.
        newWindow.setX(stage.getX() + 50);
        newWindow.setY(stage.getY() + 200);
        newWindow.show();
    }



    public ResourceBundle getCurrentBundle() {
        return bundle;
    }
}
