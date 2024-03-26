package ejb.session.stateless;

import entity.AircraftType;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.NoAircraftTypeException;
import util.exception.UnknownPersistenceException;

@Stateless
public class AircraftTypeSessionBean implements AircraftTypeSessionBeanRemote, AircraftTypeSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;
    
    @Override
    public long createAircraft(AircraftType aircraft) throws UnknownPersistenceException {
        System.out.println("AircraftSessionBean.createAircraft(Aircraft aircraft)");
        em.persist(aircraft);
        em.flush();
        System.out.println(String.format("Aircraft (ID: %d) successfully created", aircraft.getAircraftId()));
        System.out.println("AircraftSessionBean.createAircraft(Aircraft aircraft) done");
        return aircraft.getAircraftId();
    }
    
    @Override
    public AircraftType getAircraftType(String aircraftType) throws NoAircraftTypeException, UnknownPersistenceException {
        System.out.println("AircraftTypeSessionBean.getAircraftType(String aircraftType)");
        Query query = em.createQuery("SELECT e FROM AircraftType e WHERE e.name = :aircraftType");
        query.setParameter("aircraftType", aircraftType);
        
        AircraftType aircaft = null;
        try {
            aircaft = (AircraftType) query.getSingleResult();
            System.out.println(aircaft);
        } catch (NoResultException e) {
            throw new NoAircraftTypeException();
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
        System.out.println("AircraftTypeSessionBean.getAircraftType(String aircraftType) done");
        return aircaft;
    }
}
