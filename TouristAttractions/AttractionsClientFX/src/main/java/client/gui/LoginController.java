package client.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.AgencyUser;
import service.AttractionException;
import service.IService;


public class LoginController{
    private IService service;

    Parent parent;
    TripsController tripsController;

    @FXML
    TextField usernameTextField;
    @FXML
    PasswordField passwordTextField;


    public void setService(IService service){
        this.service = service;
        //service.addObserver(this);
    }

    public void setTripsController(TripsController tripsController) {
        this.tripsController = tripsController;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public void handleLoginButton(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        AgencyUser agencyUser = new AgencyUser(-1 ,username, password);

        try{
            service.login(agencyUser, tripsController);
            Stage stage=new Stage();
            stage.setTitle("Trips");
            stage.getIcons().add(new Image("https://riverfriends.org/wp-content/uploads/2017/04/map-map-marker-icon.png"));
            stage.setScene(new Scene(parent));

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    tripsController.logout();
                    System.exit(0);
                }
            });

            stage.show();
            tripsController.setAgencyUser(service.filterAgencyUserByUserNameAndPassword(username, password));
            tripsController.initTripsModel(agencyUser);
            ((Node)(event.getSource())).getScene().getWindow().hide();

        }   catch (AttractionException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Authentication failure");
            alert.setContentText("Wrong username or password");
            alert.showAndWait();
        }

    }

    public void showTripsDialog(AgencyUser agencyUser){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/TripsView.fxml"));
            GridPane root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Trips");
            dialogStage.getIcons().add(new Image("https://riverfriends.org/wp-content/uploads/2017/04/map-map-marker-icon.png"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            TripsController editController = loader.getController();
            editController.setService(service, agencyUser, dialogStage);
            editController.initTripsModel(agencyUser);

            dialogStage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
