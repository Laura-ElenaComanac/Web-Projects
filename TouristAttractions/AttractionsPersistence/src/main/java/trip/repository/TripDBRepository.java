package trip.repository;

import model.Trip;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class TripDBRepository implements TripRepository{

    private JdbcUtils jdbcUtils;
    private final static Logger log = LogManager.getLogger(TripDBRepository.class);

    public TripDBRepository() {
        try {
            Properties props = new Properties();
            props.load(TripRepository.class.getResourceAsStream("/touristAttractionsServer.properties"));
            log.info("Initializing TripDBRepository with properties: {} ", props);
            jdbcUtils = new JdbcUtils(props);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void add(Trip el){
        log.traceEntry("saving trip {} ",el);
        Connection con=jdbcUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("INSERT INTO trips VALUES (?,?,?,?,?,?)")){
            preStmt.setInt(1, el.getId());
            preStmt.setString(2,el.getTouristAttraction());
            preStmt.setString(3,el.getTransportCompany());
            preStmt.setString(4, el.getLeavingHour().toString());
            preStmt.setDouble(5,el.getPrice());
            preStmt.setInt(6,el.getNrSeats());
            int result=preStmt.executeUpdate();
            log.trace("Saved {} instances", result);
        }
        catch (SQLException ex){
            log.error(ex);
            System.out.println("Error DB "+ex);
        }

        log.traceExit();
    }

    @Override
    public void delete(Integer id){
        log.traceEntry("DELETING trip {} ", id);
        Connection con=jdbcUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("DELETE FROM trips WHERE id=?")){
            preStmt.setInt(1, id);
            int result=preStmt.executeUpdate();
            log.trace("Deleted {} instances", result);
        }
        catch (SQLException ex){
            log.error(ex);
            System.out.println("Error DB "+ex);
        }

        log.traceExit();
    }

    @Override
    public void update(Trip el, Integer id){
        log.traceEntry("updating trip {} ",el);
        Connection con=jdbcUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement(
                "UPDATE trips SET id=?, touristAttraction=?, transportCompany=?, leavingHour=TIME(?), price=?, nrSeats=? WHERE id=?")){
            preStmt.setInt(1, el.getId());
            preStmt.setString(2, el.getTouristAttraction());
            preStmt.setString(3, el.getTransportCompany());
            preStmt.setString(4, el.getLeavingHour().toString());
            preStmt.setDouble(5, el.getPrice());
            preStmt.setInt(6, el.getNrSeats());
            preStmt.setInt(7, id);
            int result=preStmt.executeUpdate();
            log.trace("Updated {} instances", result);
        }
        catch (SQLException ex){
            log.error(ex);
            System.out.println("Error DB "+ ex);
        }

        log.traceExit();
    }


    @Override
    public Trip findById(Integer id){
        log.traceEntry("finding trip by id {} ", id);
        Connection con=jdbcUtils.getConnection();
        Trip trip = null;

        try(PreparedStatement preStmt=con.prepareStatement("SELECT * FROM trips WHERE id=?")) {
            preStmt.setInt(1, id);

            try(ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id2 = result.getInt("id");
                    String touristAttraction = result.getString("touristAttraction");
                    String transportCompany = result.getString("transportCompany");
                    String leavingHour = result.getString("leavingHour");
                    Double price = result.getDouble("price");
                    int nrSeats = result.getInt("nrSeats");

                    trip = new Trip(id2, touristAttraction, transportCompany, LocalTime.parse(leavingHour), price, nrSeats);
                }
            }
        } catch (SQLException e) {
            log.error(e);
            System.out.println("Error DB "+e);
        }

        log.traceExit(trip);
        return trip;
    }

    @Override
    public Iterable<Trip> findAll() {
        Connection con=jdbcUtils.getConnection();
        List<Trip> trips=new ArrayList<>();

        try(PreparedStatement preStmt=con.prepareStatement("SELECT * FROM trips")) {
            try(ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String touristAttraction = result.getString("touristAttraction");
                    String transportCompany = result.getString("transportCompany");
                    String leavingHour = result.getString("leavingHour");
                    Double price = result.getDouble("price");
                    int nrSeats = result.getInt("nrSeats");

                    Trip trip = new Trip(id, touristAttraction, transportCompany, LocalTime.parse(leavingHour), price, nrSeats);
                    trips.add(trip);
                }
            }
        } catch (SQLException e) {
            log.error(e);
            System.out.println("Error DB "+e);
        }

        log.traceExit(trips);
        return trips;
    }

    @Override
    public int size() {
        log.traceEntry("SIZE of trips");
        Connection con=jdbcUtils.getConnection();
        int result;

        try(PreparedStatement preStmt=con.prepareStatement("SELECT COUNT(*) FROM trips")){
            result=preStmt.executeUpdate();
            log.trace("SIZE {}", result);
            return result;
        }
        catch (SQLException ex){
            log.error(ex);
            System.out.println("Error DB "+ex);
        }

        log.traceExit();
        return 0;
    }

    @Override
    public List<Trip> searchTripByTouristAttractionAndLeavingHour(String touristAttraction, LocalTime hour1, LocalTime hour2) {
        Connection con=jdbcUtils.getConnection();
        List<Trip> trips=new ArrayList<>();

        try(PreparedStatement preStmt=con.prepareStatement("SELECT * FROM trips WHERE touristAttraction=? AND leavingHour BETWEEN ? AND ?")) {
            preStmt.setString(1, touristAttraction);
            preStmt.setString(2, hour1.toString());
            preStmt.setString(3, hour2.toString());

            try(ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String touristAttraction2 = result.getString("touristAttraction");
                    String transportCompany = result.getString("transportCompany");
                    //System.out.println(result.getTime("leavingHour"));
                    String leavingHour = result.getString("leavingHour");
                    Double price = result.getDouble("price");
                    int nrSeats = result.getInt("nrSeats");

                    Trip trip = new Trip(id, touristAttraction2, transportCompany, LocalTime.parse(leavingHour), price, nrSeats);
                    trips.add(trip);
                }
            }
        } catch (SQLException e) {
            log.error(e);
            System.out.println("Error DB "+e);
        }

        log.traceExit(trips);
        return trips;
    }
}
