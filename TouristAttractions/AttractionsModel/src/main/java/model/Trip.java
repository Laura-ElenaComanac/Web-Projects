package model;

import java.time.LocalTime;
import java.util.Objects;

public class Trip extends EntityID<Integer> {
    private String touristAttraction;
    private String transportCompany;
    private LocalTime leavingHour;
    private double price;
    private int nrSeats;

    public Trip(){}

    public Trip(int id, String touristAttraction, String transportCompany, LocalTime leavingHour, double price, int nrSeats) {
        this.touristAttraction = touristAttraction;
        this.transportCompany = transportCompany;
        this.leavingHour = leavingHour;
        this.price = price;
        this.nrSeats = nrSeats;
        this.setId(id);
    }

    public String getTouristAttraction() {
        return touristAttraction;
    }

    public void setTouristAttraction(String touristAttraction) {
        this.touristAttraction = touristAttraction;
    }

    public String getTransportCompany() {
        return transportCompany;
    }

    public void setTransportCompany(String transportCompany) {
        this.transportCompany = transportCompany;
    }

    public LocalTime getLeavingHour() {
        return leavingHour;
    }

    public void setLeavingHour(LocalTime leavingHour) {
        this.leavingHour = leavingHour;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNrSeats() {
        return nrSeats;
    }

    public void setNrSeats(int nrSeats) {
        this.nrSeats = nrSeats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return Objects.equals(getId(), trip.getId());
    }

    @Override
    public String toString() {
        return "Trip{" +
                "touristAttraction='" + touristAttraction + '\'' +
                ", transportCompany='" + transportCompany + '\'' +
                ", leavingHour=" + leavingHour +
                ", price=" + price +
                ", nrSeats=" + nrSeats +
                '}';
    }
}
