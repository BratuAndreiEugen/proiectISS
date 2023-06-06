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
import model.EventRepresentation;
import model.EventRepresentationDTO;
import model.User;
import service.Service;
import utils.Observer;

import java.util.ArrayList;
import java.util.List;

public class EventController implements Observer {

    private Service service;

    private ObservableList<EventRepresentationDTO> modelEvents = FXCollections.observableArrayList();

    @FXML
    TableView<EventRepresentationDTO> eventTable;

    @FXML
    TableColumn<EventRepresentationDTO, Integer> columnId;

    @FXML
    TableColumn<EventRepresentationDTO, String> columnName;

    @FXML
    TableColumn<EventRepresentationDTO, String> columnDate;

    @FXML
    TableColumn<EventRepresentationDTO, Integer> columnAvb;
    @FXML
    TableColumn<EventRepresentationDTO, Integer> columnSold;

    @FXML
    Button buttonReset;

    @FXML
    Button buttonAdd;


    @FXML
    public void initialize(){

        columnId.setCellValueFactory(new PropertyValueFactory<EventRepresentationDTO, Integer>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<EventRepresentationDTO, String>("name"));
        columnDate.setCellValueFactory(new PropertyValueFactory<EventRepresentationDTO, String>("date"));
        columnAvb.setCellValueFactory(new PropertyValueFactory<EventRepresentationDTO, Integer>("avb"));
        columnSold.setCellValueFactory(new PropertyValueFactory<EventRepresentationDTO, Integer>("sold"));
        eventTable.setItems(modelEvents);

        EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleReset(event);
            }

        };
        buttonReset.setOnAction(event1);


        EventHandler<ActionEvent> event2 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleAdd(event);
            }

        };
        buttonAdd.setOnAction(event2);
    }

    private void handleAdd(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartClient.class.getResource("/view/addEventView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            AddEventController controller = fxmlLoader.getController();
            controller.setService(service);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void handleReset(ActionEvent event) {
        EventRepresentationDTO selected = eventTable.getSelectionModel().getSelectedItem();
        if(selected == null){
            MessageAlert.showErrorMessage(null, "You haven't selected any event");
            return;
        }

        try{
            service.resetEvent(selected.getId());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void loadModel(){
        Iterable<EventRepresentation> e = service.getAll();
        List<EventRepresentationDTO> list = new ArrayList<>();
        e.forEach(x->{
            list.add(new EventRepresentationDTO(x.getId(), x.getEvent().getName(), x.getDate(), x.getAvb(), x.getSold()));
        });
        modelEvents.setAll(list);
    }

    @Override
    public void update() {
        loadModel();
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
