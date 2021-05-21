package trip.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class JdbcUtils {
    private Properties jdbcProps;
    private static final Logger logger= LogManager.getLogger(JdbcUtils.class);
    public JdbcUtils(Properties props){
        jdbcProps=props;
    }
    private  Connection instance=null;
    private Connection getNewConnection(){
        logger.traceEntry();

        String url=jdbcProps.getProperty("attractions.jdbc.url");
        //String url = "jdbc:sqlite:Z:\\Desktop\\Proiecte_MPP\\temalab2-Laura-ElenaOlaru\\TemaLab3BD\\databases\\touristAttractions.db";
        //String user=jdbcProps.getProperty("trips.jdbc.user");
        //String pass=jdbcProps.getProperty("trips.jdbc.pass");
        logger.info("trying to connect to database ... {}",url);
        //logger.info("user: {}",user);
        //logger.info("pass: {}", pass);
        Connection con=null;

        try {
            //if (user!=null && pass!=null)
            //con= DriverManager.getConnection(url,user,pass);
            //else
            con=DriverManager.getConnection(url);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error getting connection "+e);
        }
        return con;
    }

    public Connection getConnection(){
        logger.traceEntry();
        try {
            if (instance==null || instance.isClosed())
                instance=getNewConnection();

        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(instance);
        return instance;
    }
}
