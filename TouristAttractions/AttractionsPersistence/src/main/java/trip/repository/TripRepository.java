package trip.repository;

import model.Trip;

import java.time.LocalTime;
import java.util.List;

public interface TripRepository extends Repository<Trip, Integer>{
    public List<Trip> searchTripByTouristAttractionAndLeavingHour(String touristAttraction, LocalTime hour1, LocalTime hour2);
}
