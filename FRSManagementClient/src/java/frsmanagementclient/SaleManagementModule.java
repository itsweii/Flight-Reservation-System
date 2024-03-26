package frsmanagementclient;

import ejb.session.stateless.CabinConfigurationSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import entity.Employee;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.Scanner;
import util.Menu;
import util.exception.LogoutException;

public class SaleManagementModule {
    private Scanner sc;
    private Employee employee;
    private CabinConfigurationSessionBeanRemote cabinConfigurationSessionBeanRemote;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
            
    public SaleManagementModule(Scanner sc, Employee employee, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote, CabinConfigurationSessionBeanRemote cabinConfigurationSessionBeanRemote) {
        this.sc = sc;
        this.employee = employee;
        this.flightSchedulePlanSessionBeanRemote = flightSchedulePlanSessionBeanRemote;
        this.cabinConfigurationSessionBeanRemote = cabinConfigurationSessionBeanRemote;
    }
    
    public void run() throws LogoutException {
        while (true) {            
            //print menu
            Menu.print(String.format("FRS Management %s Client", employee.getEmployeeType()), new String[] 
                {"View Seats Inventory", "View all Flight Reservations", "logout"});
            System.out.print("> ");
            String input = sc.nextLine().trim();
            
            if(input.equals("1")) {
                viewSeatInventory();
            } else if (input.equals("2")) {
                viewFlightReservation();
            } else if (input.equals("3")) {
                throw new LogoutException();
            }
        }
    }
    
    private void viewSeatInventory() {
        System.out.println("View Seats Inventory");
        System.out.println("enter flight number:");
        String input = sc.nextLine().trim();
        try {
            FlightSchedulePlan fsp = flightSchedulePlanSessionBeanRemote.getFlightSchedulePlan(input);
            System.out.println(flightSchedulePlanSessionBeanRemote.flightScheduleDetail(fsp.getFlightSchedulePlanId()));
        
        System.out.println("enter flight schedule id:");
        Long id = Long.parseLong(sc.nextLine().trim());
        try {
            FlightSchedule f =flightSchedulePlanSessionBeanRemote.getFlightSchedule(id);
            System.out.println(flightSchedulePlanSessionBeanRemote.flightScheduleprint(id));
        } catch (NoSuchFieldException e) {
            System.out.println("Invalid flight schedule Id");
        }
        } catch (NoSuchFieldException e) {
            System.out.println("Invalid flightNumber");
        }
    }
    
    private void viewFlightReservation() {
        System.out.println("View Flight Reservation");
        System.out.println("enter flight number:");
        String input = sc.nextLine().trim();
        try {
            FlightSchedulePlan fsp = flightSchedulePlanSessionBeanRemote.getFlightSchedulePlan(input);
            System.out.println(flightSchedulePlanSessionBeanRemote.flightScheduleDetail(fsp.getFlightSchedulePlanId()));
        
        System.out.println("enter flight schedule id:");
        Long id = Long.parseLong(sc.nextLine().trim());
        try {
            FlightSchedule f =flightSchedulePlanSessionBeanRemote.getFlightSchedule(id);
            System.out.println(flightSchedulePlanSessionBeanRemote.flightScheduleprint(id));
        } catch (NoSuchFieldException e) {
            System.out.println("Invalid flight schedule Id");
        }
        } catch (NoSuchFieldException e) {
            System.out.println("Invalid flightNumber");
        }
    }
}
