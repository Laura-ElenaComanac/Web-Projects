package utils;

import model.Trip;
import service.AttractionException;

public interface Observer {
    void bookedTrip(Iterable<Trip> trips) throws AttractionException;
}
