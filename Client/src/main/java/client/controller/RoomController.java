package client.controller;

import client.StartClient;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.EventRepresentation;
import model.Seat;
import model.User;
import service.Service;
import utils.Observer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoomController implements Observer {
    private Service service;
    private User currentUser;
    private EventRepresentation event;


    @FXML
    private AnchorPane rootPane;

    @FXML
    Label labelUser;

    @FXML
    Label labelEvent;

    @FXML
    Button reserveButton;

    @FXML
    Button viewResButton;

    private List<Button> buttonList;

    @Override
    public void update() {
        setEvent(service.findEventForToday(LocalDate.now()));
        checkSeats();
    }

    @FXML
    public void initialize(){
        buttonList = new ArrayList<>();

        // Lookup all buttons in the root pane
        List<Button> buttons = new ArrayList<>();
        for (Node node : rootPane.lookupAll(".button")) {
            if (node instanceof Button) {
                buttons.add((Button) node);
            }
        }
        System.out.println("ROOM : " + buttons.size());


        // Filter and add only the desired buttons to the buttonList
        for (Button button : buttons) {
            // Customize the filter condition based on your requirements
            if (button.getText().matches("\\d+")) { // Matches numeric buttons
                buttonList.add(button);
            }
        }

        EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleReserve(event);
            }

        };
        reserveButton.setOnAction(event1);

        EventHandler<ActionEvent> event2 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleViewRes(event);
            }

        };
        viewResButton.setOnAction(event2);


    }

    private void handleViewRes(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartClient.class.getResource("/view/reservationsView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            ReservationsController controller = fxmlLoader.getController();
            controller.setService(service);
            controller.setCurrent(currentUser);
            service.addObserver(controller);
            controller.loadModel();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void handleReserve(ActionEvent event1){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartClient.class.getResource("/view/reserveView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            ReserveController controller = fxmlLoader.getController();
            controller.setService(service);
            controller.setCurrent(currentUser);
            controller.setEventRep(event);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    
    
    public void checkSeats(){
        System.out.println("CHECK SEATS");
        Iterable<Seat> s = service.getFreeSeatsForEvent(event);
        System.out.println(s);
        List<Seat> seats = new ArrayList<>();
        if(s!=null)
            s.forEach(x->seats.add(x));


        for (Button button : buttonList) {
            button.setStyle("-fx-background-color: #a49e9e");
        }

        for (Button button : buttonList) {
            // Customize the filter condition based on your requirements
            for(Seat seat : seats){
                if (Integer.parseInt(button.getText()) == seat.getNumber() && seat.getReservation().getUser().getId() == currentUser.getId()) { // Matches numeric buttons
                    button.setStyle("-fx-background-color: #b0e84f;");
                    continue;
                }
                if (Integer.parseInt(button.getText()) == seat.getNumber() && seat.getReservation().getUser().getId() != currentUser.getId()) { // Matches numeric buttons
                    button.setStyle("-fx-background-color: #ea5f3c;");
                    continue;
                }

            }

        }
    }


    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        labelUser.setText(currentUser.getUsername());
    }

    public EventRepresentation getEvent() {
        return event;
    }

    public void setEvent(EventRepresentation event) {
        this.event = event;
        labelEvent.setText("'"+ event.getEvent().getName() + "'");
    }
}
