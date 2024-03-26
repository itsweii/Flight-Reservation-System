package frsmanagementclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.AircraftTypeSessionBeanRemote;
import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.CabinConfigurationSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.Menu;
import util.enumeration.EmployeeType;
import util.exception.CancelledException;
import util.exception.InvalidLoginCredentialsException;
import util.exception.LogoutException;
import util.exception.UnknownPersistenceException;

public class FrsManagement {
    private Scanner sc;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    private AirportSessionBeanRemote airportSessionBeanRemote;
    private FlightSessionBeanRemote flightSessionBeanRemote;
    private FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    private CabinConfigurationSessionBeanRemote cabinConfigurationSessionBeanRemote;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
    
    public FrsManagement(Scanner sc,
            EmployeeSessionBeanRemote employeeSessionBeanRemote, 
            AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote,
            AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote,
            AirportSessionBeanRemote airportSessionBeanRemote,
            FlightSessionBeanRemote flightSessionBeanRemote,
            FlightRouteSessionBeanRemote flightRouteSessionBeanRemote,
            CabinConfigurationSessionBeanRemote cabinConfigurationSessionBeanRemote,
            FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote) {
        
        this.sc = sc;    
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.aircraftTypeSessionBeanRemote = aircraftTypeSessionBeanRemote;
        this.aircraftConfigurationSessionBeanRemote = aircraftConfigurationSessionBeanRemote;
        this.airportSessionBeanRemote = airportSessionBeanRemote;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
        this.cabinConfigurationSessionBeanRemote = cabinConfigurationSessionBeanRemote;
        this.flightSchedulePlanSessionBeanRemote = flightSchedulePlanSessionBeanRemote;
        
    }

    public void run() {
        Employee employee = null;
        
        while (true) {
            if (employee == null) {
                //login if employee is null
                try {
                    employee = login();
                } catch (CancelledException e) {
                    Menu.clear();
                    System.out.println(e.getMessage());
                    System.out.println("");
                    break;
                } catch(Exception e) {
                    Menu.clear();
                    System.out.println(e.toString());
                    System.out.println("");
                }
            } else {
                //login successfully
                Menu.clear();
                System.out.println(String.format("Welcome %s",employee.getName()));
                System.out.println("");
                
                EmployeeType type = employee.getEmployeeType();
                try {
                    if (type == EmployeeType.FleetManager) {
                        new FlightPlanningModule(sc, employee, aircraftTypeSessionBeanRemote, aircraftConfigurationSessionBeanRemote,
                        airportSessionBeanRemote,flightRouteSessionBeanRemote,cabinConfigurationSessionBeanRemote).fleetManger();
                    } else if (type == EmployeeType.RoutePlanner) {
                        new FlightPlanningModule(sc, employee, aircraftTypeSessionBeanRemote, aircraftConfigurationSessionBeanRemote,
                        airportSessionBeanRemote,flightRouteSessionBeanRemote,cabinConfigurationSessionBeanRemote).routePlanner();
                    } else if (type == EmployeeType.ScheduleManger) {
                        new FlightOperationModule(sc, employee,flightSessionBeanRemote,
                                flightRouteSessionBeanRemote,airportSessionBeanRemote, aircraftConfigurationSessionBeanRemote, cabinConfigurationSessionBeanRemote,flightSchedulePlanSessionBeanRemote).run();
                    } else {
                        new SaleManagementModule(sc, employee, flightSchedulePlanSessionBeanRemote, cabinConfigurationSessionBeanRemote).run();
                    }
                } catch (LogoutException e) {
                    Menu.clear();
                    employee = null;
                } catch (CancelledException e) {
                    break;
                }
            }
        }
    }
    
    private Employee login() throws InvalidLoginCredentialsException, UnknownPersistenceException, CancelledException {
        Menu.print("FRS Management Client Login", new String[] {"Login"});
        System.out.print(">");
        String input = sc.nextLine().trim();
        if (input.length() > 0) {
            input = input.substring(0,1);
            if (input.equals("1")) {
                System.out.println();
                System.out.print("Username: ");
                String username = sc.nextLine().trim();
                System.out.print("Password: ");
                String password = sc.nextLine().trim();

                if(username.length() > 0 && password.length() > 0) {
                    return employeeSessionBeanRemote.getEmployee(username, password);
                }
                else {
                    Menu.clear();
                    System.out.println("Missing login credential!\n");
                }
            } else {
                throw new CancelledException("login");
            }
        }
        return null;
    }
}