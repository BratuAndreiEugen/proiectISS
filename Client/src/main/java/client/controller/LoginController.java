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


public class LoginController {
    private Service service;

    @FXML
    TextField nameField;

    @FXML
    PasswordField passwordField;

    @FXML
    Button loginButton;

    @FXML
    Button registerButton;

    @FXML
    public void initialize()
    {
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    handleLogin(event);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

        };
        loginButton.setOnAction(event);

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
    public void handleLogin(ActionEvent event) throws IOException
    {
        String data = nameField.getText();
        String password = passwordField.getText();
        passwordField.clear();
        nameField.clear();
        System.out.println(data);
        System.out.println(password);
        User u = null;
        try {
            u = service.login(data, password);
            if(u == null){
                MessageAlert.showErrorMessage(null, "Username or password is incorrect");
                return;
            }
            if(u.getAdmin() == 0) {
                FXMLLoader fxmlLoader = new FXMLLoader(StartClient.class.getResource("/view/roomView.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
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
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }else {
                FXMLLoader fxmlLoader = new FXMLLoader(StartClient.class.getResource("/view/clientView.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                ClientsController mainController = fxmlLoader.getController();
                mainController.setCurrentAdmin(u);
                mainController.setService(service);
                service.addObserver(mainController);
                mainController.initModel();
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }


        }catch (Exception ve) {
            MessageAlert.showErrorMessage(null, ve.getMessage());
            System.err.println(ve.getMessage());
        }
    }

    @FXML
    public void handleRegister(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(StartClient.class.getResource("/view/registerView.fxml"));
        Scene scene=new Scene(fxmlLoader.load());
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.show();
        RegisterController reg = fxmlLoader.getController();
        reg.setService(service);
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
