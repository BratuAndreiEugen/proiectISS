package client.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.EventRepresentation;
import service.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddEventController {
    private Service service;

    @FXML
    TextField nameField;

    @FXML
    Button confirmButton;

    @FXML
    DatePicker datePicker;

    @FXML
    public void initialize(){
        EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleAdd(event);
            }

        };
        confirmButton.setOnAction(event1);
    }

    private void handleAdd(ActionEvent event1) {
        LocalDate selectedDate = datePicker.getValue();
        String name = nameField.getText();
        nameField.clear();

        if(selectedDate == null || name == null){
            MessageAlert.showErrorMessage(null, "You haven't completed all the fields");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = selectedDate.format(formatter);

        EventRepresentation ev = service.findEventForToday(selectedDate);
        if(ev != null){
            MessageAlert.showErrorMessage(null, "There is already an event on this day");
            return;
        }
        date = date.concat(" 13:00");



        try {
            service.addEventRepresentation(name, date);
        }catch (Exception e){
            MessageAlert.showErrorMessage(null, "We don't organize this event/play");
            return;
        }

    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
