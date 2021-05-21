package trip.repository;

import model.AgencyUser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class AgencyUserRepository {
    private SessionFactory sessionFactory;

    public AgencyUserRepository(SessionFactory sessionFactory){
       this.sessionFactory = sessionFactory;
    }

    /*static void close() {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }*/

    public Iterable<AgencyUser> findAll() {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                List<AgencyUser> agencyUsers =
                        session.createQuery("FROM AgencyUser", AgencyUser.class)
                                .list();
                tx.commit();
                return agencyUsers;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        return null;
    }
    public AgencyUser filterAgencyUserByUserNameAndPassword(String userName, String password) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                List<AgencyUser> agencyUsers =
                        session.createQuery("FROM AgencyUser WHERE userName=:u AND password=:p", AgencyUser.class)
                                .setString("u", userName).setString("p", password).list();
                AgencyUser agencyUser = null;
                for (AgencyUser a : agencyUsers) {
                    agencyUser = a;
                }
                tx.commit();
                return agencyUser;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        return null;
    }

    public void delete(AgencyUser agencyUser) {
    }

    public AgencyUser findById(Integer id) {
        return null;
    }

    public void update(AgencyUser agencyUser, Integer id) {
    }

    public void add(AgencyUser agencyUser) {
    }

    public int size() {
        return 0;
    }
}