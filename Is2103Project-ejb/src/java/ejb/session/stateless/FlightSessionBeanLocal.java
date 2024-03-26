package ejb.session.stateless;

import entity.Employee;
import entity.Flight;
import java.util.List;
import javax.ejb.Local;
import util.exception.FlightExistException;
import util.exception.NoFlightFoundException;
import util.exception.UnknownPersistenceException;

@Local
public interface FlightSessionBeanLocal {
    public long createFlight(Flight flight);
    public List<Flight> retrieveAllFlight();
    public Flight retrieveFlight(String flightNumber)throws NoFlightFoundException;
    public void updateFlightDetail(Flight flight);
    public void deleteFlightRoute (String flightNum) throws NoFlightFoundException;

    public Long craeteFlight(String num, Long flightRouteId, Long aircraftConfigurationId) throws FlightExistException;

    public Long craeteReturnFlight(String num, Long flightRouteId, Long aircraftConfigurationId, Long returnFlightId) throws FlightExistException;

    public String printFlightDetails(String flightnum) throws NoFlightFoundException;
    
    public boolean isBeingUsed(Long flightId);
}
