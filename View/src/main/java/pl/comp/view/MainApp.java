package pl.comp.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        URL location = getClass().getResource("/pl/comp/view/MainView.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent/*, 600, 100*/);
        stage.setTitle("Sudoku Game");
        stage.setScene(scene);
        MainView controller = fxmlLoader.getController();
        controller.setStage(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}