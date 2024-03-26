/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightRoute;
import java.util.List;
import javax.ejb.Remote;
import util.exception.FlightExistException;
import util.exception.FlightRouteExistException;
import util.exception.NoFlightRouteException;

/**
 *
 * @author mw
 */
@Remote
public interface FlightRouteSessionBeanRemote {
    public Long createFlightRoute(FlightRoute flightRoute) throws FlightRouteExistException;
    public void addReturnRoute(Long flightRouteId,Long returnRouteId);
    public List<FlightRoute> retrieveAllFlightRoutes();
    public FlightRoute findFlightRoute(Airport origin, Airport destination) throws NoFlightRouteException;
    public String deleteFlightRoute(Airport origin, Airport destination) throws NoFlightRouteException;
    public void update(FlightRoute r);
}
