package client;

import model.EventRepresentation;
import model.Reservation;
import org.hibernate.SessionFactory;
import persistence.dbHibernate.ReservationHRepository;
import persistence.dbHibernate.UserHRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestApp {

    public static void main(String[] args){
        System.out.println(LocalDateTime.now());
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
        System.out.println(LocalDateTime.now().format(f));

    }
}
