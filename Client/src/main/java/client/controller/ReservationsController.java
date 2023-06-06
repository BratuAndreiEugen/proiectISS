package client.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Reservation;
import model.ReservationDTO;
import model.Seat;
import model.User;
import service.Service;
import utils.Observer;

import java.util.ArrayList;
import java.util.List;

public class ReservationsController implements Observer {

    private Service service;

    private User current;

    private ObservableList<ReservationDTO> modelRes = FXCollections.observableArrayList();

    @FXML
    TableView<ReservationDTO> resTable;

    @FXML
    TableColumn<ReservationDTO, Integer> columnId;

    @FXML
    TableColumn<ReservationDTO, String> columnEvent;

    @FXML
    TableColumn<ReservationDTO, String> columnSeats;

    @FXML
    TableColumn<ReservationDTO, String> columnDate;

    @FXML
    TableColumn<ReservationDTO, String> columnRes;

    @FXML
    Button deleteButton;


    @FXML
    public void initialize() {
        columnId.setCellValueFactory(new PropertyValueFactory<ReservationDTO, Integer>("id"));
        columnEvent.setCellValueFactory(new PropertyValueFactory<ReservationDTO, String>("event"));
        columnDate.setCellValueFactory(new PropertyValueFactory<ReservationDTO, String>("eventDate"));
        columnSeats.setCellValueFactory(new PropertyValueFactory<ReservationDTO, String>("seatList"));
        columnRes.setCellValueFactory(new PropertyValueFactory<ReservationDTO, String>("date"));
        resTable.setItems(modelRes);


        EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleDelete(event);
            }

        };
        deleteButton.setOnAction(event1);

    }

    private void handleDelete(ActionEvent event) {
        ReservationDTO selected = resTable.getSelectionModel().getSelectedItem();
        if(selected == null){
            MessageAlert.showErrorMessage(null, "You haven't selected any reservation");
            return;
        }

        try{
            service.deleteReservation(selected.getId());
        }catch (Exception e){
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void loadModel(){
        Iterable<Reservation> reses = service.getResForUser(current);
        System.out.println(reses);
        List<ReservationDTO> modelList = new ArrayList<>();
        for(Reservation reservation : reses){
            Iterable<Seat> seats = service.getSeatsForReservation(reservation);
            System.out.println( "UI : " + seats);
            String seatList = "";
            for( Seat s : seats){
                seatList = seatList.concat(s.getNumber().toString() + ",");
            }

            ReservationDTO r = new ReservationDTO(reservation.getId(), reservation.getEvent().getEvent().getName(), reservation.getDate(),reservation.getEvent().getDate(), seatList);
            modelList.add(r);
        }
        modelRes.setAll(modelList);
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

    public User getCurrent() {
        return current;
    }

    public void setCurrent(User current) {
        this.current = current;
    }
}
