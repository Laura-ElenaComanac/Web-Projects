package network.objectprotocol;

import model.Reservation;

public class AddReservationRequest implements Request{
    Reservation reservation;

    public AddReservationRequest(Reservation reservation) {
        this.reservation = reservation;
    }

    public Reservation getReservation() {
        return reservation;
    }
}
