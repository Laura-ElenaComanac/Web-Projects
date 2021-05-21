package network.objectprotocol;

import model.Trip;

public class FindAllTripsResponse implements Response{
    private Iterable<Trip> trips;

    public FindAllTripsResponse(Iterable<Trip> trips) {
        this.trips = trips;
    }
    
    public Iterable<Trip> getTrips() {
        return trips;
    }
}
