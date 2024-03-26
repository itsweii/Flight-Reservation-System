package frsmanagementclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.AircraftTypeSessionBeanRemote;
import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.CabinConfigurationSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import javax.ejb.EJB;
import util.exception.CancelledException;
import util.exception.LogoutException;

public class Main {

    @EJB
    private static FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;

    @EJB 
    private static AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;

    @EJB 
    private static AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote;

    @EJB 
    private static PartnerSessionBeanRemote partnerSessionBeanRemote;

    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;
    
    @EJB
    private static AirportSessionBeanRemote airportSessionBeanRemote;
    
    @EJB
    private static FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    
    @EJB
    private static CabinConfigurationSessionBeanRemote cabinConfigurationSessionBeanRemote;
    
    @EJB
    private static FlightSessionBeanRemote flightSessionBeanRemote;


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        new FrsManagement(sc, employeeSessionBeanRemote, aircraftTypeSessionBeanRemote, aircraftConfigurationSessionBeanRemote,airportSessionBeanRemote,flightSessionBeanRemote,
                flightRouteSessionBeanRemote,cabinConfigurationSessionBeanRemote, flightSchedulePlanSessionBeanRemote).run();

    }
}