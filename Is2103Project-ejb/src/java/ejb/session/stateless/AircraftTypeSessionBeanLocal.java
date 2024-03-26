package ejb.session.stateless;

import entity.AircraftType;
import javax.ejb.Local;
import util.exception.NoAircraftTypeException;
import util.exception.UnknownPersistenceException;

@Local
public interface AircraftTypeSessionBeanLocal {

    public long createAircraft(AircraftType aircraft) throws UnknownPersistenceException;
    
    public AircraftType getAircraftType(String aircraftType) throws NoAircraftTypeException, UnknownPersistenceException;
}
