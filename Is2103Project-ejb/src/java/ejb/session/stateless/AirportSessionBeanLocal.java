package ejb.session.stateless;

import entity.Airport;
import javax.ejb.Local;
import util.exception.NoAirportException;
import util.exception.UnknownPersistenceException;

@Local
public interface AirportSessionBeanLocal {
    public long createAirport(Airport airport) throws UnknownPersistenceException;
    public Airport findAirportByIATA(String IATA) throws NoAirportException;
    
}
