package ejb.session.stateless;

import entity.CabinConfiguration;
import java.util.List;
import javax.ejb.Remote;
import util.exception.NoCabinConfigurationException;
import util.exception.UnknownPersistenceException;

@Remote
public interface CabinConfigurationSessionBeanRemote {
    public CabinConfiguration getCabinConfiguration(CabinConfiguration c) throws NoCabinConfigurationException, UnknownPersistenceException;
    public void createConfig(CabinConfiguration cc);

    public List<CabinConfiguration> getCabinConfigurations(Long flightId);

    public CabinConfiguration getCabinConfiguration(String CabinClass) throws NoSuchFieldException;
}
