package persistence.dbHibernate;

import model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ReservationHRepository {

    private static SessionFactory sessionFactory;

    public static void initialize(){
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exceptie "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close(){
        if(sessionFactory != null){
            sessionFactory.close();
        }
    }
    public ReservationHRepository(){};
    public Iterable<Seat> findSeats(EventRepresentation event){
        List<Seat> seats = null;
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                String queryString = "SELECT s.* FROM seats s JOIN reservations r ON s.res_id = r.id WHERE r.rep_id = :eventRepresentationId";
                List<Object[]> result = session.createNativeQuery(queryString)
                        .setParameter("eventRepresentationId", event.getId())
                        .getResultList();
                seats = new ArrayList<>();
                for (Object[] row : result) {
                    Seat seat = new Seat((Integer) row[0], null, (Integer) row[2]);
                    seats.add(seat);
                }

                System.out.println(seats.get(0));

                // THIS IS HALF ASSED FIX IT !!!
                String hqlQuery = "FROM Seat s";
                List<Seat> ss = session.createQuery(hqlQuery)
                        .list();
                List<Seat> sss = new ArrayList<>();
                ss.forEach(x->{
                    if(x.getReservation().getEvent().getId() == event.getId())
                        sss.add(x);
                });
                System.out.println("FIND SEATS" + sss);
                seats = sss;
                tx.commit();

            }catch (Exception e){
                if (tx != null) {
                    System.out.println(e.getMessage());
                    tx.rollback();
                }
                return null;
            }
        }
        return seats;
    }

    public Iterable<Seat> findSeatsForReservation(Reservation res){
        List<Seat> seats = null;
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();

                // THIS IS HALF ASSED FIX IT !!!
                String hqlQuery = "FROM Seat s";
                List<Seat> ss = session.createQuery(hqlQuery)
                        .list();
                List<Seat> sss = new ArrayList<>();
                ss.forEach(x->{
                    if(x.getReservation().getId() == res.getId())
                        sss.add(x);
                });
                System.out.println("FIND SEATS" + sss);
                seats = sss;
                tx.commit();

            }catch (Exception e){
                if (tx != null) {
                    System.out.println(e.getMessage());
                    tx.rollback();
                }
                return null;
            }
        }
        return seats;
    }

    public void reserveSeats(Reservation reservation, List<Integer> seatNumbers) throws Exception {
        Iterable<Seat> seatsForEvent = this.findSeats(reservation.getEvent());
        if(seatsForEvent != null) {
            List<Integer> occupied = new ArrayList<>();
            seatsForEvent.forEach(x -> occupied.add(x.getNumber()));

            for(Integer i : seatNumbers) {
                if(occupied.contains(i)){
                    throw new Exception("Unul dintre locuri este deja occupat");
                }
            }
        }


        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                String queryString = "INSERT INTO reservations(user_id, rep_id, date) values (:uid, :repid, :date)";
                session.createNativeQuery(queryString, Reservation.class)
                        .setParameter("uid", reservation.getUser().getId())
                        .setParameter("repid", reservation.getEvent().getId())
                        .setParameter("date", reservation.getDate())
                        .executeUpdate();

                queryString = "SELECT * from reservations r WHERE r.date =:date";
                List<Reservation> res = session.createNativeQuery(queryString, Reservation.class)
                                .setParameter("date", reservation.getDate()).getResultList();

                Reservation actualRes = res.get(0);

                for(Integer i : seatNumbers) {
                    queryString = "INSERT INTO seats(res_id, number) values (:resid, :val)";
                    session.createNativeQuery(queryString, Seat.class)
                            .setParameter("resid", actualRes.getId()).setParameter("val", i).executeUpdate();
                }

                Integer nr = seatNumbers.size();
                queryString = "UPDATE eventrepresentations set avb = :available, sold = :soldS where id = :repid";
                session.createNativeQuery(queryString, EventRepresentation.class)
                        .setParameter("available", reservation.getEvent().getAvb() - nr)
                        .setParameter("soldS", reservation.getEvent().getSold() + nr)
                        .setParameter("repid", reservation.getEvent().getId())
                        .executeUpdate();

                tx.commit();

            }catch (Exception e){
                if (tx != null) {
                    System.out.println(e.getMessage());
                    tx.rollback();
                }
            }

        }
    }

    public void deleteReservationAndSeats(Integer id){
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                String queryString = "DELETE FROM seats where res_id =:resid";
                Integer nr = session.createNativeQuery(queryString, Reservation.class)
                        .setParameter("resid", id)
                        .executeUpdate();

                queryString = "SELECT * from reservations r WHERE r.id = :id";
                List<Reservation> res = session.createNativeQuery(queryString, Reservation.class)
                        .setParameter("id", id).getResultList();
                Reservation r = res.get(0);
                queryString = "UPDATE eventrepresentations set avb = :available, sold = :soldS where id= :repid";
                session.createNativeQuery(queryString, EventRepresentation.class)
                        .setParameter("available",r.getEvent().getAvb() + nr)
                        .setParameter("soldS", r.getEvent().getSold() - nr)
                        .setParameter("repid", r.getEvent().getId())
                        .executeUpdate();

                queryString = "DELETE FROM reservations where id =:resid";
                session.createNativeQuery(queryString, Reservation.class)
                        .setParameter("resid", id)
                        .executeUpdate();

                tx.commit();

            }catch (Exception e){
                if (tx != null) {
                    System.out.println(e.getMessage());
                    tx.rollback();
                }
            }
        }
    }

    public Iterable<Reservation> getAllReservationsForUser(User user){
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

//                String queryString = "SELECT * from reservations r WHERE r.user_id = :uid";
//                List<Reservation> res = session.createNativeQuery(queryString, Reservation.class)
//                        .setParameter("uid",user.getId()).getResultList();

                String hqlQuery = "FROM Reservation r";
                List<Reservation> ss = session.createQuery(hqlQuery)
                        .list();
                List<Reservation> res = new ArrayList<>();
                ss.forEach(x->{
                    if(x.getUser().getId() == user.getId())
                        res.add(x);
                });
                System.out.println("FIND RESERVATIONS" + res);


                tx.commit();
                return res;

            }catch (Exception e){
                if (tx != null) {
                    System.out.println(e.getMessage());
                    tx.rollback();
                }
            }
        }
        return null;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void setSessionFactory(SessionFactory sessionFactory) {
        ReservationHRepository.sessionFactory = sessionFactory;
    }
}
