package com.timeclockapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TimeClock extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage){
        FXMLLoader fxmlLoader = new FXMLLoader(TimeClock.class.getResource("GUI.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 850, 700);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Time Clock");
        stage.setScene(scene);
        stage.show();
    }
}
