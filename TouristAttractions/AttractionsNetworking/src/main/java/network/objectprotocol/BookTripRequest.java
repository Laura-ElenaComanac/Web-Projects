package network.objectprotocol;

import model.Trip;

public class BookTripRequest implements Request{
    Trip trip;

    public BookTripRequest(Trip trip) {
        this.trip = trip;
    }

    public Trip getTrip() {
        return trip;
    }
}
