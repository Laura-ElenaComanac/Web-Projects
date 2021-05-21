package network.objectprotocol;

import model.AgencyUser;
import model.Reservation;
import model.Trip;
import network.dto.AgencyUserDTO;
import network.dto.SearchedTripDTO;
import service.AttractionException;
import service.IService;
import utils.Observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class AttractionsServicesObjectProxy implements IService {
    private String host;
    private int port;

    private Observer client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    //private List<Response> responses;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public AttractionsServicesObjectProxy(String host, int port) {
        this.host = host;
        this.port = port;
        //responses=new ArrayList<Response>();
        qresponses=new LinkedBlockingQueue<Response>();
    }


    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request)throws AttractionException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new AttractionException("Error sending object "+e);
        }

    }

    private Response readResponse() throws AttractionException {
        Response response=null;
        try{

            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void initializeConnection() throws AttractionException {
         try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(UpdateResponse update){
        BookTripResponse response = (BookTripResponse) update;
        Iterable<Trip> trips = response.getTrips();
        System.out.println("Trip booked");

        try {
            client.bookedTrip(trips);
        }catch (AttractionException e){
            e.printStackTrace();
        }
    }

    @Override
    public void login(AgencyUser agencyUser, Observer observer) {
        initializeConnection();
        sendRequest(new LoginRequest(agencyUser));
        Response response=readResponse();
        if (response instanceof OkResponse){
            this.client=observer;
            return;
        }
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            closeConnection();
            throw new AttractionException(err.getMessage());
        }
    }

    @Override
    public void logout(AgencyUser agencyUser, Observer observer) {
        sendRequest(new LogoutRequest(agencyUser));
        Response response=readResponse();
        closeConnection();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new AttractionException(err.getMessage());
        }
    }

    @Override
    public int getAgencyUsersSize() {
        return 0;
    }

    @Override
    public int getTripsSize() {
        return 0;
    }

    @Override
    public int getReservationsSize() {
        sendRequest(new ReservationSizeRequest());
        Response response = readResponse();
        if (response instanceof ErrorResponse) {
            ErrorResponse err = (ErrorResponse) response;
            closeConnection();
            throw new AttractionException(err.getMessage());
        }
        //closeConnection();
        ReservationSizeResponse reservationSizeResponse = (ReservationSizeResponse) response;
        return reservationSizeResponse.getSize();
    }

    @Override
    public void addAgencyUser(AgencyUser agencyUser) {

    }

    @Override
    public void addTrip(Trip trip) {

    }

    @Override
    public void addReservation(Reservation reservation) {
        sendRequest(new AddReservationRequest(reservation));
        Response response = readResponse();
        if (response instanceof ErrorResponse) {
            ErrorResponse err = (ErrorResponse) response;
            throw new AttractionException(err.getMessage());
        }
    }

    @Override
    public void updateAgencyUser(AgencyUser agencyUser) {

    }

    @Override
    public void updateTrip(Trip trip) {
        sendRequest(new BookTripRequest(trip));
        Response response = readResponse();
        if (response instanceof ErrorResponse) {
            ErrorResponse err = (ErrorResponse) response;
            throw new AttractionException(err.getMessage());
        }
    }

    @Override
    public void updateReservation(Reservation reservation) {

    }

    @Override
    public void deleteAgencyUser(AgencyUser agencyUser) {

    }

    @Override
    public void deleteTrip(Trip trip) {

    }

    @Override
    public void deleteReservation(Reservation reservation) {

    }

    @Override
    public Iterable<AgencyUser> findAllAgencyUsers() {
        return null;
    }

    @Override
    public Iterable<Trip> findAllTrips() {
        sendRequest(new FindAllTripsRequest());
        Response response = readResponse();

        if (response instanceof ErrorResponse) {
            ErrorResponse err = (ErrorResponse) response;
            throw new AttractionException(err.getMessage());
        }
        FindAllTripsResponse resp = (FindAllTripsResponse) response;
        Iterable<Trip> trips = resp.getTrips();
        return trips;
    }

    @Override
    public Iterable<Reservation> findAllReservations() {
        return null;
    }

    @Override
    public AgencyUser findAgencyUserById(Integer id) {
        return null;
    }

    @Override
    public Trip findTripById(Integer id) {
        return null;
    }

    @Override
    public Reservation findReservationById(Integer id) {
        return null;
    }

    @Override
    public AgencyUser filterAgencyUserByUserNameAndPassword(String userName, String password) {
        //initializeConnection();
        sendRequest(new FindAgencyUserRequest(new AgencyUserDTO(userName, password)));
        Response response = readResponse();
        if (response instanceof ErrorResponse) {
            ErrorResponse err = (ErrorResponse) response;
            //closeConnection();
            throw new AttractionException(err.getMessage());
        }
        //closeConnection();
        FindAgencyUserResponse findClientResponse = (FindAgencyUserResponse) response;
        return findClientResponse.getAgencyUser();
    }

    @Override
    public List<Trip> searchTripByTouristAttractionAndLeavingHour(String touristAttraction, LocalTime hour1, LocalTime hour2) {
        sendRequest(new SearchedTripsRequest(new SearchedTripDTO(touristAttraction, hour1, hour2)));
        Response response = readResponse();

        if (response instanceof ErrorResponse) {
            ErrorResponse err = (ErrorResponse) response;
            throw new AttractionException(err.getMessage());
        }
        SearchedTripsResponse resp = (SearchedTripsResponse) response;
        Iterable<Trip> trips = resp.getTrips();
        return StreamSupport.stream(trips.spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public List<Reservation> searchReservationByClientNameAndTelephone(String clientName, String telephone) {
        return null;
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (response instanceof UpdateResponse){
                         handleUpdate((UpdateResponse)response);
                    }else{
                        /*qresponses.add((Response)response);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        synchronized (qresponses){
                            qresponses.notify();
                        }*/
                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();  
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
