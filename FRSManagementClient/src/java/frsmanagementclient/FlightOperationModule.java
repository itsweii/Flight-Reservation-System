 package frsmanagementclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.CabinConfigurationSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.Airport;
import entity.CabinConfiguration;
import entity.Employee;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.Menu;
import util.exception.CancelledException;
import util.exception.FlightExistException;
import util.exception.FlightRouteExistException;
import util.exception.InvalidInputException;
import util.exception.LogoutException;
import util.exception.NoAircraftConfigurationException;
import util.exception.NoAirportException;
import util.exception.NoFlightFoundException;
import util.exception.NoFlightRouteException;
import util.exception.RecordBeingUsedException;
import util.exception.UnknownPersistenceException;

public class FlightOperationModule {
    private Scanner sc;
    private Employee employee;
    private FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    private FlightSessionBeanRemote flightSessionBeanRemote;
    private AirportSessionBeanRemote airportSessionBeanRemote;
    private CabinConfigurationSessionBeanRemote cabinConfigurationSessionBeanRemote;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
    
    public FlightOperationModule(Scanner sc, Employee employee, 
            FlightSessionBeanRemote flightSessionBeanRemote,
            FlightRouteSessionBeanRemote flightRouteSessionBeanRemote,
            AirportSessionBeanRemote airportSessionBeanRemote, 
            AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote,
            CabinConfigurationSessionBeanRemote cabinConfigurationSessionBeanRemote,
            FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote ) {
        
        
        this.sc = sc;
        this.employee = employee;
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.airportSessionBeanRemote = airportSessionBeanRemote;
        this.aircraftConfigurationSessionBeanRemote = aircraftConfigurationSessionBeanRemote;
        this.cabinConfigurationSessionBeanRemote = cabinConfigurationSessionBeanRemote;
        this.flightSchedulePlanSessionBeanRemote = flightSchedulePlanSessionBeanRemote;
    }

    
    public void run() throws LogoutException, CancelledException {
        
        while (true) {            
            //print menu
            Menu.print(String.format("FRS Management %s Client", employee.getEmployeeType()), new String[] 
                {"Create Flight", "View all Flights", "View Flight Details", "Update Flight Details", "Delete Flight", "Create Flight Schedule Plan",
                "View All Flight Schedule Plans", "View Flight Schedule Plan Details", "Update Flight Schedule Plan", "Delete Flight Schedule Plan", "logout"});
            System.out.print("> ");
            String input = sc.nextLine().trim();
            
            if (input.length() > 0) {
                
                if (input.equals("1")) {
                    createFlight();
                } else if (input.equals("2")) {
                  viewAllFlight();
                } else if (input.equals("3")) {
                    viewFlightDetail();
                } else if(input.equals("4")) {
                    updateFlightDetail();
                } else if(input.equals("5")) {
                    deleteFlightRecord();
                } else if(input.equals("6")) {
                    createFlightSchedule();
                } else if (input.equals("7")) {
                    viewAllFlightSchedule();
                } else if (input.equals("8")) {
                    viewFlightSchedule();
                } else if (input.equals("9")) {
                    updateFlightSchedulePlan();
                } else if (input.equals("10")) {
                    deleteFlightSchedule();
                } else if (input.equals("11")) {
                    throw new LogoutException();
                }
            }   
                        
             else {
                    throw new CancelledException("FRS Management ScheduleManger Client");
            }
            
        }
    }
    
    
    private void createFlight() {
        System.out.println("Create Flight:\n");

        try {
            System.out.println("Enter Flight Number: ");
            String Num = sc.nextLine().trim();
            String flightNum = "ML" + Num; 

            if (Num.length() == 0) {
                throw new InvalidInputException("Invalid Flight Number"); 
            } 
            
            

            System.out.println("\nEnter Flight Origin IATA:");
            String Origin = sc.nextLine().trim();

            System.out.println("\nEnter Flight Destination IATA:");
            String Destination = sc.nextLine().trim();
            
            System.out.println("\nEnter Aircraft Configuration Name:");
            String name = sc.nextLine().trim();
            
            System.out.println("\nCreate return flight? yes/no");
            String createReturn = sc.nextLine().trim();
            
            AircraftConfiguration config = aircraftConfigurationSessionBeanRemote.getAircraftConfiguration(name);
            
            Airport originAirport = airportSessionBeanRemote.findAirportByIATA(Origin);
            Airport destinationAirport = airportSessionBeanRemote.findAirportByIATA(Destination);
            
            FlightRoute flightRoute = flightRouteSessionBeanRemote.findFlightRoute(originAirport, destinationAirport);
            if (flightRoute.isIsDisabled()) {
                throw new NoSuchMethodException();
            }
            Long temp = flightSessionBeanRemote.craeteFlight(flightNum, flightRoute.getFlightRouteId(), config.getAircraftConfigurationId());
            System.out.println("Flight created! Flight ID > " + temp);   
            
            if (createReturn.equals("yes")) {
                FlightRoute returnRoute = new FlightRoute(destinationAirport,originAirport);
                try {
                    Long end = flightRouteSessionBeanRemote.createFlightRoute(returnRoute);
                    flightRouteSessionBeanRemote.addReturnRoute(flightRoute.getFlightRouteId(), end);
                } catch (FlightRouteExistException e) {
                    returnRoute = flightRouteSessionBeanRemote.findFlightRoute(originAirport, destinationAirport);
                    flightRouteSessionBeanRemote.addReturnRoute(flightRoute.getFlightRouteId(), returnRoute.getFlightRouteId());
                }
                returnRoute = flightRouteSessionBeanRemote.findFlightRoute(destinationAirport, originAirport);
                System.out.println("\nEnter flight number");
                flightNum = "ML" + sc.nextLine().trim();
                try {
                  Long returnId = flightSessionBeanRemote.craeteReturnFlight(flightNum, returnRoute.getFlightRouteId(), config.getAircraftConfigurationId(), temp);
                  System.out.println("Return flight Created! Flight ID >" + returnId);
                } catch (FlightExistException e) {
                    System.out.println(e.getMessage());
                }
            }

        } catch (NoFlightRouteException e) {
            System.out.println("no flight route was found");
        } catch (NoSuchMethodException e) {
            System.out.println("flight route disabled");
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
            System.out.println("");
        } catch (NoAirportException e) {
            System.out.println(e.getMessage());
        } catch (NoAircraftConfigurationException e){
            System.out.println(e.getMessage());
        } catch (FlightExistException e) {
            System.out.println(e.getMessage());
        } catch(UnknownPersistenceException e) {
            System.out.println("invalid input");
        }
    }
    
    private void viewAllFlight() {
        System.out.println("\nview all flight:");
        List<Flight> flightList = flightSessionBeanRemote.retrieveAllFlight();
        for(Flight f : flightList) {
            System.out.println(f.toString());
        }
        System.out.println("");
    }
    
    private void viewFlightDetail() {
        System.out.println("Flight Number:");
        String flightNum = sc.nextLine().trim();
        try {
            System.out.println(flightSessionBeanRemote.printFlightDetails(flightNum));
        } catch (NoFlightFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void updateFlightDetail() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Update Flight Detail\n");
        System.out.println("Enter Flight Number\n");
        String flightNum = sc.nextLine();
        
        try {
            Flight flight = flightSessionBeanRemote.retrieveFlight(flightNum);
            try {
                FlightSchedulePlan fsp = flightSchedulePlanSessionBeanRemote.getFlightSchedulePlan(flightNum);
                throw new RecordBeingUsedException();
            } catch (NoSuchFieldException e) {
                
            }
        while (true) {            
            System.out.println(flightSessionBeanRemote.printFlightDetails(flight.getFlightNum()) + "\n");
            System.out.println("1. Update Flight Number");
            System.out.println("2. Update Flight Route");
            System.out.println("3. Update AirCraft Configuration ");
            System.out.println("4. exit \n");
            String input = sc.nextLine().trim();
                
                if (input.equals("1")) {
                    System.out.println("Enter New Flight Number\n");
                    String newNum = sc.nextLine();
                    newNum = "ML" + newNum;
                    flight.setFlightNum(newNum);
                }
                if (input.equals("2")) {
                    
                    System.out.println("Enter New Flight Origin IATA:\n ");
                    String Origin = sc.nextLine().trim();
                    System.out.println("Enter New Flight Destination IATA:\n ");
                    String Destination = sc.nextLine().trim();
                    Airport originAirport = airportSessionBeanRemote.findAirportByIATA(Origin);
                    Airport destinationAirport = airportSessionBeanRemote.findAirportByIATA(Destination);
                    FlightRoute flightRoute = flightRouteSessionBeanRemote.findFlightRoute(originAirport, destinationAirport);
                    if (flight.getReturnFlight() != null) {
                        try {
                            FlightRoute returnRoute = flightRouteSessionBeanRemote.findFlightRoute(destinationAirport, originAirport);
                            flight.getReturnFlight().setFlightRoute(returnRoute);
                            flightSessionBeanRemote.updateFlightDetail(flight.getReturnFlight());
                        } catch (NoFlightRouteException e) {
                            throw new NoSuchMethodException();
                        }
                    }
                    
                    flight.setFlightRoute(flightRoute);
                }
                if (input.equals("3")) {
                    for (AircraftConfiguration ac : aircraftConfigurationSessionBeanRemote.getAllAircraftConfiguration()) {
                        System.out.println(ac);
                    }
                    System.out.println("Enter AirCraft Configuration name\n ");
                    String name = sc.nextLine();
                    flight.setAircraftConfiguration(aircraftConfigurationSessionBeanRemote.getAircraftConfiguration(name));
                }
                flightSessionBeanRemote.updateFlightDetail(flight);
                
                if(input.equals("4")) {
                    break;
                }
            }
        
        } catch (RecordBeingUsedException e) {
            System.out.println("Flight is being used, unable to update");
        } catch (NoFlightFoundException ex) {
            System.out.println("no flight was found");
        } catch (NoAirportException ex) {
            System.out.println("invalid airport IATA");
        } catch (NoFlightRouteException ex){
            System.out.println("no flight route with the entered combination was found");
        } catch (UnknownPersistenceException ex) {
            ex.getMessage(); 
        } catch (NoAircraftConfigurationException ex) {
            System.out.println("invalid aircraft Configuration");
        } catch (NoSuchMethodException e) {
            System.out.println("there is no return flight route");
        }
        
    }
    
    private void deleteFlightRecord() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Delete Flight Detail\n");
        System.out.println("Enter Flight Number\n");
        String flightNum = sc.nextLine();
        
        try {
            flightSessionBeanRemote.deleteFlightRoute(flightNum);
            System.out.println("flight deleted / disabled! \n");
        } catch (NoFlightFoundException ex) {
            ex.getMessage();
        }
    }
    
    private void createFlightSchedule() {
        try {
            System.out.println("Enter flight number:");
            String flightNum = sc.nextLine().trim();
            try {
                FlightSchedulePlan check = flightSchedulePlanSessionBeanRemote.getFlightSchedulePlan(flightNum);
                throw new NoSuchMethodException();
            } catch (NoSuchFieldException e) {
            }
            Flight flight = flightSessionBeanRemote.retrieveFlight(flightNum);
            Flight returnflight = flight.getReturnFlight();
             
            
            System.out.println("types of schedule:");
            System.out.println("1. Single schedule");
            System.out.println("2. Multiple schedules");
            System.out.println("3. Recurrent schedules every n days");
            System.out.println("4. Recurrent schedules every week");
            System.out.print("> ");
            String input = sc.nextLine().trim();
            
            
            if (input.equals("1")) {
                System.out.println("date (e.g. 1/12/23)");
                String[] start = sc.nextLine().split("/");
                int day = Integer.parseInt(start[0]);
                int month = Integer.parseInt(start[1]);
                int year = Integer.parseInt(start[2]) + 2000;
                LocalDateTime date = LocalDateTime.of(year, Month.of(month), day, 0, 0);
                //time
                System.out.println("enter time (e.g. 10:00 AM): ");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
                LocalTime time = LocalTime.parse(sc.nextLine(), timeFormatter);
                LocalDateTime dateTime = date.withHour(time.getHour()).withMinute(time.getMinute());
                //Duration
                System.out.println("duration (e.g. 6 30 for 6 hours 30 Minute)");
                String[] dur = sc.nextLine().split(" ");
                int hours = Integer.parseInt(dur[0]);
                int minutes = Integer.parseInt(dur[1]);
                Duration duration = Duration.ofHours(hours).plusMinutes(minutes);
                
                String layoverInput = "";
                if (returnflight != null) {
                    System.out.println("Layover (e.g. 3 for 3 Hours, -1 for no return flight):");
                    layoverInput = sc.nextLine().trim();
                }
                
                System.out.println("");
                List<CabinConfiguration> list = cabinConfigurationSessionBeanRemote.getCabinConfigurations(flight.getFlightId());
                List<Long> cabinFees = new ArrayList<Long>();
                int cout = list.size();
                while(cout != 0) {
                    System.out.println("enter fee (e.g. F F002 4000 for classname, feename, fee): ");
                    String cabininput = sc.nextLine().trim();
                    String[] cabinarr = cabininput.split(" ");
                    
                    CabinConfiguration curr = null;
                    for (CabinConfiguration cc : list) {
                        String temp = cabinarr[0].toUpperCase();
                        if (cc.getCabinclass().equals(temp)) {
                            curr = cc;
                            break;
                        }
                    }
                    
                    if (curr == null) {
                        System.out.println("invalid cabin configuration");
                    } else {
                        String name = cabinarr[1];
                        String feeString = cabinarr[2];
                        int fee = Integer.parseInt(feeString);
                        Long lg = flightSchedulePlanSessionBeanRemote.createCabinFee(curr.getCabinConfigurationId(), name, fee);
                        cout--;
                        cabinFees.add(lg);
                    }
                }
                Long returnFlightId = (returnflight == null) ? 0 : returnflight.getFlightId();
                
                Long id = flightSchedulePlanSessionBeanRemote.createSingleFlightSchedulePlan(
                    flight.getFlightId(), 
                    returnFlightId, 
                    dateTime, 
                    duration,
                    layoverInput,
                    cabinFees);
                
                System.out.println("flight plan creeated (id:" + id + ")");
            } else if (input.equals("2")) {
                //Multiple schedules
                System.out.println("number of plan: ");
                int loop = Integer.parseInt(sc.nextLine().trim());
                
                List<LocalDateTime> times = new ArrayList<>();
                List<Duration> durations = new ArrayList<>();
                for (int i = 0; i < loop; i++) {
                    System.out.println("plan " + (i + 1));
                    //date
                    System.out.println("date (e.g. 1/12/23)");
                    String[] start = sc.nextLine().split("/");
                    int day = Integer.parseInt(start[0]);
                    int month = Integer.parseInt(start[1]);
                    int year = Integer.parseInt(start[2]) + 2000;
                    LocalDateTime date = LocalDateTime.of(year, Month.of(month), day, 0, 0);
                    //time
                    System.out.println("enter time (e.g. 10:00 AM): ");
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
                    LocalTime time = LocalTime.parse(sc.nextLine(), timeFormatter);
                    LocalDateTime dateTime = date.withHour(time.getHour()).withMinute(time.getMinute());
                    //Duration
                    System.out.println("duration (e.g. 6 30 for 6 hours 30 Minute)");
                    String[] dur = sc.nextLine().split(" ");
                    int hours = Integer.parseInt(dur[0]);
                    int minutes = Integer.parseInt(dur[1]);
                    Duration duration = Duration.ofHours(hours).plusMinutes(minutes);
                    times.add(dateTime);
                    durations.add(duration);
                }
                
                String layoverInput = "";
                if (returnflight != null) {
                    System.out.println("Layover (e.g. 3 for 3 Hours, -1 for no return flight):");
                    layoverInput = sc.nextLine().trim();
                }
                
                System.out.println("");
                List<CabinConfiguration> list = cabinConfigurationSessionBeanRemote.getCabinConfigurations(flight.getFlightId());
                for (CabinConfiguration cc : list) {
                    System.out.println(cc);
                }
                List<Long> cabinFees = new ArrayList<Long>();
                int cout = list.size();
                while(cout != 0) {
                    System.out.println("enter fee (e.g. F F002 4000 for classname, feename, fee) `done` to end: ");
                    String cabininput = sc.nextLine().trim();
                    String[] cabinarr = cabininput.split(" ");
                    
                    CabinConfiguration curr = null;
                    for (CabinConfiguration cc : list) {
                        String temp = cabinarr[0].toUpperCase();
                        if (cc.getCabinclass().equals(temp)) {
                            curr = cc;
                            break;
                        }
                    }
                    if (curr == null) {
                        System.out.println("invalid cabin configuration");
                    } else {
                        String name = cabinarr[1];
                        String feeString = cabinarr[2];
                        int fee = Integer.parseInt(feeString);
                        Long lg = flightSchedulePlanSessionBeanRemote.createCabinFee(curr.getCabinConfigurationId(), name, fee);
                        cout--;
                        cabinFees.add(lg);
                    }
                }
                Long returnFlightId = (returnflight == null) ? 0 : returnflight.getFlightId();
                
        
                Long id = flightSchedulePlanSessionBeanRemote.createMultipleFlightSchedulePlan(
                flight.getFlightId(), 
                returnFlightId, 
                times, 
                durations,
                layoverInput,
                cabinFees);
                System.out.println("flight plan creeated (id:" + id + ")");
            } else if (input.equals("3")) {
                //n schedules
                System.out.println("enter n:");
                Long n = Long.parseLong(sc.nextLine().trim());

                //date
                System.out.println("start date (e.g. 1/12/23)");
                String[] start = sc.nextLine().split("/");
                int day = Integer.parseInt(start[0]);
                int month = Integer.parseInt(start[1]);
                int year = Integer.parseInt(start[2]) + 2000;
                LocalDateTime startdate = LocalDateTime.of(year, Month.of(month), day, 0, 0);

                System.out.println("end date (e.g. 1/12/23)");
                String[] end = sc.nextLine().split("/");
                day = Integer.parseInt(end[0]);
                month = Integer.parseInt(end[1]);
                year = Integer.parseInt(end[2]) + 2000;
                LocalDateTime enddate = LocalDateTime.of(year, Month.of(month), day, 0, 0);

                //time
                System.out.println("enter time (e.g. 10:00 AM): ");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
                LocalTime time = LocalTime.parse(sc.nextLine(), timeFormatter);
                LocalDateTime startTime = startdate.withHour(time.getHour()).withMinute(time.getMinute());
                //Duration
                System.out.println("duration (e.g. 6 30 for 6 hours 30 Minute)");
                String[] dur = sc.nextLine().split(" ");
                int hours = Integer.parseInt(dur[0]);
                int minutes = Integer.parseInt(dur[1]);
                Duration duration = Duration.ofHours(hours).plusMinutes(minutes);
                
                String layoverInput = "";
                if (returnflight != null) {
                    System.out.println("Layover (e.g. 3 for 3 Hours, -1 for no return flight):");
                    layoverInput = sc.nextLine().trim();
                }
                
                System.out.println("");
                List<CabinConfiguration> list = cabinConfigurationSessionBeanRemote.getCabinConfigurations(flight.getFlightId());
                for (CabinConfiguration cc : list) {
                    System.out.println(cc);
                }
                List<Long> cabinFees = new ArrayList<Long>();
                int cout = list.size();
                while(cout != 0) {
                    System.out.println("enter fee (e.g. F F002 4000 for classname, feename, fee) `done` to end: ");
                    String cabininput = sc.nextLine().trim();
                    String[] cabinarr = cabininput.split(" ");
                    
                    CabinConfiguration curr = null;
                    for (CabinConfiguration cc : list) {
                        String temp = cabinarr[0].toUpperCase();
                        if (cc.getCabinclass().equals(temp)) {
                            curr = cc;
                            break;
                        }
                    }
                    if (curr == null) {
                        System.out.println("invalid cabin configuration");
                    } else {
                        String name = cabinarr[1];
                        String feeString = cabinarr[2];
                        int fee = Integer.parseInt(feeString);
                        Long lg = flightSchedulePlanSessionBeanRemote.createCabinFee(curr.getCabinConfigurationId(), name, fee);
                        cout--;
                        cabinFees.add(lg);
                    }
                }
                Long returnFlightId = (returnflight == null) ? 0 : returnflight.getFlightId();
                
                Long id = flightSchedulePlanSessionBeanRemote.createNFlightSchedulePlan(
                flight.getFlightId(), 
                returnFlightId, 
                n,
                startTime,
                enddate,
                duration,
                layoverInput,
                cabinFees);
                System.out.println("flight plan creeated (id:" + id + ")");
            } else if (input.equals("4")) {
                System.out.println("enter the day of the week (e.g. Wednesday):");
                String dayOfWeekInput = sc.nextLine().trim().toLowerCase();
                DayOfWeek dayOfWeek = DayOfWeek.SUNDAY;
                switch (dayOfWeekInput) {
                    case "monday":
                        dayOfWeek = DayOfWeek.MONDAY;
                        break;
                    case "tuesday":
                        dayOfWeek = DayOfWeek.TUESDAY;
                        break;
                    case "wednesday":
                        dayOfWeek = DayOfWeek.WEDNESDAY;
                        break;
                    case "thursday":
                        dayOfWeek = DayOfWeek.THURSDAY;
                        break;
                    case "friday":
                        dayOfWeek = DayOfWeek.FRIDAY;
                        break;
                    case "saturday":
                        dayOfWeek = DayOfWeek.SATURDAY;
                        break;
                    default:
                        throw new InvalidInputException();
                }
                
                System.out.println("enter time (e.g. 10:00 AM): ");
                String timeInput = sc.nextLine().trim();
                
                System.out.println("start date (e.g. 1/12/23)");
                String startDateInput = sc.nextLine().trim();
                
                System.out.println("end date (e.g. 31/12/23)");
                String endDateInput = sc.nextLine().trim();
                
                System.out.println("duration (e.g. 6 30 for 6 hours 30 Minute)");
                String durationInput = sc.nextLine().trim();
                
                String layoverInput = "";
                if (returnflight != null) {
                    System.out.println("Layover (e.g. 3 for 3 Hours, -1 for no return flight):");
                    layoverInput = sc.nextLine().trim();
                }
                
                System.out.println("");
                List<CabinConfiguration> list = cabinConfigurationSessionBeanRemote.getCabinConfigurations(flight.getFlightId());
                for (CabinConfiguration cc : list) {
                    System.out.println(cc);
                }
                List<Long> cabinFees = new ArrayList<Long>();
                int cout = list.size();
                while(cout != 0) {
                    System.out.println("enter fee (e.g. F F002 4000 for classname, feename, fee) `done` to end: ");
                    String cabininput = sc.nextLine().trim();
                    String[] cabinarr = cabininput.split(" ");
                    
                    CabinConfiguration curr = null;
                    for (CabinConfiguration cc : list) {
                        String temp = cabinarr[0].toUpperCase();
                        if (cc.getCabinclass().equals(temp)) {
                            curr = cc;
                            break;
                        }
                    }
                    if (curr == null) {
                        System.out.println("invalid cabin configuration");
                    } else {
                        String name = cabinarr[1];
                        String feeString = cabinarr[2];
                        int fee = Integer.parseInt(feeString);
                        Long lg = flightSchedulePlanSessionBeanRemote.createCabinFee(curr.getCabinConfigurationId(), name, fee);
                        cout--;
                        cabinFees.add(lg);
                    }
                }
                Long returnFlightId = (returnflight == null) ? 0 : returnflight.getFlightId();
                
                Long id = flightSchedulePlanSessionBeanRemote.createWeeklyFlightSchedulePlan(
                    flight.getFlightId(), 
                    returnFlightId, 
                    dayOfWeek, 
                    timeInput, 
                    startDateInput, 
                    endDateInput, 
                    durationInput,
                    layoverInput,
                    cabinFees);
                
                System.out.println("flight plan creeated (id:" + id + ")");
            } else {
                throw new InvalidInputException();
            }
        } catch (NoFlightFoundException e) {
            System.out.println(e.getMessage());
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchMethodException e) {
            System.out.println("flight schedule plan for the flight already exist");
        } catch (Exception e) {
            System.out.println("Invaid input");
        }
    }
    
    private void viewAllFlightSchedule() {
        System.out.println("\nview all flight schedule plan:");
        List<String> list = flightSchedulePlanSessionBeanRemote.getAllFlightSchedulePlan();
        
        for (String f : list) {
            System.out.println(f);
        }
        System.out.println("");
    }
    
    private void viewFlightSchedule() {
        viewAllFlightSchedule();
        System.out.println("\nFlight Schedule Plan Id:");
        Long input = Long.parseLong(sc.nextLine().trim());
        try {
            System.out.println(flightSchedulePlanSessionBeanRemote.flightScheduleDetail(input));
        } catch (Exception e) {
            System.out.println("Invalid Id");
        }
    }
    
    private void deleteFlightSchedule() {
        viewAllFlightSchedule();
        System.out.println("\nFlight Schedule Plan Id:");
        Long input = Long.parseLong(sc.nextLine().trim());
        try {
            flightSchedulePlanSessionBeanRemote.deleteFlightSchedule(input);
            System.out.println("succussfully deleted");
        } catch (NoSuchFieldException e) {
            System.out.println("Invalid Id");
        } catch (NoSuchMethodException e) {
            System.out.println("flight schedule disabled");
        }
    }
    
    private void updateFlightSchedulePlan() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Update Flight Schedule Plan Detail\n");
        System.out.println("Enter Flight Number\n");
        String flightNum = sc.nextLine();
        
        try {
            FlightSchedulePlan fsp = flightSchedulePlanSessionBeanRemote.getFlightSchedulePlan(flightNum);
            FlightSchedulePlan rfsp = fsp.getReturnFlightSchedulePlan();
            if (flightSchedulePlanSessionBeanRemote.isBeingUsed(fsp)) {
                throw new RecordBeingUsedException();
            }
            
            while (true) {            
                System.out.println(flightSchedulePlanSessionBeanRemote.flightScheduleDetail(fsp.getFlightSchedulePlanId()));
                System.out.println("1. Update Flight");
                System.out.println("2. Add flight schedule");
                System.out.println("3. remove flight schedule");
                System.out.println("4. exit \n");
                String input = sc.nextLine().trim();
                
                if (input.equals("1")) {
                    viewAllFlight();
                    System.out.println("enter the flight number");
                    input = sc.nextLine().trim();
                    Flight flight = flightSessionBeanRemote.retrieveFlight(input);
                    fsp.setFlightNumber(input);
                    if (rfsp != null) {
                        Flight returnFlight = flight.getReturnFlight();
                        if (returnFlight == null) {
                            throw new NoSuchMethodException();
                        }
                        rfsp.setFlightNumber(returnFlight.getFlightNum());
                        flightSchedulePlanSessionBeanRemote.update(rfsp);
                    }
                    flightSchedulePlanSessionBeanRemote.update(fsp);
                } else if (input.equals("2")) {
                    FlightSchedule f = fsp.getFlightSchedules().get(0);
                    System.out.println("enter date (e.g. 1/12/23)");
                    String[] start = sc.nextLine().split("/");
                    int day = Integer.parseInt(start[0]);
                    int month = Integer.parseInt(start[1]);
                    int year = Integer.parseInt(start[2]) + 2000;
                    LocalDateTime date = LocalDateTime.of(year, Month.of(month), day, 0, 0);
                    //time
                    System.out.println("enter time (e.g. 10:00 AM): ");
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
                    LocalTime time = LocalTime.parse(sc.nextLine(), timeFormatter);
                    LocalDateTime dateTime = date.withHour(time.getHour()).withMinute(time.getMinute());
                    f.setDepartureDateTime(dateTime);
                    flightSchedulePlanSessionBeanRemote.createFlightSchedule(f);
                } else if (input.equals("3")) {
                    FlightSchedule f = fsp.getFlightSchedules().get(0);
                    System.out.println("enter Id ");
                    Long a = Long.parseLong(sc.nextLine().trim());
                    flightSchedulePlanSessionBeanRemote.deleteFlightScheduleInd(a);
                } else if(input.equals("4")) {
                    break;
                }
            }
            
        } catch (NoFlightFoundException e) {
            System.out.println("invalid flight");
        } catch (NoSuchFieldException e) {
            System.out.println("invalid flight number / plan does not exist");
        } catch (NoSuchMethodException e) {
            System.out.println("flight does not have a return flight");
        } catch (RecordBeingUsedException e) {
            System.out.println("flight schedule plan being used, unable to update");
        }
    }
}
