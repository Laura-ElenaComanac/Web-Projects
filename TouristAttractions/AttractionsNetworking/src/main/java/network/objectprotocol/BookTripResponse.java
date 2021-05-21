package network.objectprotocol;

import model.Trip;

public class BookTripResponse implements UpdateResponse {
    private Iterable<Trip> trips;

    public BookTripResponse(Iterable<Trip> trips){this.trips = trips;}

    public Iterable<Trip> getTrips(){return trips;}
}
