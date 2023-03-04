package com.timeclockapp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class PopupController implements Initializable {

    @FXML
    Button exitButton;



    public void initialize(URL url, ResourceBundle resourceBundle) {
        exitButton.setOnMouseClicked(e -> ((Stage) exitButton.getScene().getWindow()).close());
    }
}
