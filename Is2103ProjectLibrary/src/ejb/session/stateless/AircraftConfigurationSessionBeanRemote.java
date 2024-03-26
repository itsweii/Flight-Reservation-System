package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.CabinConfiguration;
import java.util.List;
import javax.ejb.Remote;
import util.exception.NoAircraftConfigurationException;
import util.exception.UnknownPersistenceException;

@Remote
public interface AircraftConfigurationSessionBeanRemote {

    public AircraftConfiguration getAircraftConfiguration(String name) throws UnknownPersistenceException, NoAircraftConfigurationException;

    public long createAircraftConfiguration(AircraftConfiguration aircraftConfiguration);

    public List<AircraftConfiguration> getAllAircraftConfiguration();
    public String getCabinConfigurations(String name) throws UnknownPersistenceException, NoAircraftConfigurationException;
    public void updateAircraftConfiguration (AircraftConfiguration ac);
    

    //public String printAircraftConfiguration(String name) throws UnknownPersistenceException, NoAircraftConfigurationException;
    
}
