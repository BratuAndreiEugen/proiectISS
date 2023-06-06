package service;

import model.*;
import persistence.dbHibernate.EventHRepository;
import persistence.dbHibernate.ReservationHRepository;
import persistence.dbHibernate.UserHRepository;
import utils.Observable;
import utils.Observer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Service implements Observable {
    private List<Observer> obs=new ArrayList<>();

    private UserHRepository repoUsers;
    private EventHRepository repoEvents;
    private ReservationHRepository repoRes;

    public Service(UserHRepository repoUsers, EventHRepository repoEvents, ReservationHRepository repoRes) {
        this.repoUsers = repoUsers;
        this.repoEvents = repoEvents;
        this.repoRes = repoRes;
    }

    /**
     * USER MANAGEMENT
     */
    public Iterable<User> getAllUsersNotAdmin(){return repoUsers.findAll();}

    public User login(String username, String password){
        return repoUsers.login(new User(null, username, 0), password);
    }

    public User register(String username, String email, String password){
        return repoUsers.register(new User(email, username, 0), password);
    }

    /**
     * SEAT MANAGEMENT
     */
    public Iterable<Seat> getFreeSeatsForEvent(EventRepresentation event){return repoRes.findSeats(event);}

    public void reserveSeats(User user, EventRepresentation event, String seatString) throws Exception {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS");
        Reservation r = new Reservation(event, user, LocalDateTime.now().format(f));
        String[] seats = seatString.split(",");
        List<Integer> seatNumbers = new ArrayList<>();
        try {
            Arrays.stream(seats).sequential().forEach(x->seatNumbers.add(Integer.parseInt(x)));
            repoRes.reserveSeats(r, seatNumbers);
            notifyObservers();
        }catch (Exception e){
            throw new Exception(e.toString());
        }

    }

    public Iterable<Reservation> getResForUser(User u){return repoRes.getAllReservationsForUser(u);}

    public Iterable<Seat> getSeatsForReservation(Reservation r){
        return repoRes.findSeatsForReservation(r);
    }

    public void deleteReservation(Integer id){
        try {
            repoRes.deleteReservationAndSeats(id);
            notifyObservers();
        }catch (Exception e){
            return;
        }

    }

    /**
     * EVENT MANAGEMENT
     */

    public void addEventRepresentation(String name, String date) throws Exception {
        Event e = repoEvents.findByName(name);
        if(e == null){
            throw new Exception("We don't organize this event/play");
        }
        EventRepresentation ev = new EventRepresentation(null, e, date, null, null);
        repoEvents.addEventRepresentation(ev);
        notifyObservers();
    }

    public void resetEvent(Integer id){
        repoEvents.resetEventRepresentation(id);
        notifyObservers();
    }


    public Iterable<EventRepresentation> getAll(){return repoEvents.findAll();}

    public EventRepresentation findEventForToday(LocalDate today){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);
        return repoEvents.findByDay(formattedDate);
    }

    @Override
    public void addObserver(Observer e) {
        obs.add(e);
    }

    @Override
    public void removeObserver(Observer e) {
        obs.remove(e);
    }

    @Override
    public void notifyObservers() {
        obs.stream().forEach(x->x.update());
    }
}

