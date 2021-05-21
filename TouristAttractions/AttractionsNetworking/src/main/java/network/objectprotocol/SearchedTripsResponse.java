package network.objectprotocol;

import model.Trip;

public class SearchedTripsResponse implements Response{
    private Iterable<Trip> trips;

    public SearchedTripsResponse(Iterable<Trip> trips) {
        this.trips = trips;
    }

    public Iterable<Trip> getTrips() {
        return trips;
    }
}
