package com.example._3_matches_game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication1 extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication1.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        stage.setTitle("CLIENT 2");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    public static void main(String[] args) {
        launch();
    }
}