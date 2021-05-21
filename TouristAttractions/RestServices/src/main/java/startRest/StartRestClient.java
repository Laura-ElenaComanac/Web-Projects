package startRest;

import clientRest.AttractionsClient;
import model.Trip;
import org.springframework.web.client.RestClientException;
import trip.serviceRest.AttractionsException;

import java.time.LocalTime;

public class StartRestClient {
    private final static AttractionsClient attractionsClient = new AttractionsClient();

    private static void show(Runnable task) {
        try {
            task.run();
        } catch (AttractionsException e) {
            System.out.println("Service exception: "+ e);
        }
    }

    public static void main(String[] args) {
        Trip trip = new Trip(10,"NewAttraction2", "NewTransport2", LocalTime.parse("10:00:00"), 1000, 10);
        Trip trip2 = new Trip(10,"NewAttraction3", "NewTransport3", LocalTime.parse("10:00:00"), 1000, 10);
        try{
            System.out.println("CREATE");
            show(()-> System.out.println(attractionsClient.create(trip)));

            System.out.println("UPDATE");
            show(()-> System.out.println(attractionsClient.update(trip2, trip2.getId())));

            System.out.println("FIND ALL");
            show(()->{
               Trip[] trips = attractionsClient.getAll();
               for(Trip t : trips){
                   System.out.println(t);
               }
            });

            System.out.println("FIND BY ID");
            show(()-> System.out.println(attractionsClient.findOne(trip2.getId())));

            System.out.println("DELETE");
            show(()-> System.out.println(attractionsClient.delete(10)));

            System.out.println("FIND ALL (AFTER DELETED)");
            show(()->{
                Trip[] trips = attractionsClient.getAll();
                for(Trip t : trips){
                    System.out.println(t);
                }
            });
        }
        catch(RestClientException ex){
            System.out.println("RestClientException: " + ex.getMessage());
        }
    }
}
