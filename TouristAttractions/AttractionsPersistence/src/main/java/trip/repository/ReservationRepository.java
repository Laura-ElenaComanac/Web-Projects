package trip.repository;

import model.Reservation;

import java.util.List;

public interface ReservationRepository extends Repository<Reservation, Integer> {
    public List<Reservation> searchReservationByClientNameAndTelephone(String clientName, String telephone);
}
