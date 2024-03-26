package ejb.session.stateless;

import entity.Airport;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.NoAirportException;
import util.exception.UnknownPersistenceException;

@Stateless
public class AirportSessionBean implements AirportSessionBeanRemote, AirportSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;
    
    @Override
    public long createAirport(Airport airport) throws UnknownPersistenceException {
        System.out.println("AirportSessionBean.createAirport(Airport airport)");
        em.persist(airport);
        em.flush();
        System.out.println(String.format("Airport (ID: %d) successfully created", airport.getAirportId()));
        System.out.println("AirportSessionBean.createAirport(Airport airport) done");
        return airport.getAirportId();
    }
    
    @Override
    public Airport findAirportByIATA(String IATA) throws NoAirportException {
        
        
        Query query = em.createQuery("SELECT a FROM Airport a WHERE a.IataCode = :IataCode");
        query.setParameter("IataCode", IATA);
        
        try
        {
            Airport airport = (Airport)query.getSingleResult();
            return airport;
        }
        catch (NoResultException ex)
        { 
            throw new NoAirportException("No airport found with IATA code: " + IATA);
        }
    }

    
}
