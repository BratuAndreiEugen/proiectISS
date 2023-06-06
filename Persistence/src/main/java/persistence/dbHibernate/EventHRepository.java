package persistence.dbHibernate;

import model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EventHRepository {
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

    public EventHRepository(){};

    public EventRepresentation findByDay(String formattedDate) {
        EventRepresentation ev = null;
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<EventRepresentation> criteria = builder.createQuery(EventRepresentation.class);
                Root<EventRepresentation> root = criteria.from(EventRepresentation.class);

                // Add a condition to filter the results
                criteria.where(builder.like(root.get("date"), formattedDate + "%"));

                List<EventRepresentation> events = session.createQuery(criteria).getResultList();
                System.out.println("EV REPO : " + events);
                ev = events.get(0);
                System.out.println("FIND BY DAY : " + events.get(0));
                tx.commit();


            }catch (RuntimeException ex){
                System.err.println(ex);
                if(tx != null)
                    tx.rollback();
            }
        }
        return ev;
    }

    public Iterable<EventRepresentation> findAll(){
        List<EventRepresentation> empty = new ArrayList<>();
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                String hqlQuery = "FROM EventRepresentation e JOIN FETCH e.event";
                List<EventRepresentation> events = session.createQuery(hqlQuery, EventRepresentation.class)
                        .list();

                tx.commit();

                return events;

            }catch (RuntimeException ex){
                System.err.println(ex);
                if(tx != null)
                    tx.rollback();
            }
        }
        return empty;
    }

    public Event findByName(String name){
        Event e = null;
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                String hqlQuery = "FROM Event e where e.name = :name";
                List<Event> events = session.createQuery(hqlQuery, Event.class).setParameter("name", name)
                        .list();

                try {
                    e = events.get(0);
                }catch (Exception ex){
                    return null;
                }
                tx.commit();

                return e;

            }catch (RuntimeException ex){
                System.err.println(ex);
                if(tx != null)
                    tx.rollback();
            }
        }

        return e;
    }

    public void addEventRepresentation(EventRepresentation ev){
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                String queryString = "INSERT INTO eventrepresentations(avb, sold, date, event_id) values (90, 0, :dateStr, :evid)";
                session.createNativeQuery(queryString, Seat.class)
                        .setParameter("dateStr", ev.getDate()).setParameter("evid", ev.getEvent().getId()).executeUpdate();

                tx.commit();

            }catch (Exception e){
                if (tx != null) {
                    System.out.println(e.getMessage());
                    tx.rollback();
                }
            }
        }

    }

    /**
     *
     * @param id Id-ul EventRepresentation-ului ce urmeaza sa fie resetat
     */
    public void resetEventRepresentation(Integer id){
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                String queryString = "SELECT * from reservations WHERE rep_id = :id";
                List<Reservation> res = session.createNativeQuery(queryString, Reservation.class)
                        .setParameter("id", id).getResultList();

                for(Reservation r : res){
                    queryString = "DELETE from seats WHERE res_id = :id";
                    session.createNativeQuery(queryString, Seat.class)
                            .setParameter("id", r.getId()).executeUpdate();
                }

                queryString = "DELETE from reservations WHERE rep_id = :id";
                session.createNativeQuery(queryString, Reservation.class)
                        .setParameter("id", id).executeUpdate();

                queryString = "UPDATE eventrepresentations set avb = 90, sold = 0 WHERE id = :id";
                session.createNativeQuery(queryString, EventRepresentation.class).setParameter("id", id).executeUpdate();

                tx.commit();


            } catch (Exception e) {
                if (tx != null) {
                    System.out.println(e.getMessage());
                    tx.rollback();
                }
            }
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void setSessionFactory(SessionFactory sessionFactory) {
        EventHRepository.sessionFactory = sessionFactory;
    }
}
