package ejb.session.singleton;

import ejb.session.stateless.AirportSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.AircraftType;
import entity.Airport;
import entity.Employee;
import entity.Partner;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.EmployeeType;
import util.exception.UnknownPersistenceException;
import ejb.session.stateless.AircraftTypeSessionBeanLocal;
import entity.AircraftConfiguration;
import entity.CabinConfiguration;
import entity.Flight;
import entity.FlightRoute;
import java.util.ArrayList;
import java.util.List;

@Singleton
@LocalBean
@Startup
public class StartupSessionBean {

    @EJB 
    private AircraftTypeSessionBeanLocal aircraftSessionBeanLocal;

    @EJB
    private AirportSessionBeanLocal airportSessionBeanLocal;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;
    
    @PostConstruct
    public void PostConstruct() {
        if (em.find(Employee.class, 1l) == null) {
            try {
                employeeSessionBeanLocal.createEmployee(new Employee(EmployeeType.FleetManager, "FleetManager name", "fleetmanager", "password"));
                employeeSessionBeanLocal.createEmployee(new Employee(EmployeeType.RoutePlanner, "RoutePlanner name", "routeplanner", "password"));
                employeeSessionBeanLocal.createEmployee(new Employee(EmployeeType.ScheduleManger, "ScheduleManger name", "schedulemanger", "password"));
                employeeSessionBeanLocal.createEmployee(new Employee(EmployeeType.SaleManager, "SaleManager name", "salemanager", "password"));
            } catch (UnknownPersistenceException e) {
                System.out.println(e.toString());
            }
        }
        
        if (em.find(Partner.class, 1l) == null) {
            try {
                partnerSessionBeanLocal.createPartner(new Partner("Partner name", "partner", "password"));
            } catch (UnknownPersistenceException e) {
                System.out.println(e.toString());
            }
        }
        
        if (em.find(Airport.class, 1l) == null) {
            try {
                airportSessionBeanLocal.createAirport(new Airport( "Changi", "SIN", "Singapore", "Singapore", "Singapore"));
                airportSessionBeanLocal.createAirport(new Airport( "Hong Kong", "HKG", "Chek Lap Kok", "Hong Kong", "China"));
                airportSessionBeanLocal.createAirport(new Airport( "Taoyuan", "TPE", "Taoyuan", "Taipei", "Taiwan R.O.C."));
                airportSessionBeanLocal.createAirport(new Airport( "Narita", "NRT", "Narita", "Chiba", "Japan"));
                airportSessionBeanLocal.createAirport(new Airport("Narita", "NRT", "Narita", "Chiba", "Japan"));
                airportSessionBeanLocal.createAirport(new Airport( "Sydney", "SYD", "Sydney", "New South Wales", "Australia"));
            } catch (UnknownPersistenceException e) {
                System.out.println(e.toString());
            }
        }
        
        if (em.find(AircraftType.class, 1l) == null) {
            try {
                aircraftSessionBeanLocal.createAircraft(new AircraftType("Boeing 737", 200));
                aircraftSessionBeanLocal.createAircraft(new AircraftType("Boeing 747", 400));
            } catch (UnknownPersistenceException e) {
                System.out.println(e.toString());
            }
        }
        
//        if (em.find(FlightRoute.class, 1l) == null) {
//            try {
//                Airport originAirport = em.find(Airport.class, 1l);
//                Airport destinationAirport = em.find(Airport.class, 3l);
//                Airport destinationAirport2 = em.find(Airport.class, 2l);
//                FlightRoute flightRoute = new FlightRoute(originAirport,destinationAirport);
//                FlightRoute flightRoute2 = new FlightRoute(originAirport,destinationAirport);
//                em.persist(flightRoute);
//                em.persist(flightRoute2);
//                FlightRoute returnRoute = new FlightRoute(destinationAirport,originAirport);
//                em.persist(returnRoute);
//                flightRoute.setReturnRoute(returnRoute);
//                returnRoute.setReturnRoute(flightRoute);
//            } catch (Exception e) {
//                System.out.println(e.toString());
//            }
//        }
//        
//        if (em.find(AircraftConfiguration.class, 1l) == null) {
//            try {
//                List<Integer> temp = new ArrayList<Integer>();
//                temp.add(1);
//                temp.add(1);
//                
//                CabinConfiguration cc = new CabinConfiguration("f", 1, 10, 2, temp);
//                CabinConfiguration cc2 = new CabinConfiguration("f", 1, 15, 2, temp);
//                em.persist(cc);
//                em.persist(cc2);
//                em.flush();
//                List<CabinConfiguration> temp2 = new ArrayList<CabinConfiguration>();
//                temp2.add(cc);
//                temp2.add(cc2);
//                
//                AircraftType at = em.find(AircraftType.class, 1l);
//                AircraftConfiguration ac = new AircraftConfiguration("tiny", 2, 100, temp2, at);
//                em.persist(ac);
//                
//                
//            } catch (Exception e) {
//                System.out.println(e.toString());
//            }
//        }
        System.out.println("StartupSessionBean.PostConstruct() done");
    }
}
