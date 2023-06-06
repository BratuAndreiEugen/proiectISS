package client.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.EventRepresentation;
import model.User;
import service.Service;

import java.io.IOException;

public class ReserveController {

    private Service service;

    private User current;

    private EventRepresentation eventRep;


    @FXML
    TextField seatsField;

    @FXML
    Button seatsButton;

    @FXML
    public void initialize(){
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    handleReserve(event);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

        };
        seatsButton.setOnAction(event);
    }

    public void handleReserve(ActionEvent event) throws IOException{
        String list = seatsField.getText();
        seatsField.clear();
        try {
            service.reserveSeats(current, eventRep, list);
            ((Stage) ((Node) (event.getSource())).getScene().getWindow() ).close();
        }catch (Exception e){
            System.out.println("UI : " + e.getMessage());
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public User getCurrent() {
        return current;
    }

    public void setCurrent(User current) {
        this.current = current;
    }

    public EventRepresentation getEventRep() {
        return eventRep;
    }

    public void setEventRep(EventRepresentation eventRep) {
        this.eventRep = eventRep;
    }

}
