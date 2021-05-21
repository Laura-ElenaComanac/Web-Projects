package trip.repository;

import model.Reservation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ReservationDBRepository implements ReservationRepository{

    private final static Logger log = LogManager.getLogger(ReservationDBRepository.class);
    private JdbcUtils jdbcUtils;

    public ReservationDBRepository(Properties props){
        log.info("Initializing TripDBRepository with properties: {} ", props);
        jdbcUtils=new JdbcUtils(props);
    }

    @Override
    public void add(Reservation el){
        log.traceEntry("saving reservation {} ",el);
        Connection con=jdbcUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("INSERT INTO reservations VALUES (?,?,?,?,?,?)")){
            preStmt.setInt(1, el.getId());
            preStmt.setInt(2, el.getNrTickets());
            preStmt.setString(3, el.getClientName());
            preStmt.setString(4, el.getTelephone());
            preStmt.setInt(5, el.getAgencyUserId());
            preStmt.setInt(6, el.getTripId());
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
        log.traceEntry("DELETING reservation {} ", id);
        Connection con=jdbcUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("DELETE FROM reservations WHERE id=?")){
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
    public void update(Reservation el, Integer id){
        log.traceEntry("updating reservation {} ",el);
        Connection con=jdbcUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement(
                "UPDATE reservations SET id=?, nrTickets=?, clientName=?, telephone=?, agencyUserId=?, tripId=? WHERE id=?")){
            preStmt.setInt(1, el.getId());
            preStmt.setInt(2, el.getNrTickets());
            preStmt.setString(3, el.getClientName());
            preStmt.setString(4, el.getTelephone());
            preStmt.setInt(5, el.getAgencyUserId());
            preStmt.setInt(6, el.getTripId());
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
    public Reservation findById(Integer id){
        log.traceEntry("finding reservation by id {} ", id);
        Connection con=jdbcUtils.getConnection();
        Reservation reservation = null;

        try(PreparedStatement preStmt=con.prepareStatement("SELECT * FROM reservations WHERE id=?")) {
            preStmt.setInt(1, id);

            try(ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id2 = result.getInt("id");
                    int nrTickets = result.getInt("nrTickets");
                    String clientName = result.getString("clientName");
                    String telephone = result.getString("telephone");
                    int agencyUserId = result.getInt("agencyUserId");
                    int tripId = result.getInt("tripId");

                    reservation = new Reservation(id2, nrTickets, clientName, telephone, agencyUserId, tripId);
                }
            }
        } catch (SQLException e) {
            log.error(e);
            System.out.println("Error DB "+e);
        }

        log.traceExit(reservation);
        return reservation;
    }

    @Override
    public Iterable<Reservation> findAll() {
        Connection con=jdbcUtils.getConnection();
        List<Reservation> reservations=new ArrayList<>();

        try(PreparedStatement preStmt=con.prepareStatement("SELECT * FROM reservations")) {
            try(ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    int nrTickets = result.getInt("nrTickets");
                    String clientName = result.getString("clientName");
                    String telephone = result.getString("telephone");
                    int agencyUserId = result.getInt("agencyUserId");
                    int tripId = result.getInt("tripId");

                    Reservation reservation = new Reservation(id, nrTickets, clientName, telephone, agencyUserId, tripId);
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            log.error(e);
            System.out.println("Error DB "+e);
        }

        log.traceExit(reservations);
        return reservations;
    }

    @Override
    public int size() {
        log.traceEntry("SIZE of reservations");
        Connection con=jdbcUtils.getConnection();
        ResultSet result;

        try(PreparedStatement preStmt=con.prepareStatement("SELECT COUNT(*) FROM reservations")){
            result=preStmt.executeQuery();
            log.trace("SIZE {}", result);
            return result.getInt(1);
        }
        catch (SQLException ex){
            log.error(ex);
            System.out.println("Error DB "+ex);
        }

        log.traceExit();
        return 0;
    }

    @Override
    public List<Reservation> searchReservationByClientNameAndTelephone(String clientName, String telephone) {
        Connection con=jdbcUtils.getConnection();
        List<Reservation> reservations=new ArrayList<>();

        try(PreparedStatement preStmt=con.prepareStatement("SELECT * FROM reservations WHERE clientName=? AND telephone=?")) {
            preStmt.setString(1, clientName);
            preStmt.setString(2, telephone);

            try(ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    int nrTickets = result.getInt("nrTickets");
                    String clientName2 = result.getString("clientName");
                    String telephone2 = result.getString("telephone");
                    int agencyUserId = result.getInt("agencyUserId");
                    int tripId = result.getInt("tripId");

                    Reservation reservation = new Reservation(id, nrTickets, clientName2, telephone2, agencyUserId, tripId);
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            log.error(e);
            System.out.println("Error DB "+e);
        }

        log.traceExit(reservations);
        return reservations;
    }
}
