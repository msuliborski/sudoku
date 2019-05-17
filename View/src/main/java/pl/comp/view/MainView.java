package pl.comp.view;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import pl.comp.model.BacktrackingSudokuSolver;
import pl.comp.model.SudokuBoard;

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
    private List<List<TextField>> boardTextFields = new ArrayList<>();

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
                if(boardTextFields.get(y).get(x).isEditable()) sudokuBoard.setFieldDefault(x, y, false);
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
        verifyButton.setText("VERIFY");
        verifyButton.setTextFill(Color.BLACK);
    }

    public void verify(ActionEvent actionEvent) {
        if (sudokuBoard != null) {
            updateSudokuBoard();
            if (sudokuBoard.verify() && !sudokuBoard.areThereZeros()) {
                verifyButton.setText("CORRECT!");
                verifyButton.setTextFill(Color.GREEN);
            } else if (sudokuBoard.verify()) {
                verifyButton.setText("NO LOGIC ERRORS!");
                verifyButton.setTextFill(Color.BLUE);
            } else {
                verifyButton.setText("WRONG!");
                verifyButton.setTextFill(Color.RED);
            }
        } else {
            verifyButton.setText("START NEW GAME FROM THE MENU ABOVE!");
            verifyButton.setTextFill(Color.RED);
        }
    }
}
