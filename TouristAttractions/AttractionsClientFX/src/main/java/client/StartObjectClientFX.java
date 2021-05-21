package client;

import client.gui.LoginController;
import client.gui.TripsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import network.objectprotocol.AttractionsServicesObjectProxy;
import service.IService;

import java.io.IOException;
import java.util.Properties;

public class StartObjectClientFX extends Application {
    private Stage primaryStage;

    private static int defaultPort = 55555;
    private static String defaultServer = "localhost";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartObjectClientFX.class.getResourceAsStream("/touristAttractionsClient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find touristAttractionsClient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("touristAttractions.server.host", defaultServer);
        int serverPort = defaultPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("touristAttractions.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IService server = new AttractionsServicesObjectProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/LoginView.fxml"));
        Parent root = loader.load();

        LoginController ctrlLogIn = loader.getController();
        ctrlLogIn.setService(server);
        primaryStage.setTitle("Login");
        primaryStage.getIcons().add(new Image("https://png.pngtree.com/png-clipart/20190516/original/pngtree-vector-airplane-icon-png-image_4277510.jpg"));


        FXMLLoader tripsLoader = new FXMLLoader(getClass().getClassLoader().getResource("views/TripsView.fxml"));
        Parent mainRoot = tripsLoader.load();

        TripsController tripsController = tripsLoader.getController();

        tripsController.setService(server);
        ctrlLogIn.setTripsController(tripsController);
        ctrlLogIn.setParent(mainRoot);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}

