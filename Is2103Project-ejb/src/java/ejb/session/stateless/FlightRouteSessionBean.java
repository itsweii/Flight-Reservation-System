/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Airport;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedulePlan;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.FlightRouteExistException;
import util.exception.NoFlightRouteException;

/**
 *
 * @author mw
 */
@Stateless
public class FlightRouteSessionBean implements FlightRouteSessionBeanRemote, FlightRouteSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;

    @Override
    public Long createFlightRoute(FlightRoute flightRoute) throws FlightRouteExistException {
        
        try {
            findFlightRoute(flightRoute.getOrigin(), flightRoute.getDestination());
            throw new FlightRouteExistException("FlightRoute already exists for the specified origin and destination.");
            
        } catch (NoFlightRouteException e) {
                em.persist(flightRoute);
                em.flush();
                return flightRoute.getFlightRouteId();
        }
    }
    
    @Override
    public void addReturnRoute(Long flightRouteId, Long returnRouteId) {
        FlightRoute flight = em.find(FlightRoute.class, flightRouteId);
        FlightRoute returna = em.find(FlightRoute.class, returnRouteId);

        flight.setReturnRoute(returna);
        returna.setReturnRoute(flight);
    }
    
    @Override
    public void update(FlightRoute r) {
        em.merge(r);
        em.flush();
    }
    
    @Override
    public FlightRoute findFlightRoute(Airport origin, Airport destination) throws NoFlightRouteException 
    {
        try {
            Query query = em.createQuery(
                "SELECT fr FROM FlightRoute fr WHERE fr.origin = :origin AND fr.destination = :destination");

            query.setParameter("origin", origin);
            query.setParameter("destination", destination);
            return (FlightRoute) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new NoFlightRouteException("No flight route found!");
        }
    }
    
    @Override
    public String deleteFlightRoute(Airport origin, Airport destination) throws NoFlightRouteException{
        Query q = em.createQuery("SELECT fr FROM FlightRoute fr WHERE fr.origin = :origin AND fr.destination = :destination");
        q.setParameter("origin", origin);
        q.setParameter("destination", destination);
        FlightRoute fr = (FlightRoute) q.getSingleResult();
        String OD = fr.toString();
        
        Query query = em.createQuery("SELECT fr FROM Flight fr");
        List<Flight> list = query.getResultList();
        for (Flight flight : list) {
            if(flight.getFlightRoute().getFlightRouteId() == fr.getFlightRouteId()) {
                fr.setIsDisabled(true);
                OD += " disabled";
                break;
            }
        }
        if (!fr.isIsDisabled()) {
            if (fr.getReturnRoute() != null) {
                fr.getReturnRoute().setReturnRoute(null);
            }
            em.remove(fr);
            em.flush();
            OD += " deleted";
        }
        return OD;
    }
   
     @Override
    public List<FlightRoute> retrieveAllFlightRoutes() {
        Query query = em.createQuery("SELECT fr FROM FlightRoute fr ORDER BY fr.origin ASC");
        List <FlightRoute> fr = query.getResultList();
        List<FlightRoute> orderedList = new ArrayList<>();

        Iterator<FlightRoute> iterator = fr.iterator();
        
        while (iterator.hasNext()) {
            FlightRoute route = iterator.next();

            if (route.getReturnRoute() != null && fr.contains(route.getReturnRoute())) {
                orderedList.add(route);
                orderedList.add(route.getReturnRoute());
                iterator.remove();
               
            } 
            if(route.getReturnRoute() == null) {
                orderedList.add(route);
                iterator.remove();
            }
        }

        return orderedList;
    }

   
}
