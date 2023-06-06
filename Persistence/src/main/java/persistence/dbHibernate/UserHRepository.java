package persistence.dbHibernate;

import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class UserHRepository{

    private static SessionFactory sessionFactory;

    public static SessionFactory initialize(){
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
        return sessionFactory;
    }

    static void close(){
        if(sessionFactory != null){
            sessionFactory.close();
        }
    }

    public UserHRepository(){};

    public User login(User entity, String password){
        User found = null;
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                String queryString = "SELECT * FROM users u WHERE u.username =:username AND u.password =:password";
                List<User> users = session.createNativeQuery(queryString, User.class)
                        .setParameter("username", entity.getUsername())
                        .setParameter("password", password)
                        .getResultList();
                tx.commit();

                if(!users.isEmpty()){
                    return users.get(0);
                }

            }catch (Exception e){
                if (tx != null) {
                    System.out.println(e.getMessage());
                    tx.rollback();
                }
                return null;
            }
        }
        return null;
    }

    public User register(User entity, String password){
        User u = null;
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                String queryString = "INSERT INTO users(username, email, password, admin) values (:name, :email, :password, 0)";
                session.createNativeQuery(queryString, User.class)
                        .setParameter("name", entity.getUsername())
                        .setParameter("password", password)
                        .setParameter("email", entity.getEmail())
                        .executeUpdate();
                queryString = "SELECT * FROM users u WHERE u.username =:username AND u.password =:password";
                List<User> users = session.createNativeQuery(queryString, User.class)
                        .setParameter("username", entity.getUsername())
                        .setParameter("password", password)
                        .getResultList();
                u = users.get(0);
                tx.commit();
            } catch (RuntimeException ex) {
                System.err.println("Eroare la register " + ex);
                if (tx != null)
                    tx.rollback();
            }
        }
        return u;
    }

    public Iterable<User> findAll() {
        List<User> all = null;
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                String queryString = "SELECT * FROM users u where admin = 0";
                all = session.createNativeQuery(queryString, User.class)
                        .getResultList();
                tx.commit();

            }catch (Exception e){
                if (tx != null) {
                    System.out.println(e.getMessage());
                    tx.rollback();
                }
                return null;
            }
        }
        return all;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void setSessionFactory(SessionFactory sessionFactory) {
        UserHRepository.sessionFactory = sessionFactory;
    }
}
