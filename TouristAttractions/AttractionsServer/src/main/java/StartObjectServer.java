import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import trip.repository.*;
import transformer.ThriftServer;
import transformer.ThriftService;

import java.io.IOException;
import java.util.Properties;


public class StartObjectServer {
    
    private static int defaultPort = 55555;
    
    public static void main(String[] args) {
       Properties serverProps=new Properties();
        try {
            serverProps.load(StartObjectServer.class.getResourceAsStream("/touristAttractionsServer.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find touristAttractionsServer.properties "+e);
            return;
        }

        /*AgencyUserRepository agencyUserRepository=new AgencyUserDBRepository(serverProps);
        TripRepository tripRepository=new TripDBRepository(serverProps);
        ReservationRepository reservationRepository=new ReservationDBRepository(serverProps);
        IService service = new Service(agencyUserRepository, tripRepository, reservationRepository);

        int serverPort=defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("touristAttractions.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number "+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+serverPort);
        AbstractServer server = new AttractionsObjectConcurrentServer(serverPort, service);
        try {
                server.start();
        } catch (ServerException e) {
                System.err.println("Error starting the server" + e.getMessage());
        }*/
        SessionFactory sessionFactory = null;

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exception "+e);
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy( registry );
        }


        AgencyUserRepository agencyUserDBRepository = new AgencyUserRepository(sessionFactory);
        //TripDBRepository tripDBRepository = new TripDBRepository(serverProps);
        ReservationDBRepository reservationDBRepository = new ReservationDBRepository(serverProps);

        /*tripDBRepository.add(new model.Trip(2,"Eiffel Tower", "Tetarom", LocalTime.parse("07:00:00"), 1000, 189));
        tripDBRepository.add(new model.Trip(3,"Colosseum", "RomeAirlines", LocalTime.parse("11:00:00"), 2000, 100));
        tripDBRepository.add(new model.Trip(4,"Bran Castle", "RoTrans", LocalTime.parse("08:00:00"), 100, 80));
        tripDBRepository.add(new model.Trip(5,"Acropolis", "AthensAirlines", LocalTime.parse("12:00:00"), 1100, 138));
        tripDBRepository.add(new model.Trip(6,"Acropolis", "Airlines", LocalTime.parse("09:00:00"), 2001, 0));
*/

        //tripDBRepository.findAll().forEach(System.out::println);
        //agencyUserDBRepository.findAll().forEach(System.out::println);
        //System.out.println(agencyUserDBRepository.filterAgencyUserByUserNameAndPassword("elo", "12345"));

        /*try {
            ThriftServer thriftService = new ThriftServer(agencyUserDBRepository, tripDBRepository, reservationDBRepository);
            ThriftService.Processor<ThriftServer> processor = new ThriftService.Processor<>(thriftService);
            TServerTransport serverTransport = new TServerSocket(55556);
            TServer thriftServer = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
            System.out.println("Server running...");
            thriftServer.serve();

        }catch (Exception ex){
            System.out.println("test");
            ex.printStackTrace();
        }*/

    }
}
