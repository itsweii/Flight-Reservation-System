/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsreservationclient;

import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.ReservedSeatSessionBeanRemote;
import entity.Airport;
import entity.CabinConfiguration;
import entity.Customer;
import entity.FlightSchedule;
import entity.CabinFee;
import entity.ReservedSeat;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.Menu;
import util.exception.CancelledException;
import util.exception.CustomerAccountFoundException;
import util.exception.InvalidLoginCredentialsException;
import util.exception.UnknownPersistenceException;
import java.time.format.DateTimeFormatter;
import util.exception.NoAirportException;



public class FrsReservation {
    
    private Scanner sc;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
    private ReservedSeatSessionBeanRemote reservedSeatSessionBeanRemote;
    private AirportSessionBeanRemote airportSessionBeanRemote;

    
    public FrsReservation(Scanner sc, CustomerSessionBeanRemote customerSessionBeanRemote,
            FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote,
            FlightSchedulePlanSessionBeanRemote FlightSchedulePlanSessionBeanRemote,
            ReservedSeatSessionBeanRemote reservedSeatSessionBeanRemote,
            AirportSessionBeanRemote airportSessionBeanRemote) {
        this.sc = sc;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;
        this.flightSchedulePlanSessionBeanRemote = flightSchedulePlanSessionBeanRemote;
        this.reservedSeatSessionBeanRemote = reservedSeatSessionBeanRemote;
        this.airportSessionBeanRemote = airportSessionBeanRemote;
    }
    
    public void run() {
        Scanner sc = new Scanner(System.in);
        Customer customer = null;

        while (true) {
            if (customer == null) {
                System.out.print("\n");
                System.out.println("1: Login");
                System.out.println("2: Create Account");
                System.out.println("3: Search Flight\n");
                System.out.print(">");
                String input = sc.nextLine().trim();
                
                
                 if (input.length() > 0) {
                    input = input.substring(0,1);
                    if (input.equals("1")) {
                        try {
                            customer = login();
                        } catch(UnknownPersistenceException ex) {
                            System.out.println("Invalid Login");
                        } catch(CancelledException ex) {
                            ex.getMessage();
                        }
                    } else if(input.equals("2")){
                        createAccount();
                    } else if(input.equals("3")){
                        searchFlight(customer);//prompt user to log in if customer is null
                    }else {
                        System.out.println("Invalid Choice. Please enter again!");
                    }
                }
                 
                } else {
                    //login successfully
                    while(true) {
                        Menu.print("FRS Reservation", new String[] 
                            {"Search Flight", 
                            "View My Flight Reservations", 
                            "View My Flight Reservations Deatils",
                            "Logout"});
                        System.out.print("> ");
                        String input = sc.nextLine().trim();

                        if (input.length() > 0) {
                             input = input.substring(0,1);

                             if (input.equals("1")) { //Create Aircraft Configuration
                                Menu.clear();
                                searchFlight(customer);
                                System.out.println("Reserve Flight? Y/N");
                                String result = sc.nextLine().trim();
                                if (result.toUpperCase().equals("Y")) {
                                    reserveFlight(customer);
                                }
                            } else if (input.equals("2")) {
                                Menu.clear();
                                viewFlightReservations(customer);

                            } else if (input.equals("3")) {
                                Menu.clear();
                                System.out.println("Reservation Id");
                                viewReservationDetail(sc.nextLong());
                            } else if (input.equals("4")) {
                                Menu.clear();
                                customer = null;
                                break;
                                
                            }
                    
                        } else {
                           System.out.println("Invalid Input!");
                        }
                    }
                }
            }
        }
            
        
        
    
    
    private Customer login() throws UnknownPersistenceException, CancelledException {
        
            System.out.print("Username: ");
            String username = sc.next().trim();
            System.out.print("Password: ");
            String password = sc.next().trim();
            if(username.length() > 0 && password.length() > 0) {
                try {
                    return customerSessionBeanRemote.doLogin(username, password);
                } catch (InvalidLoginCredentialsException ex) {
                    System.out.println("Invalid Login Credential!");
                } catch(UnknownPersistenceException ex) {
                    System.out.println("Invalid Login!");
                }
            }
            else {
                System.out.println("Missing login credential!");
                return null;
            }
            return null;
    }
    
    
  
    
    private void createAccount() {
        
        Customer cus = new Customer();
        System.out.print("Enter firstName > ");
        cus.setFirstName(sc.next().trim());
        System.out.print( "Enter lastName > ");
        cus.setLastName(sc.next().trim());
        System.out.print( "Enter email > ");
        cus.setEmail(sc.next().trim());
        System.out.print("Enter address > ");
        cus.setAddress(sc.next().trim());
       
        while(true) {
            
            System.out.print("Enter username > ");
            String userName = sc.next().trim();
        
            try{
                Customer existingCustomer = customerSessionBeanRemote.findUsername(userName);
                cus.setUserName(userName);
                break;
            } catch(CustomerAccountFoundException ex) {
                System.out.println("Username has been registered!");
                System.out.print("Try Again!");
            }
        }
            
        System.out.print("Enter password > ");
        cus.setPassword(sc.next().trim());
        System.out.println("\nCustomer Account Created! Account Name: " + 
            customerSessionBeanRemote.createCustomerAccount(cus).getUserName());
        
    } 
    
    
    private void searchFlight(Customer customer) {
        
        List<FlightSchedule> onDate = new ArrayList<> ();
        List<FlightSchedule> beforeDate = new ArrayList<> ();
        List<FlightSchedule> postDate = new ArrayList<> ();
        
        System.out.println("Departure Date> (e.g. 1/12/23)");
        String startDateInput = sc.next();
        String[] start = startDateInput.split("/");
        int day = Integer.parseInt(start[0]);
        int month = Integer.parseInt(start[1]);
        int year = Integer.parseInt(start[2]) + 2000;
        LocalDateTime startofDay = LocalDateTime.of(year, Month.of(month), day, 0, 0);
        LocalDateTime endofDay = LocalDateTime.of(year, Month.of(month), day,23, 59);
        LocalDateTime startofDayR = null;
        LocalDateTime endofDayR = null;
        
        System.out.println("Departure Airport IATA >");
        String origin = sc.next().trim().toUpperCase();
        System.out.println("Destination Airport IATA >");
        String destination = sc.next().trim().toUpperCase();
        
        onDate = flightScheduleSessionBeanRemote.searchFlightSchedules(origin,destination, startofDay,endofDay);
        beforeDate = flightScheduleSessionBeanRemote.searchFlightSchedules(origin,destination,startofDay.minusDays(3),startofDay.minusDays(1));
        postDate = flightScheduleSessionBeanRemote.searchFlightSchedules(origin,destination,endofDay.plusDays(1),endofDay.plusDays(3));
      
        System.out.println("Trip type > \n1.single trip \n2.round trip ");
        int tripType = sc.nextInt(); 
        
        if (tripType == 2){
         System.out.println("Return Date> (e.g. 1/12/23)");
                startDateInput = sc.next();
                start = startDateInput.split("/");
                day = Integer.parseInt(start[0]);
                month = Integer.parseInt(start[1]);
                year = Integer.parseInt(start[2]) + 2000;
                startofDayR = LocalDateTime.of(year, Month.of(month), day, 0, 0);
                endofDayR = LocalDateTime.of(year, Month.of(month), day,23, 59);
        }
        
        System.out.println("Flight type > \n1.direct trip \n2.connecting trip \n3.No preference");
        int flightType = sc.nextInt(); 
        
        String cabinPreference = "N";
        System.out.print("Cabin Preference : Y/N\n>");
        cabinPreference = sc.next().toUpperCase().trim();
        if (cabinPreference.equals("Y")) {
                System.out.print("Cabin Preference : F, J, W, Y\n>");
                cabinPreference = sc.next().toUpperCase().trim();
        } 
        
        System.out.println("Passenger Number");
        int passengerNum = sc.nextInt();
        
       
        if(flightType != 2 ) {
            //coming direct trip
            if(!onDate.isEmpty()) {
                System.out.println("Coming Direct Flight on departure date");
                System.out.println(flightScheduleSessionBeanRemote.printFlightSchedule(onDate, passengerNum,cabinPreference));
             } 
            if(!beforeDate.isEmpty()) {
                System.out.println("Coming Direct Flight 3 days before departure date");
                System.out.println(flightScheduleSessionBeanRemote.printFlightSchedule(beforeDate, passengerNum,cabinPreference));
              }
            if(!postDate.isEmpty()) {
                System.out.println("Coming Direct Flight 3 days after departure date");
                System.out.println(flightScheduleSessionBeanRemote.printFlightSchedule(postDate, passengerNum,cabinPreference));
            }
        }
            
            
             //return direct trip 
             if(tripType != 1 || (flightType != 2 && tripType != 1)) {
                 
                onDate = flightScheduleSessionBeanRemote.searchFlightSchedules(destination,origin, startofDayR,endofDayR);
                beforeDate = flightScheduleSessionBeanRemote.searchFlightSchedules(destination,origin,startofDayR.minusDays(3),startofDayR.minusDays(1));
                postDate = flightScheduleSessionBeanRemote.searchFlightSchedules(destination,origin,endofDayR.plusDays(1),endofDayR.plusDays(3));
                if(!onDate.isEmpty()) {
                    System.out.println("Return Flight on departure date");
                    System.out.println(flightScheduleSessionBeanRemote.printFlightSchedule(onDate, passengerNum,cabinPreference));
                }
                if(!beforeDate.isEmpty()) {
                    System.out.println("Return Flight 3 days before departure date");
                    System.out.println(flightScheduleSessionBeanRemote.printFlightSchedule(beforeDate, passengerNum,cabinPreference));
                }
                if(!postDate.isEmpty()) {
                    System.out.println("Return Flight 3 days after departure date");
                    System.out.println(flightScheduleSessionBeanRemote.printFlightSchedule(postDate, passengerNum,cabinPreference));
                }
            }
        List<FlightSchedule> originFlights = new ArrayList<> ();
        List<FlightSchedule> destinationFlights = new ArrayList<> ();
        List<FlightSchedule> result = new ArrayList<> ();

        //coming connecting flight
        if(flightType == 2 || flightType == 3) { 
                originFlights = flightScheduleSessionBeanRemote.searchConnectingFscOrigin(origin,startofDay.minusDays(3),endofDay.plusDays(3));
                //System.out.print(originFlights);
                destinationFlights = flightScheduleSessionBeanRemote.searchConnectingFscDestination(destination,startofDay.minusDays(3),endofDay.plusDays(3));
                //System.out.print(destinationFlights);
                result = flightScheduleSessionBeanRemote.getConnectingFlights(originFlights,destinationFlights);
                //System.out.print(result);
                    if(!result.isEmpty()) {
                        System.out.println("Coming Connecting Flight");
                        System.out.println(flightScheduleSessionBeanRemote.printConnectingFlights(result));
                }
            
        
        //return connecting flight
            if(tripType == 2 ){
                
            originFlights = flightScheduleSessionBeanRemote.searchConnectingFscOrigin(destination,startofDayR.minusDays(3),endofDayR.plusDays(3));
            destinationFlights = flightScheduleSessionBeanRemote.searchConnectingFscDestination(origin,startofDayR.minusDays(3),endofDayR.plusDays(3));
            result = flightScheduleSessionBeanRemote.getConnectingFlights(originFlights,destinationFlights);

            if(!result.isEmpty()) {
            System.out.println("\nReturn Connecting Flight");
            System.out.println(flightScheduleSessionBeanRemote.printConnectingFlights(result));
            }
            }
        }

    }
    
        
        
    private void reserveFlight(Customer customer) {
        int book = 0;
        
        while (book != 2) {
            
            System.out.println("Enter Flight Schedule ID");
            Long id = sc.nextLong();
            System.out.println("Enter the Cabin Class");
            String cc = sc.next().toUpperCase();
            FlightSchedule fsc = flightScheduleSessionBeanRemote.findFlightSchedule(id);
            System.out.println("Number  of Seat Available for the cabin: "
                    + flightScheduleSessionBeanRemote.checkSeatAvailbility(id,cc));
             //System.out.println(flightScheduleSessionBeanRemote.getTicketPrice(id, cc));
            if(flightScheduleSessionBeanRemote.checkSeatAvailbility(id,cc) > 0) {
            //display cabin config
                System.out.println(flightScheduleSessionBeanRemote.findSeatConfiguration(fsc, cc));
                System.out.println("***Select Seat***\n");
                String seatNum = "";
                System.out.println("Select row");
                seatNum += sc.next().trim();  // Using +=
                System.out.println("Select Seat Designation, e.g., A, B, C");
                seatNum += sc.next().toUpperCase();  // Using +=
                //System.out.println(seatNum);

                System.out.println("Enter Passenger First Name");
                String fName = sc.next();
                System.out.println("Enter Passenger Last Name");
                String lName = sc.next();
                System.out.println("Enter Passport Number");
                String passportNumber = sc.next();

                System.out.println("Enter Credit Card Number");
                String ccInfo = sc.next();
                System.out.println("Enter CVV");
                ccInfo = ccInfo + sc.next();
                System.out.println("Enter Name on Card");
                ccInfo = ccInfo + sc.next();
            
                /*ReservedSeat seat = new ReservedSeat(seatNum,cc,fName.concat(lName),passportNumber,ccInfo,
                        flightScheduleSessionBeanRemote.getTicketPrice(id, cc));*/
                ReservedSeat seat = new ReservedSeat(seatNum,cc,fName.concat(lName),passportNumber,ccInfo,
                        400);
                    System.out.println("Seat Booked! SeatId: " + reservedSeatSessionBeanRemote
                        .createReservedSeat(seat,customer,fsc));
                } else {
                        System.out.println("Insufficient seat available");
                }
            
                System.out.println("Continue? 1.Yes/ 2.No");
                book = sc.nextInt();
            
            }
        }
    
    
    private void viewFlightReservations(Customer customer) {
        System.out.println(reservedSeatSessionBeanRemote.getAllReservedSeat(customer) + "\n");
        
    }
    
    private void viewReservationDetail(Long id) {
       
         System.out.println(reservedSeatSessionBeanRemote.viewReservationDetail(id));
    }
}
        
            
            
            
            
         
            
     
  
    
    
     
   
   