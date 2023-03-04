package com.timeclockapp;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.ResourceBundle;

public class UIController{ //implements Initializable {
    @FXML
    Button exitButton, clockInButton, clockOutButton, toLunchButton, fromLunchButton, testButton, exitPopupButton;
    @FXML
    AnchorPane windowPane;
    @FXML
    Text dailyHoursText, weeklyHoursText, someText, weekOfText;
    @FXML
    DatePicker datePicker;
    @FXML
    TableView tableView;
    @FXML
    TableColumn typeColumn, timeColumn, dateColumn;
    Week week = new Week();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("M/D/yyy");

    public void TestButtonHandler(){
        final Stage dialog = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(TimeClock.class.getResource("Popup.fxml"));
        Scene dialogScene=null;
        try {
            dialogScene = new Scene(fxmlLoader.load(), 600, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void DatePickerHandler(){
        weekOfText.setText(week.currentWeek);
        typeColumn.setCellValueFactory(new PropertyValueFactory<Punch, String>("type"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<Punch, String>("time"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Punch, String>("date"));
        week.currentWeek= week.findCurrentWeek(datePicker.getValue().atStartOfDay());
        week.currentDay=datePicker.getValue().getDayOfWeek();
        weekOfText.setText(week.currentWeek);
        tableView.setItems(week.days.get(week.currentDay.toString()).punches);    }

    public void ClockHandler(Event event){
        week.performPunch(((Button) event.getSource()).getId());
        if(((Button) event.getSource()).getId().compareTo("clockOutButton")==0) {
            dailyHoursText.setText(String.valueOf(week.days.get(week.currentDay.toString()).hoursWorked));
            weeklyHoursText.setText(String.valueOf(week.weeklyHours));
            System.out.println(week.currentDay.toString());
        }
        tableView.setItems(week.days.get(week.currentDay.toString()).punches);
    }

    public void LoadButtonHandler(){
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.setInitialDirectory(new File("C:\\Users\\phill\\Desktop\\Timeclock"));
        File file = fileChooser.showOpenDialog(stage);
        if(file!=null) {
            ArrayList<String> tokens = new ArrayList(List.of(FileMethods.ReadFile(file.toString()).split("\n")));
            week.loadWeek(tokens);

        }
        else
            System.out.println("No file selected...");
    }
    public void ExitButtonHandler(Event e){
        if(((Button)e.getSource()).getId().compareTo("exitButton")==0)
            Platform.exit();
        else
            ((Stage) exitPopupButton.getScene().getWindow()).close();
    }

    /*
    public void initialize(URL url, ResourceBundle resourceBundle){
        exitButton.setOnMouseClicked(e -> Platform.exit());
        weekOfText.setText(week.currentWeek);
        datePicker.getEditor().setText(LocalDateTime.now().format(dateTimeFormatter));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Punch, String>("type"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<Punch, String>("time"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Punch, String>("date"));
    }*/
}
