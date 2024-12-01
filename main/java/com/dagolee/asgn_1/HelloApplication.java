package com.dagolee.asgn_1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/dagolee/asgn_1/hello-view.fxml"));
        Parent root = fxmlLoader.load();
        root.setStyle("-fx-background-color: linear-gradient(to right, #E2E4F6, #E7C0DD, #DBAFC1);");
        Scene scene = new Scene(root, 400, 400);
        stage.setTitle("DaGoLee Library System");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
