package pl.comp.view;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import pl.comp.model.BacktrackingSudokuSolver;
import pl.comp.model.SudokuBoard;
import pl.comp.model.SudokuBoardDaoFactory;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

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
    private List<List<TextField>> boardTextFields = new ArrayList<>();
    private static boolean isEnglish = true;

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
                    if(change.getText().matches("[0-9]*") && !(change.getText().length() > 1)) {
                        return change;
                    } else {
                        return null;
                    }
                }));

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
                if (sudokuBoard.get(x, y) != 0) {
                    boardTextFields.get(x).get(y).setText(String.valueOf(sudokuBoard.get(x, y)));
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
                sudokuBoard.set(x, y, Integer.parseInt("0" + boardTextFields.get(y).get(x).getText()));
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
        if (sudokuBoard != null) {
            updateSudokuBoard();
            if (sudokuBoard.verify() && !sudokuBoard.areThereZeros()) {
                if (isEnglish)
                    verifyButton.setText("CORRECT!");
                else
                    verifyButton.setText("DOBRZE!");
                verifyButton.setTextFill(Color.GREEN);
            } else if (sudokuBoard.verify()) {
                if (isEnglish)
                    verifyButton.setText("NO LOGIC ERRORS!");
                else
                    verifyButton.setText("BRAK BŁĘDÓW!");
                verifyButton.setTextFill(Color.BLUE);
            } else {
                if (isEnglish)
                    verifyButton.setText("WRONG!");
                else
                    verifyButton.setText("ŹLE!");
                verifyButton.setTextFill(Color.RED);
            }
        } else {
            if (isEnglish)
                verifyButton.setText("START NEW GAME FROM THE MENU ABOVE!");
            else
                verifyButton.setText("ZACZNIJ NOWĄ GRĘ Z MENU NA GÓRZE!");
            verifyButton.setTextFill(Color.RED);
        }
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

    public void changeLanguage() {
        isEnglish = !isEnglish;
        updateNames();
    }

    void updateNames() {
        String text = verifyButton.getText();

        if(isEnglish) {
            newGame.setText("New Game");
            startEasy.setText("Easy");
            startMedium.setText("Medium");
            startHard.setText("Hard");
            file.setText("File");
            save.setText("Save");
            load.setText("Load");
            language.setText("Language");

            switch (text) {
                case "SPRAWDŹ!": verifyButton.setText("VERIFY!"); break;
                case "DOBRZE!": verifyButton.setText("CORRECT!"); break;
                case "BRAK BŁĘDÓW!": verifyButton.setText("NO LOGIC ERRORS!"); break;
                case "ŹLE!": verifyButton.setText("WRONG!"); break;
                case "ZACZNIJ NOWĄ GRĘ Z MENU NA GÓRZE!": verifyButton.setText("START NEW GAME FROM THE MENU ABOVE!"); break;
            }
        }
        else
        {
            newGame.setText("Nowa gra");
            startEasy.setText("Łatwa");
            startMedium.setText("Średnia");
            startHard.setText("Trudna");
            file.setText("Plik");
            save.setText("Zapisz");
            load.setText("Wczytaj");
            language.setText("Język");
            switch (text) {
                case "VERIFY!": verifyButton.setText("SPRAWDŹ!"); break;
                case "CORRECT!": verifyButton.setText("DOBRZE!"); break;
                case "NO LOGIC ERRORS!": verifyButton.setText("BRAK BŁĘDÓW!"); break;
                case "WRONG!": verifyButton.setText("ŹLE!"); break;
                case "START NEW GAME FROM THE MENU ABOVE!": verifyButton.setText("ZACZNIJ NOWĄ GRĘ Z MENU NA GÓRZE!"); break;
            }
        }
    }
}
