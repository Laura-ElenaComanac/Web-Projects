package client.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.AgencyUser;
import model.Reservation;
import model.Trip;
import service.AttractionException;
import service.IService;
import utils.Observer;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TripsController implements Observer {
    private IService service;

    ObservableList<Trip> tripsModel = FXCollections.observableArrayList();
    ObservableList<Trip> searchedTripsModel = FXCollections.observableArrayList();

    Stage dialogStage;
    AgencyUser agencyUser;

    @FXML
    TableView<Trip> tripsTableView;
    @FXML
    TableView<Trip> searchedTripsTableView;

    @FXML
    TableColumn<Trip, String> touristAttractionTableColumn;
    @FXML
    TableColumn<Trip, String> transportCompanyTableColumn;
    @FXML
    TableColumn<Trip, LocalTime> departureTimeTableColumn;
    @FXML
    TableColumn<Trip, Double> priceTableColumn;
    @FXML
    TableColumn<Trip, Integer> nrSeatsTableColumn;

    @FXML
    TableColumn<Trip, String> searchedTransportCompanyTableColumn;
    @FXML
    TableColumn<Trip, Double> searchedPriceTableColumn;
    @FXML
    TableColumn<Trip, LocalTime> searchedDepartureTimeTableColumn;
    @FXML
    TableColumn<Trip, Integer> searchedNrSeatsTableColumn;

    @FXML
    Label usernameLabel;
    @FXML
    TextField touristAttractionTextField;
    @FXML
    TextField hour1TextField;
    @FXML
    TextField hour2TextField;
    @FXML
    TextField clientNameTextField;
    @FXML
    TextField telephoneTextField;
    @FXML
    TextField nrTicketsTextField;


    public void setService(IService service, AgencyUser agencyUser, Stage dialogStage){
        this.service = service;
        this.agencyUser = agencyUser;
        this.dialogStage = dialogStage;
        usernameLabel.setText(agencyUser.getUserName());
        //service.addObserver(this);
    }

    public void setService(IService service) {
        this.service = service;
    }

    public void setAgencyUser(AgencyUser agencyUser) {
        this.agencyUser = agencyUser;
        usernameLabel.setText(agencyUser.getUserName());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    public void initialize(){
        touristAttractionTableColumn.setCellValueFactory(new PropertyValueFactory<>("touristAttraction"));
        transportCompanyTableColumn.setCellValueFactory(new PropertyValueFactory<>("transportCompany"));
        departureTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("leavingHour"));
        priceTableColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        nrSeatsTableColumn.setCellValueFactory(new PropertyValueFactory<>("nrSeats"));
        tripsTableView.setItems(tripsModel);

        searchedTransportCompanyTableColumn.setCellValueFactory(new PropertyValueFactory<>("transportCompany"));
        searchedDepartureTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("leavingHour"));
        searchedPriceTableColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        searchedNrSeatsTableColumn.setCellValueFactory(new PropertyValueFactory<>("nrSeats"));
        searchedTripsTableView.setItems(searchedTripsModel);

        tripsTableView.setRowFactory(tv -> new TableRow<Trip>() {
            @Override
            protected void updateItem(Trip item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null)
                    setStyle("");
                else if (item.getNrSeats() == 0)
                    setStyle("-fx-background-color: #ff0000;");
            }
        });

        searchedTripsTableView.setRowFactory(tv -> new TableRow<Trip>() {
            @Override
            protected void updateItem(Trip item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null)
                    setStyle("");
                else if (item.getNrSeats() == 0)
                    setStyle("-fx-background-color: #ff0000;");
            }
        });
    }

    public void initTripsModel(AgencyUser agencyUser){
        Iterable<Trip> trips = service.findAllTrips();
        List<Trip> tripsList = StreamSupport.stream(trips.spliterator(), false).collect(Collectors.toList());

        tripsModel.setAll(tripsList);
    }

    private void initSearchedTripsModel(AgencyUser agencyUser){
        if(!(touristAttractionTextField.getText().equals("") && hour1TextField.getText().equals("") && hour2TextField.getText().equals(""))) {
            //Iterable<Trip> searchedTrips = service.searchTripByTouristAttractionAndLeavingHour(touristAttractionTextField.getText(), LocalTime.parse(hour1TextField.getText()), LocalTime.parse(hour2TextField.getText()));
            //List<Trip> searchedTripsList = StreamSupport.stream(searchedTrips.spliterator(), false).collect(Collectors.toList());
            //searchedTripsModel.setAll(searchedTripsList);
            searchedTripsModel.setAll(
                    tripsModel.stream().filter(
                            (trip -> trip.getTouristAttraction().equals(touristAttractionTextField.getText()) &&
                                    trip.getLeavingHour().compareTo(LocalTime.parse(hour1TextField.getText())) >= 0 &&
                                    trip.getLeavingHour().compareTo(LocalTime.parse(hour2TextField.getText())) <= 0
                            )
                    )
                    .collect(Collectors.toList())
            );
        }
    }

    @FXML
    public void handleSearchTripsButton(ActionEvent event) {
        try{
            initSearchedTripsModel(agencyUser);
        } catch (Exception e) {
            searchedTripsModel.clear();
            MessageAlert.showErrorMessage(null, "Wrong data!");
        }
    }

    @FXML
    public void handleBookButton(ActionEvent event) {
        try {
            String client = clientNameTextField.getText();
            String telephone = telephoneTextField.getText();
            int nrTickets = Integer.parseInt(nrTicketsTextField.getText());

            Trip selectedTrip = searchedTripsTableView.getSelectionModel().getSelectedItem();

            if(selectedTrip.getNrSeats() - nrTickets < 0)
                MessageAlert.showErrorMessage(null, "There are no available seats!");
            else {
                selectedTrip.setNrSeats(selectedTrip.getNrSeats() - nrTickets);
                service.updateTrip(selectedTrip);

                Reservation reservation = new Reservation(service.getReservationsSize() + 1, nrTickets, client, telephone, agencyUser.getId(), selectedTrip.getId());
                service.addReservation(reservation);
            }
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, "Wrong data!");
        }
    }

    void logout() {
        try {
            service.logout(agencyUser, this);
        } catch (AttractionException e) {
            System.out.println("Logout error " + e);
        }
    }

    @FXML
    public void handleLogoutButton(ActionEvent event) {
            logout();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }

    @Override
    public void bookedTrip(Iterable<Trip> trips) throws AttractionException {
        Platform.runLater(() -> {
            tripsModel.setAll(StreamSupport.stream(trips.spliterator(), false).collect(Collectors.toList()));
            initSearchedTripsModel(agencyUser);
        });
    }
}
