package model;

import java.util.Objects;

public class Reservation extends EntityID<Integer> {
    private int agencyUserId;
    private int tripId;
    private int nrTickets;
    private String clientName;
    private String telephone;

    public Reservation(int id, int nrTickets, String clientName, String telephone, int agencyUserId, int tripId) {
        this.agencyUserId = agencyUserId;
        this.tripId = tripId;
        this.nrTickets = nrTickets;
        this.clientName = clientName;
        this.telephone = telephone;
        this.setId(id);
    }

    public int getAgencyUserId() {
        return agencyUserId;
    }

    public int getTripId() {
        return tripId;
    }

    public void setAgencyUserId(int agencyUserId) {
        this.agencyUserId = agencyUserId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getNrTickets() {
        return nrTickets;
    }

    public void setNrTickets(int nrTickets) {
        this.nrTickets = nrTickets;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "nrTickets=" + nrTickets +
                ", clientName='" + clientName + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
