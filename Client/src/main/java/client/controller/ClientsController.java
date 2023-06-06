package client.controller;

import client.StartClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.User;
import service.Service;
import utils.Observer;

import java.util.ArrayList;
import java.util.List;


public class ClientsController implements Observer {
    private Service service;

    private User currentAdmin;

    private ObservableList<User> modelUsers = FXCollections.observableArrayList();

    @FXML
    TableView<User> userTable;

    @FXML
    TableColumn<User, Integer> columnId;

    @FXML
    TableColumn<User, String> columnUser;

    @FXML
    TableColumn<User, String> columnEmail;

    @FXML
    Button viewEventsButton;

    @FXML
    public void initialize() {
        columnId.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        columnUser.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        userTable.setItems(modelUsers);

        EventHandler<ActionEvent> event2 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleViewEvents(event);
            }

        };
        viewEventsButton.setOnAction(event2);

    }

    private void handleViewEvents(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartClient.class.getResource("/view/eventView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            EventController controller = fxmlLoader.getController();
            controller.setService(service);
            service.addObserver(controller);
            controller.loadModel();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void initModel() throws Exception {
        Iterable<User> users = service.getAllUsersNotAdmin();
        List<User> listC = new ArrayList<>();
        if(users != null)
            users.forEach(x->listC.add(x));
        modelUsers.setAll(listC);
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public User getCurrentAdmin() {
        return currentAdmin;
    }

    public void setCurrentAdmin(User currentAdmin) {
        this.currentAdmin = currentAdmin;
    }

    @Override
    public void update() {

    }
}
