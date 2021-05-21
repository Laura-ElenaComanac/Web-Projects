package transformer;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import trip.repository.AgencyUserRepository;
import trip.repository.ReservationDBRepository;
import trip.repository.TripDBRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ThriftServer implements ThriftService.Iface{
    private AgencyUserRepository agencyUserRepository;
    private TripDBRepository tripRepository;
    private ReservationDBRepository reservationRepository;

    private List<Integer> ports = new ArrayList<>();
    private Map<Integer, Integer> loggedAgencyUsers;
    private List<TripsController.Client> observers = new ArrayList<>();

    public ThriftServer(AgencyUserRepository agencyUserRepository, TripDBRepository tripRepository, ReservationDBRepository reservationRepository) {
        this.agencyUserRepository = agencyUserRepository;
        this.tripRepository = tripRepository;
        this.reservationRepository = reservationRepository;
        loggedAgencyUsers = new ConcurrentHashMap<>();
    }


    @Override
    public void addObserver(int port) throws TTransportException {
        TTransport transport = new TSocket("localhost",port);
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        TripsController.Client client = new TripsController.Client(protocol);
        ports.add(port);
        observers.add(client);
    }

    public void removeObserver(int port) {
        int index = ports.indexOf(port);
        ports.remove(index);
        observers.remove(index);
    }

    @Override
    public void Login(AgencyUser agencyUser) throws TTransportException {
        model.AgencyUser agencyUser1 = agencyUserRepository.filterAgencyUserByUserNameAndPassword(agencyUser.getUserName(), agencyUser.getPassword());
        if (agencyUser1 != null){
            Integer obs = loggedAgencyUsers.putIfAbsent(agencyUser.getId(),5560);
            if(obs != null)
                System.out.println("Already connected!");
            else
                System.out.println("OK!");
        }
    }

    @Override
    public void Logout(int port) throws TTransportException {
        removeObserver(port);
    }

    @Override
    public int GetReservationsSize() throws TTransportException {
        return reservationRepository.size();
    }

    @Override
    public void AddReservation(Reservation reservation) throws TTransportException {
        reservationRepository.add(new model.Reservation(reservation.id, reservation.nrTickets, reservation.clientName, reservation.telephone, reservation.agencyUserId, reservation.tripId));
    }

    @Override
    public void UpdateTrip(Trip trip) throws TException {
        int hour = trip.leavingHour.hour;
        int minute = trip.leavingHour.minute;
        int second = trip.leavingHour.second;
        //String s = hour +":"+ minute +":"+ second +".0";
        tripRepository.update(new model.Trip(trip.id, trip.touristAttraction, trip.transportCompany, java.time.LocalTime.of(hour, minute, second), trip.price, trip.nrSeats), trip.getId());
        notifyServer();
    }

    @Override
    public List<AgencyUser> FindAllAgencyUsers() throws TTransportException {
        Iterable<model.AgencyUser> au = agencyUserRepository.findAll();
        List<model.AgencyUser> agencyUsers = new ArrayList<>();
        au.forEach(agencyUsers::add);
        List<AgencyUser> agencyUsers1 = new ArrayList<>();
        agencyUsers.forEach(a -> {
            agencyUsers1.add(new AgencyUser(a.getId(), a.getUserName(), a.getPassword()));
        });
        return agencyUsers1;
    }

    @Override
    public List<Trip> FindAllTrips() throws TTransportException {
        Iterable<model.Trip> tr = tripRepository.findAll();
        List<model.Trip> trips = new ArrayList<>();
        tr.forEach(trips::add);
        List<Trip> trips1 = new ArrayList<>();
        trips.forEach(a -> {
            trips1.add(new Trip(a.getId(), a.getTouristAttraction(), a.getTransportCompany(), new LocalTime(a.getLeavingHour().getHour(), a.getLeavingHour().getMinute(), a.getLeavingHour().getSecond()), a.getPrice(), a.getNrSeats()));
        });
        return trips1;
    }

    @Override
    public AgencyUser SearchAgencyUserByUserNameAndPassword(String userName, String password) throws TTransportException {
        model.AgencyUser au = agencyUserRepository.filterAgencyUserByUserNameAndPassword(userName, password);
        return new AgencyUser(au.getId(), au.getUserName(), au.getPassword());
    }

    @Override
    public List<Trip> SearchTripByTouristAttractionAndLeavingHour(String touristAttraction, LocalTime hour1, LocalTime hour2) throws TTransportException {
        int hour11 = hour1.hour;
        int minute11 = hour1.minute;
        int second11 = hour1.second;
        //String s1 = hour11 +":"+ minute11 +":"+ second11 +".0";

        int hour22 = hour2.hour;
        int minute22 = hour2.minute;
        int second22 = hour2.second;
        //String s2 = hour22 +":"+ minute22 +":"+ second22 +".0";

        List<model.Trip> tr = tripRepository.searchTripByTouristAttractionAndLeavingHour(touristAttraction, java.time.LocalTime.of(hour11, minute11, second11), java.time.LocalTime.of(hour22, minute22, second22));
        List<Trip> trips = new ArrayList<>();
        tr.forEach(t -> {
            int hour = t.getLeavingHour().getHour();
            int minute = t.getLeavingHour().getMinute();
            int second = t.getLeavingHour().getSecond();
            trips.add(new Trip(t.getId(), t.getTouristAttraction(), t.getTransportCompany(), new LocalTime(hour, minute, second), t.getPrice(), t.getNrSeats()));});

        return trips;
    }

    @Override
    public void notifyServer() throws TException {
        for(TripsController.Client client : observers) {
            client.Update(FindAllTrips());
        }
    }

}
