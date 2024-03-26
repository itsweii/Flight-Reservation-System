package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.CabinConfiguration;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedulePlan;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.FlightExistException;
import util.exception.NoFlightFoundException;

@Stateless
public class FlightSessionBean implements FlightSessionBeanRemote, FlightSessionBeanLocal {

    @EJB
    private CabinConfigurationSessionBeanLocal cabinConfigurationSessionBeanLocal;

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;
    
 
    @Override
    public long createFlight(Flight flight) {
        em.persist(flight);
                
                
        em.flush();
        System.out.println("flight " + flight.getFlightId() + " " + flight.getFlightNum() +
                flight.getAircraftConfiguration() + " created");
        return flight.getFlightId();
    }
    
    @Override
    public List<Flight> retrieveAllFlight() {
        Query query = em.createQuery("SELECT f FROM Flight f ORDER BY f.flightNumber ASC");
        List <Flight> fr = query.getResultList();
        List<Flight> orderedList = new ArrayList<>();
        Iterator<Flight> iterator = fr.iterator();
        
        while (iterator.hasNext()) {
            Flight flight = iterator.next();

            if (flight.getReturnFlight() != null && fr.contains(flight.getReturnFlight())) {
                orderedList.add(flight);
                orderedList.add(flight.getReturnFlight());
                iterator.remove();
               
            } 
            if(flight.getReturnFlight() == null) {
                orderedList.add(flight);
                iterator.remove();
            }
        }

        return orderedList;
    }
    
    @Override
    public Flight retrieveFlight(String flightNumber)throws NoFlightFoundException {
        Query query = em.createQuery("SELECT f FROM Flight f WHERE f.flightNumber = :flightnumber");
        query.setParameter("flightnumber", flightNumber);
        try {
            System.out.println("test");
            return (Flight)query.getSingleResult();
        } catch (NoResultException e) {
            throw new NoFlightFoundException("no flight found");
        } 
    }
    
    @Override
    public void updateFlightDetail(Flight flight) {
        em.merge(flight);
        em.flush();
    }
    
    @Override
    public void deleteFlightRoute (String flightNum) throws NoFlightFoundException {
        Query query = em.createQuery("SELECT f FROM Flight f WHERE f.flightNumber = :flightnumber");
        query.setParameter("flightnumber", flightNum);
        try {
            Flight flight = (Flight) query.getSingleResult();
            
            if (isBeingUsed(flight.getFlightId())) {
                flight.setIsDisabled(true);
            } else {
                //return flight
                Flight returnflight = flight.getReturnFlight();
                if (returnflight != null) {
                    if (returnflight.isIsReturnFlight()) returnflight.setIsReturnFlight(false);
                    returnflight.setReturnFlight(null);
                    }
                em.remove(flight);
            }
        } catch (NoResultException e) {
            throw new NoFlightFoundException();
        }
    }
    
    @Override
    public boolean isBeingUsed(Long flightId) {
        Query query = em.createQuery("SELECT f FROM FlightSchedulePlan f");
        List<FlightSchedulePlan> list = query.getResultList();
        for (FlightSchedulePlan flightSchedulePlan : list) {
            if (flightSchedulePlan.getFlightNumber() == em.find(Flight.class, flightId).getFlightNum()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Long craeteFlight(String num, Long flightRouteId, Long aircraftConfigurationId) throws FlightExistException {
        Query query = em.createQuery("SELECT f FROM Flight f WHERE f.flightNumber = :flightNumber");
        query.setParameter("flightNumber", num);
        
        AircraftConfiguration config = em.find(AircraftConfiguration.class, aircraftConfigurationId);
        FlightRoute flightRoute = em.find(FlightRoute.class, flightRouteId);
        
        Flight flight = null;
        try {
            flight = (Flight)query.getSingleResult();
            throw new FlightExistException();
        } catch (NoResultException e) {
        }
        
        flight = new Flight(num);
        em.persist(flight);
        System.out.println("sad");
        flight.setAircraftConfiguration(config);
        flight.setFlightRoute(flightRoute);
        System.out.println(flight.getFlightRoute());
        System.out.println(flight.getAircraftConfiguration());
        System.out.println("fight");
        System.out.println(flight);
        em.flush();
        return flight.getFlightId();
    }
    
    @Override
    public Long craeteReturnFlight(String num, Long flightRouteId, Long aircraftConfigurationId, Long returnFlightId) throws FlightExistException {
        Query query = em.createQuery("SELECT f FROM Flight f WHERE f.flightNumber = :flightNumber");
        query.setParameter("flightNumber", num);

        AircraftConfiguration config = em.find(AircraftConfiguration.class, aircraftConfigurationId);
        FlightRoute flightRoute = em.find(FlightRoute.class, flightRouteId);

        Flight returnFlight = em.find(Flight.class, returnFlightId);

        Flight flight = null;
        try {
            flight = (Flight)query.getSingleResult();
            throw new FlightExistException();
        } catch (NoResultException e) {
        }

        flight = new Flight(num);
        em.persist(flight);
        flight.setIsReturnFlight(true);
        flight.setAircraftConfiguration(config);
        flight.setFlightRoute(flightRoute);
        flight.setReturnFlight(returnFlight);
        returnFlight.setReturnFlight(flight);
        em.flush();

        return flight.getFlightId();
    }
    
    @Override
    public String printFlightDetails(String flightnum) throws NoFlightFoundException {
        Flight flight = this.retrieveFlight(flightnum);
        AircraftConfiguration ac = em.find(AircraftConfiguration.class, flight.getAircraftConfiguration().getAircraftConfigurationId());
        
        String out = "\nFlight " + flightnum + "\n\nOrigin and Destination:\n" + flight.getFlightRoute().toString();
        out += "\n\n" + ac.getAircraftType().toString();
        out += "\n\nAircraft Configuration:\n" + ac.toString();
        
        StringBuilder sb = new StringBuilder(String.format("%-20s%-20s%-20s%-30s%-20s%-20s\n",
                "CabinClass", "NumberOfAisle", "NumberOfRow", 
                "NumberOfSeatAbreast", "SeatConfiguration", "MaxCapacity"));
        for (CabinConfiguration cc : ac.getCabinConfigurations()) {
                sb.append(String.format("%-20s%-20d%-20d%-30d%-20s%-20d\n",
                        cc.getCabinclass(),
                        cc.getNumOfAisle(),
                        cc.getNumberOfRow(),
                        cc.getNumberOfSeatAbreast(),
                        cc.printSeatConfiguration(),
                        cc.getCabinCapacity()
                        ));
            }
        return out + sb.toString();
    }
}
