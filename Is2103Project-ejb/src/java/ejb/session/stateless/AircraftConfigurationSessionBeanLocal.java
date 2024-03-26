package ejb.session.stateless;

import entity.AircraftConfiguration;
import java.util.List;
import javax.ejb.Local;
import util.exception.NoAircraftConfigurationException;
import util.exception.UnknownPersistenceException;

@Local
public interface AircraftConfigurationSessionBeanLocal {
    public AircraftConfiguration getAircraftConfiguration(String name) throws UnknownPersistenceException, NoAircraftConfigurationException;
    public long createAircraftConfiguration(AircraftConfiguration aircraftConfiguration);
    public List<AircraftConfiguration> getAllAircraftConfiguration();
    public String getCabinConfigurations(String name) throws UnknownPersistenceException, NoAircraftConfigurationException;
    public void updateAircraftConfiguration (AircraftConfiguration ac);
    
}
