package pl.comp.view;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainView implements Initializable {

    private Stage stage;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
