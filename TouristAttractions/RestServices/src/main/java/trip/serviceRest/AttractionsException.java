package trip.serviceRest;

public class AttractionsException extends RuntimeException {
    public AttractionsException(Exception e) {
        super(e);
    }

    public AttractionsException(String message) {
        super(message);
    }
}