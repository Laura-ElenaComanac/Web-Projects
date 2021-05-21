package trip.serviceRest;

import model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trip.repository.TripDBRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/touristAttractions/trip")
@CrossOrigin
public class AttractionsController {
    @Autowired
    private TripDBRepository tripDBRepository;

    @RequestMapping(method=RequestMethod.GET)
    public List<Trip> getAll(){
        List<Trip> trips = StreamSupport
                .stream(tripDBRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return trips;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id){
        Trip trip = tripDBRepository.findById(id);
        if(trip == null)
            return new ResponseEntity<>("Trip not found!", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(trip, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Trip create(@RequestBody Trip trip){
        Integer maxid = getAll().stream()
                .map(t->{return t.getId();})
                .max(Integer::compare).get();
        trip.setId(maxid+1);

        try {
            tripDBRepository.add(trip);
        }
        catch (Exception exc){
            System.out.println(exc.getMessage());
        }
        return trip;
    }

    @RequestMapping(value="/{id}", method = RequestMethod.PUT)
    public Trip update(@RequestBody Trip trip, @PathVariable int id){
//        Integer id = getAll().stream()
//                .filter(t -> {return t.equals(trip);})
//                .map(t->t.getId())
//                .collect(Collectors.toList()).get(0);
//        trip.setId(id);

        try {
            tripDBRepository.update(trip, id);
        }
        catch (Exception exc){
            System.out.println(exc.getMessage());
        }
        return trip;
    }

    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable int id){
        try{
            tripDBRepository.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception exc){
            System.out.println(exc.getMessage());
            return new ResponseEntity<String>(exc.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
