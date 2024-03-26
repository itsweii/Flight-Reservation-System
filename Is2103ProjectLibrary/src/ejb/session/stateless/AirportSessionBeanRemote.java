package ejb.session.stateless;

import entity.Airport;
import javax.ejb.Remote;
import util.exception.NoAirportException;
import util.exception.UnknownPersistenceException;

@Remote
public interface AirportSessionBeanRemote {
    public long createAirport(Airport airport) throws UnknownPersistenceException;
    public Airport findAirportByIATA(String IATA) throws NoAirportException;
}
