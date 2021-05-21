package service;

import model.AgencyUser;
import model.Reservation;
import model.Trip;
import utils.Observer;

import java.time.LocalTime;
import java.util.List;

public interface IService {
    void login(AgencyUser agencyUser, Observer observer);
    void logout(AgencyUser agencyUser, Observer observer);
    int getAgencyUsersSize();
    int getTripsSize();
    int getReservationsSize();
    void addAgencyUser(AgencyUser agencyUser);
    void addTrip(Trip trip);
    void addReservation(Reservation reservation);
    void updateAgencyUser(AgencyUser agencyUser);
    void updateTrip(Trip trip);
    void updateReservation(Reservation reservation);
    void deleteAgencyUser(AgencyUser agencyUser);
    void deleteTrip(Trip trip);
    void deleteReservation(Reservation reservation);
    Iterable<AgencyUser> findAllAgencyUsers();
    Iterable<Trip> findAllTrips();
    Iterable<Reservation> findAllReservations();
    AgencyUser findAgencyUserById (Integer id);
    Trip findTripById (Integer id);
    Reservation findReservationById (Integer id);
    AgencyUser filterAgencyUserByUserNameAndPassword(String userName, String password);
    List<Trip> searchTripByTouristAttractionAndLeavingHour(String touristAttraction, LocalTime hour1, LocalTime hour2);
    List<Reservation> searchReservationByClientNameAndTelephone(String clientName, String telephone);
}
