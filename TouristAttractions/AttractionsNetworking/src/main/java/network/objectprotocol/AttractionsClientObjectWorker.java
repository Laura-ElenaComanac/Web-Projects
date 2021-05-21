package network.objectprotocol;

import model.Trip;
import network.dto.AgencyUserDTO;
import service.AttractionException;
import service.IService;
import utils.Observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class AttractionsClientObjectWorker implements Runnable, Observer {
    private IService server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public AttractionsClientObjectWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Object response=handleRequest((Request)request);
                if (response!=null){
                   sendResponse((Response) response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    private Response handleRequest(Request request){
        Response response=null;

        if(request instanceof FindAgencyUserRequest){
            System.out.println("Login request");
            FindAgencyUserRequest logReq = (FindAgencyUserRequest) request;
            AgencyUserDTO agencyUser = logReq.getUser();
            try{
                return new FindAgencyUserResponse(server.filterAgencyUserByUserNameAndPassword(agencyUser.getUserName(), agencyUser.getPassword()));
            } catch (AttractionException e) {
                connected = false;
                return new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof FindAllTripsRequest){
            System.out.println("FindAllTripsRequest");
            try{
                Iterable<Trip> trips = server.findAllTrips();
                return new FindAllTripsResponse(trips);
            } catch (AttractionException e) {
                connected = false;
                return new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof SearchedTripsRequest) {
            System.out.println("SearchedTripsRequest");
            try {
                SearchedTripsRequest searchedTripsRequest = (SearchedTripsRequest) request;
                Iterable<Trip> trips = server.searchTripByTouristAttractionAndLeavingHour(searchedTripsRequest.getSearchedTripDTO().getTouristAttraction(), searchedTripsRequest.getSearchedTripDTO().getHour1(), searchedTripsRequest.getSearchedTripDTO().getHour2());
                return new SearchedTripsResponse(trips);
            } catch (AttractionException e) {
                connected = false;
                return new ErrorResponse(e.getMessage());
            }
        }

            if(request instanceof BookTripRequest) {
                System.out.println("BookTripRequest");
                try {
                    BookTripRequest bookTripRequest = (BookTripRequest) request;
                    server.updateTrip(bookTripRequest.getTrip());
                    return new OkResponse();
                } catch (AttractionException e) {
                    connected = false;
                    return new ErrorResponse(e.getMessage());
                }
            }

        if(request instanceof AddReservationRequest) {
            System.out.println("AddReservationRequest");
            try {
                AddReservationRequest addReservationRequest = (AddReservationRequest) request;
                server.addReservation(addReservationRequest.getReservation());
                return new OkResponse();
            } catch (AttractionException e) {
                connected = false;
                return new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof ReservationSizeRequest) {
            System.out.println("ReservationSizeRequest");
            try {
                return new ReservationSizeResponse(server.getReservationsSize());
            } catch (AttractionException e) {
                connected = false;
                return new ErrorResponse(e.getMessage());
            }
        }

        if (request instanceof LoginRequest){
            System.out.println("Login request ...");
            LoginRequest logReq=(LoginRequest)request;
            try {
                server.login(logReq.getAgencyUser(), this);
                return new OkResponse();
            } catch (AttractionException e) {
                connected=false;
                return new ErrorResponse(e.getMessage());
            }
        }

        if (request instanceof LogoutRequest){
            System.out.println("Logout request");
            LogoutRequest logReq=(LogoutRequest)request;
            try {
                server.logout(logReq.getAgencyUser(), this);
                connected=false;
                return new OkResponse();

            } catch (AttractionException e) {
                return new ErrorResponse(e.getMessage());
            }
        }

        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }

    @Override
    public void bookedTrip(Iterable<Trip> trips) throws AttractionException {
        System.out.println("Trip has been booked");
        try {
            sendResponse(new BookTripResponse(trips));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
