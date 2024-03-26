package ejb.session.stateless;

import entity.AircraftType;
import javax.ejb.Remote;
import util.exception.NoAircraftTypeException;
import util.exception.UnknownPersistenceException;

@Remote
public interface AircraftTypeSessionBeanRemote {
    public long createAircraft(AircraftType aircraft) throws UnknownPersistenceException;

    public AircraftType getAircraftType(String aircraftType) throws NoAircraftTypeException, UnknownPersistenceException;
}
