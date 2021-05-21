package network.dto;

import java.io.Serializable;
import java.time.LocalTime;

public class SearchedTripDTO implements Serializable {
    String touristAttraction;
    LocalTime hour1;
    LocalTime hour2;

    public SearchedTripDTO(String touristAttraction, LocalTime hour1, LocalTime hour2) {
        this.touristAttraction = touristAttraction;
        this.hour1 = hour1;
        this.hour2 = hour2;
    }

    public String getTouristAttraction() {
        return touristAttraction;
    }

    public LocalTime getHour1() {
        return hour1;
    }

    public LocalTime getHour2() {
        return hour2;
    }
}
