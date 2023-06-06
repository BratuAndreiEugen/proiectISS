package client.controller;

import client.StartClient;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.EventRepresentation;
import model.User;
import service.Service;

import java.io.IOException;
import java.time.LocalDate;

public class RegisterController {
    private Service service;

    @FXML
    TextField emailField;

    @FXML
    TextField nameField;

    @FXML
    PasswordField passField;

    @FXML
    PasswordField confirmField;

    @FXML
    Button registerButton;

    @FXML
    public void initialize(){
        EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    handleRegister(event);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

        };
        registerButton.setOnAction(event1);
    }

    @FXML
    public void handleRegister(ActionEvent event) throws IOException{
        String name = nameField.getText();
        String password = passField.getText();
        String confirm = confirmField.getText();
        String email = emailField.getText();
        passField.clear();
        nameField.clear();
        confirmField.clear();
        emailField.clear();


        System.out.println(confirm);
        System.out.println(password);

        if(!confirm.equals(password)) {
            MessageAlert.showErrorMessage(null, "Password was not confirmed");
            return;
        }

        User u = null;
        try {
            u = service.register(name, email, password);
            if(u == null){
                throw new Exception("Something went wrong");
            }
            FXMLLoader fxmlLoader = new FXMLLoader(StartClient.class.getResource("/view/roomView.fxml"));
            Scene scene=new Scene(fxmlLoader.load());
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.show();
            RoomController mainController = fxmlLoader.getController();

            EventRepresentation e = service.findEventForToday(LocalDate.now());
            mainController.setCurrentUser(u);
            mainController.setService(service);
            mainController.setEvent(e);
            mainController.checkSeats();
            service.addObserver(mainController);
            //mainController.initModel();
            ((Node)(event.getSource())).getScene().getWindow().hide();


        }catch (Exception ve) {
            MessageAlert.showErrorMessage(null, ve.getMessage());
            System.err.println(ve.getMessage());
        }
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
