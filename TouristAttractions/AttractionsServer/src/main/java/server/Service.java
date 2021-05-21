package server;

import model.AgencyUser;
import model.Reservation;
import model.Trip;
import trip.repository.AgencyUserRepository;
import trip.repository.ReservationRepository;
import trip.repository.TripRepository;
import service.AttractionException;
import service.IService;
import utils.Observer;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Service implements IService {
    private AgencyUserRepository agencyUserRepository;
    private TripRepository tripRepository;
    private ReservationRepository reservationRepository;

    private Map<Integer, Observer> loggedAgencyUsers;

    public Service(AgencyUserRepository agencyUserRepository, TripRepository tripRepository, ReservationRepository reservationRepository){
        this.agencyUserRepository = agencyUserRepository;
        this.tripRepository = tripRepository;
        this.reservationRepository = reservationRepository;
        loggedAgencyUsers = new ConcurrentHashMap<>();
    }

    public void notifyObservers(){
        Iterable<AgencyUser> agencyUsers=findAllAgencyUsers();
        for(AgencyUser agencyUser :agencyUsers){
            Observer client = loggedAgencyUsers.get(agencyUser.getId());
            if (client!=null){
                System.out.println("Notifying "+ agencyUser.getId());
                Iterable<Trip> trips = findAllTrips();
                try {
                    client.bookedTrip(trips);
                } catch (AttractionException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void login(AgencyUser agencyUser, Observer observer){
        AgencyUser agencyUser1 = filterAgencyUserByUserNameAndPassword(agencyUser.getUserName(), agencyUser.getPassword());
        if (agencyUser1 != null) {
            if(loggedAgencyUsers.get(agencyUser1.getId())!=null)
                throw new AttractionException("Agency User already logged in!");
            loggedAgencyUsers.put(agencyUser1.getId(), observer);
        } else
            throw new AttractionException("Authentication failed!");
    }

    public void logout(AgencyUser agencyUser, Observer observer){
        Observer localClient = loggedAgencyUsers.remove(agencyUser.getId());
        if(localClient == null)
            throw new AttractionException("AgencyUser" + agencyUser.getId() + " is not logged in");

    }

    public int getAgencyUsersSize(){
        return agencyUserRepository.size();
    }

    public int getTripsSize(){
        return tripRepository.size();
    }

    public int getReservationsSize(){
        return reservationRepository.size();
    }

    public void addAgencyUser(AgencyUser agencyUser){
        agencyUserRepository.add(agencyUser);
    }

    public void addTrip(Trip trip){
        tripRepository.add(trip);
    }

    public void addReservation(Reservation reservation){
        reservationRepository.add(reservation);
    }

    public void updateAgencyUser(AgencyUser agencyUser){
        agencyUserRepository.update(agencyUser, agencyUser.getId());
    }

    public void updateTrip(Trip trip){
        tripRepository.update(trip, trip.getId());
        notifyObservers();
    }

    public void updateReservation(Reservation reservation){
        reservationRepository.update(reservation, reservation.getId());
    }

    public void deleteAgencyUser(AgencyUser agencyUser){
        agencyUserRepository.delete(agencyUser);
    }

    @Override
    public void deleteTrip(Trip trip) {

    }

    @Override
    public void deleteReservation(Reservation reservation) {

    }

    public void deleteTrip(Integer id){
        tripRepository.delete(id);
    }

    public void deleteReservation(Integer id){
        reservationRepository.delete(id);
    }

    public Iterable<AgencyUser> findAllAgencyUsers(){
        return agencyUserRepository.findAll();
    }

    public Iterable<Trip> findAllTrips(){
        return tripRepository.findAll();
    }

    public Iterable<Reservation> findAllReservations(){
        return reservationRepository.findAll();
    }

    public AgencyUser findAgencyUserById (Integer id){
        return agencyUserRepository.findById(id);
    }

    public Trip findTripById (Integer id){
        return tripRepository.findById(id);
    }

    public Reservation findReservationById (Integer id){
        return reservationRepository.findById(id);
    }

    public AgencyUser filterAgencyUserByUserNameAndPassword(String userName, String password){
        return agencyUserRepository.filterAgencyUserByUserNameAndPassword(userName, password);
    }

    public List<Trip> searchTripByTouristAttractionAndLeavingHour(String touristAttraction, LocalTime hour1, LocalTime hour2){
        return tripRepository.searchTripByTouristAttractionAndLeavingHour(touristAttraction, hour1, hour2);
    }

    public List<Reservation> searchReservationByClientNameAndTelephone(String clientName, String telephone){
        return reservationRepository.searchReservationByClientNameAndTelephone(clientName, telephone);
    }
}
