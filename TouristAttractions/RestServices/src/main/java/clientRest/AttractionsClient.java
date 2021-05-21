package clientRest;

import model.Trip;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import trip.serviceRest.AttractionsException;

import java.util.concurrent.Callable;

public class AttractionsClient {
    public static final String URL = "http://localhost:8080/touristAttractions/trip";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable){
        try{
            return callable.call();
        }
        catch (ResourceAccessException | HttpClientErrorException e){
            throw new AttractionsException(e);
        }
        catch (Exception exc) {
            throw new AttractionsException(exc);
        }
    }

    public Trip[] getAll(){
        return execute(()->restTemplate.getForObject(URL,Trip[].class));
    }

    public Trip create(Trip trip){
        return execute(()->restTemplate.postForObject(URL, trip, Trip.class));
    }

    public Trip update(Trip trip, Integer id){
        return execute(()->{restTemplate.put(URL+'/'+id.toString(),trip);
        return trip;
        });
    }

    public ResponseEntity<?> delete(Integer id){
        return execute(()->{restTemplate.delete(URL+'/'+id);
        return new ResponseEntity<>(id, HttpStatus.OK);
        });
    }

    public Trip findOne(Integer id){
        return execute(()->restTemplate.getForObject(URL+'/'+id.toString(), Trip.class));
    }
}
