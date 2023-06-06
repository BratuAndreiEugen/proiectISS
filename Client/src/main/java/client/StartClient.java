package client;

import client.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import persistence.dbHibernate.EventHRepository;
import persistence.dbHibernate.ReservationHRepository;
import persistence.dbHibernate.UserHRepository;
import service.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class StartClient extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties serverProps=new Properties();
        try {
            serverProps.load(new FileReader("C:\\Proiecte SSD\\proiectISS\\Client\\src\\main\\resources\\db.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatserver.properties "+e);
            return;
        }

        UserHRepository repoUsers = new UserHRepository();
        EventHRepository repoEvents = new EventHRepository();
        ReservationHRepository repoRes = new ReservationHRepository();
        SessionFactory s = UserHRepository.initialize();
        repoEvents.setSessionFactory(s);
        repoRes.setSessionFactory(s);

        Service service = new Service(repoUsers, repoEvents, repoRes);


        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/loginView.fxml"));
        AnchorPane root=loader.load();

        LoginController ctrl=loader.getController();
        ctrl.setService(service);
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Login");
        primaryStage.show();
    }

    public static void main(String[] args){ launch(args);}
}
