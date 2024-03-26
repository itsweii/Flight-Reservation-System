package ejb.session.stateless;

import entity.CabinConfiguration;
import java.util.List;
import javax.ejb.Local;
import util.exception.NoCabinConfigurationException;
import util.exception.UnknownPersistenceException;

@Local
public interface CabinConfigurationSessionBeanLocal {

    public CabinConfiguration getCabinConfiguration(CabinConfiguration c) throws NoCabinConfigurationException, UnknownPersistenceException;
    public void createConfig(CabinConfiguration cc);
    public List<CabinConfiguration> getCabinConfigurations(Long flightId);
}
