package pl.comp.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.adapter.JavaBeanIntegerProperty;
import javafx.beans.property.adapter.JavaBeanIntegerPropertyBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;
import pl.comp.model.BacktrackingSudokuSolver;
import pl.comp.model.SudokuBoard;
import pl.comp.model.SudokuBoardDaoFactory;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainView implements Initializable {

    private Stage stage;

    //Scene elements
    public GridPane grid;
    public Button verifyButton;
    public Menu newGame;
    public MenuItem startEasy;
    public MenuItem startMedium;
    public MenuItem startHard;
    public Menu file;
    public MenuItem save;
    public MenuItem load;
    public Menu language;
    private static final String BUNDLE_NAME = "pl.comp.model.LanguagePack";
    private List<List<TextField>> boardTextFields = new ArrayList<>();
    private SimpleIntegerProperty[][] intProp = new SimpleIntegerProperty[9][9];
    private static boolean isEnglish = true;

    int gunwo = 9;

    //private

    //Game components
    private SudokuBoard sudokuBoard;
    private BacktrackingSudokuSolver sudokuSolver = new BacktrackingSudokuSolver();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
                    if(change.getText().matches("[0-9]*") && !(change.getControlNewText().length() > 1)) {
                        return change;
                    } else {
                        return null;
                    }
                }));


                int finalX = x;
                int finalY = y;
                emptyTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    sudokuBoard.setFieldValue(finalY, finalX, Integer.parseInt(newValue));//.g.get(x).add(emptyTextField);
                    System.out.println(sudokuBoard.toString());
                });

                intProp[x][y] = new SimpleIntegerProperty();
                Bindings.bindBidirectional(emptyTextField.textProperty(), intProp[x][y], new NumberStringConverter());


                boardTextFields.get(x).add(emptyTextField);
                grid.add(emptyTextField, x, y);
            }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void updateGridView() {
        for (int x = 0; x < 9; x++)
            for (int y = 0; y < 9; y++) {
                if (sudokuBoard.getFieldValue(x, y) != 0) {
                    boardTextFields.get(x).get(y).setText(String.valueOf(sudokuBoard.getFieldValue(x, y)));
                } else {
                    boardTextFields.get(x).get(y).setText("");
                }
                if (sudokuBoard.isFieldDefault(x, y)) {
                    boardTextFields.get(x).get(y).setStyle("-fx-text-fill: grey;");
                    boardTextFields.get(x).get(y).setEditable(false);
                }
            }
    }

    private void updateSudokuBoard() {
        for (int x = 0; x < 9; x++)
            for (int y = 0; y < 9; y++) {
                sudokuBoard.setFieldValue(x, y, Integer.parseInt("0" + boardTextFields.get(y).get(x).getText()));
                if (boardTextFields.get(y).get(x).isEditable()) sudokuBoard.setFieldDefault(x, y, false);
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
        this.updateGridView();
        if (isEnglish)
            verifyButton.setText("VERIFY");
        else
            verifyButton.setText("SPRAWDŹ");
        verifyButton.setTextFill(Color.BLACK);
    }

    public void verify(ActionEvent actionEvent) {
        intProp[1][1].set(3);

        System.out.println(sudokuBoard.toString());
//        if (sudokuBoard != null) {
//            updateSudokuBoard();
//            if (sudokuBoard.verify() && !sudokuBoard.areThereZeros()) {
//                if (isEnglish)
//                    verifyButton.setText("CORRECT!");
//                else
//                    verifyButton.setText("DOBRZE!");
//                verifyButton.setTextFill(Color.GREEN);
//            } else if (sudokuBoard.verify()) {
//                if (isEnglish)
//                    verifyButton.setText("NO LOGIC ERRORS!");
//                else
//                    verifyButton.setText("BRAK BŁĘDÓW!");
//                verifyButton.setTextFill(Color.BLUE);
//            } else {
//                if (isEnglish)
//                    verifyButton.setText("WRONG!");
//                else
//                    verifyButton.setText("ŹLE!");
//                verifyButton.setTextFill(Color.RED);
//            }
//        } else {
//            if (isEnglish)
//                verifyButton.setText("START NEW GAME FROM THE MENU ABOVE!");
//            else
//                verifyButton.setText("ZACZNIJ NOWĄ GRĘ Z MENU NA GÓRZE!");
//            verifyButton.setTextFill(Color.RED);
//        }
    }

    public void saveGame() {
        SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();
        updateSudokuBoard();
        if (sudokuBoard != null) {
            factory.getFileDao("sudoku").write(sudokuBoard);
        }
    }

    public void loadGame() throws IOException {
        SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();
        sudokuBoard = (SudokuBoard) factory.getFileDao("sudoku").read();
        updateGridView();
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
        ResourceBundle bundle;
        if(isEnglish) {
            bundle = ResourceBundle.getBundle(BUNDLE_NAME);
        }
        else {
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
}
