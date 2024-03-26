package frsmanagementclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.AircraftTypeSessionBeanRemote;
import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.CabinConfigurationSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.AircraftType;
import entity.Airport;
import entity.CabinConfiguration;
import entity.Employee;
import entity.FlightRoute;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.Menu;
import util.exception.CancelledException;
import util.exception.FlightRouteExistException;
import util.exception.LogoutException;
import util.exception.NoAircraftTypeException;
import util.exception.UnknownPersistenceException;
import util.exception.InvalidInputException;
import util.exception.NoAircraftConfigurationException;
import util.exception.NoAirportException;
import util.exception.NoFlightRouteException;

public class FlightPlanningModule {
    private Scanner sc;
    private Employee employee;
    private AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    private AirportSessionBeanRemote airportSessionBeanRemote;
    private FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    private CabinConfigurationSessionBeanRemote cabinConfigurationSessionBeanRemote;
    
    public FlightPlanningModule(Scanner sc, Employee employee, 
                AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote, 
                AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote,
                AirportSessionBeanRemote airportSessionBeanRemote,
                FlightRouteSessionBeanRemote flightRouteSessionBeanRemote,
                CabinConfigurationSessionBeanRemote cabinConfigurationSessionBeanRemote) {
        this.sc = sc;
        this.employee = employee;
        this.aircraftTypeSessionBeanRemote = aircraftTypeSessionBeanRemote;
        this.aircraftConfigurationSessionBeanRemote = aircraftConfigurationSessionBeanRemote;
        this.airportSessionBeanRemote = airportSessionBeanRemote;
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
        this.cabinConfigurationSessionBeanRemote = cabinConfigurationSessionBeanRemote;
    }

    public void fleetManger() throws CancelledException, LogoutException {
        Scanner sc = new Scanner(System.in);
        while (true) {       
            //print menu
            Menu.print(String.format("FRS Management %s Client", employee.getEmployeeType()), new String[] 
                {"Create Aircraft Configuration", 
                "View all Aircraft Configuration", 
                "View Aircraft Configuration Details", 
                "logout"});
            System.out.print("> ");
            String input = sc.nextLine().trim();
            
            if (input.length() > 0) {
                input = input.substring(0,1);
                
                if (input.equals("1")) { //Create Aircraft Configuration
                    System.out.println("Create Aircraft Configuration\n");
                    createAircraftConfiguration();
                    
                } else if (input.equals("2")) {
                    System.out.println("View All Aircraft Configurations\n");
                    List<AircraftConfiguration> list = aircraftConfigurationSessionBeanRemote.getAllAircraftConfiguration();
                    System.out.printf("%-15s%-32s%-20s%-20s\n","Aircraft","Name","NumOfCabinClass","NumberOfSeats");
                    for (AircraftConfiguration ac : list) {
                        System.out.printf("%-15s%-32s%-20d%-20d\n",
                                ac.getAircraftType().getName(),
                                ac.getName(),
                                ac.getNumOfCabinClass(),
                                ac.getNumberOfSeats());
                    }
                    System.out.println("");
                
                } else if (input.equals("3")) {
                    System.out.println("\nView Aircraft Configuration Details\n");
                    System.out.print("Aircraft Configuration (e.g. Boeing 737 Tiny):\n");
                    input = sc.nextLine().trim();
                    try {
                        System.out.println(aircraftConfigurationSessionBeanRemote.getCabinConfigurations(input));
                    } catch (NoAircraftConfigurationException e) {
                        Menu.clear();
                        System.out.println("no aircraft configuration was found\n");
                    } catch (UnknownPersistenceException e) {
                        System.out.println(e.toString());
                    }
                } else if (input.equals("4")) {
                    throw new LogoutException();
                } else {
                    throw new CancelledException("FRS Management FleetManger Client");
                }
          
            } else {
                    throw new CancelledException("FRS Management FleetManger Client");
            }
        }
    }
    
    
    private void createAircraftConfiguration() {
        Scanner sc = new Scanner(System.in);
        
        try {
                System.out.print("Aircraft Type: ");
                String aircraftTypeString = sc.nextLine().trim();
                if (aircraftTypeString.length() == 0) {
                    throw new InvalidInputException("Invalid Aircraft Type"); 
                }
                AircraftType aircraftType = aircraftTypeSessionBeanRemote.getAircraftType(aircraftTypeString);

                System.out.print("Aircraft Configuration Name: ");
                String name = sc.nextLine().trim();//for aircraft name
                String configName = aircraftType.getName() + " " + name;
                if (name.length() == 0) {
                    throw new InvalidInputException("Invalid Aircraft Configuration Name");
                } else {
                    try {
                        AircraftConfiguration ac = aircraftConfigurationSessionBeanRemote.getAircraftConfiguration(name);
                        if (ac.getAircraftType().getName().equals(aircraftType.getName())) {
                            throw new InvalidInputException("Aircraft Configuration Name existed");
                        }
                    } catch (InvalidInputException e) {
                        throw new InvalidInputException("Aircraft Configuration Name existed");
                    } catch (Exception e) {
                    }
                } //check whether the aircraft has already been created

                System.out.println("");

                System.out.print("Number of Cabin(s): ");
                String input = sc.nextLine().trim();
                if (input.length() == 0) {
                    throw new InvalidInputException("Invalid number of cabin");
                }
                int cabinNum = Integer.parseInt(input);
                int totalseats = 0;

                List<CabinConfiguration> cabinList = new ArrayList<>();
                int start = 1;
                while (start <= cabinNum) {
                    try {
                        System.out.println("\nCabin " + start);

                        System.out.println("Enter the cabin class");
                        String cabinClass = sc.nextLine().toUpperCase();
                            
                        System.out.println("Enter the cabin configuration (e.g. 3-4-3)");
                        input = sc.nextLine().trim();

                        if (!input.contains("-")) {
                            throw new InvalidInputException("Invalid Cabin Configuration");
                        }

                        String[] setup = input.split("-");
                        List<Integer> config = new ArrayList<>();

                        for (String configPart : setup) {
                            try {
                                int intValue = Integer.parseInt(configPart);
                                config.add(intValue);
                            } catch (NumberFormatException e) {
                                throw new InvalidInputException("Invalid Cabin Configuration: " + e.getMessage());
                            }
                        }

                        System.out.print("\nEnter the number of row: ");
                        input = sc.nextLine().trim();
                        if (input.length() == 0) {
                            throw new InvalidInputException("Invalid Number of Row");
                        }
                        int numberOfRow = Integer.parseInt(input);


                        System.out.print("\nEnter the number of aisle: ");
                        input = sc.nextLine().trim();
                        if (input.length() == 0) {
                            throw new InvalidInputException("Invalid Number of aisle");
                        }
                        int numberOfAisle = Integer.parseInt(input);

                        System.out.print("\nEnter the number of seat abreast: ");
                        input = sc.nextLine().trim();
                        if (input.length() == 0) {
                            throw new InvalidInputException("Invalid Number of aisle");
                        }
                        int numberOfSeatAbreast = Integer.parseInt(input);

                        CabinConfiguration cabinConfiguration = new CabinConfiguration(cabinClass, numberOfAisle, numberOfRow,numberOfSeatAbreast,config);

                        cabinList.add(cabinConfiguration);

                        totalseats += cabinConfiguration.getCabinCapacity();
                        start++;
                    } catch (InvalidInputException e) {
                        System.out.println(e.toString());
                    }
                }
                if (totalseats > aircraftType.getSeatCapacity()) {
                    throw new InvalidInputException("Invalid Cabin Seats Configuration");
                }
                AircraftConfiguration aircraftConfiguration = new AircraftConfiguration(configName, cabinNum,totalseats,cabinList,aircraftType);
                long aircraftId = aircraftConfigurationSessionBeanRemote.createAircraftConfiguration(aircraftConfiguration);
                Menu.clear();
                System.out.println("Aircraft Configuration (id = " + aircraftId + " ) successfully created\n");
            } catch (InvalidInputException e) {
                Menu.clear();
                System.out.println(e.getMessage());
                System.out.println("");
            } catch (NoAircraftTypeException e) {
                Menu.clear();
                System.out.println("Invalid Aircraft Type\n");
            } catch (UnknownPersistenceException e) {
                Menu.clear();
                System.out.println(e.toString());
                System.out.println("");
            }
      

    }
    

    
    public void routePlanner() throws CancelledException, LogoutException{
        Scanner sc = new Scanner(System.in);
        while (true) {       
            //print menu
            Menu.print(String.format("FRS Management %s Client", employee.getEmployeeType()), new String[] 
                {"Create Flight Route", 
                "View all Flight Routes", 
                "Delete Flight Route", 
                "Logout"});
            System.out.print("> ");
            Integer input = 0;
            
            while(input < 1 || input > 4) {
                input = sc.nextInt();
                
                if (input == 1) { //Create Flight Route
                    createFlightRoute();
                    
                } else if (input == 2) {
                    
                    List<FlightRoute> frList = flightRouteSessionBeanRemote.retrieveAllFlightRoutes();
                    FlightRoute prev = null;
                    System.out.println("View all flight routes\n");
                    for(FlightRoute fr: frList) {
                        String out = fr.printout();
                        if (prev != null) {
                            if (prev.getReturnRoute() != null) {
                                if (prev.getReturnRoute().getFlightRouteId() == fr.getFlightRouteId()) {
                                    out = "   " + out;
                                } else {
                                    out = "\n" + out;
                                }
                            }
                        }
                        prev = fr;
                        System.out.println(out);
                    }
                } else if (input == 3) {
                    deleteFlightRoute();
                    
                } else if (input == 4) {
                  throw new LogoutException();
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
        }
    }   
    
    
        private void createFlightRoute() {
            Scanner sc = new Scanner(System.in);
            try {
                    System.out.println("\nCreate Flight Route\n");
                    System.out.println("Enter Origin Airport IATA code> ");
                    String origin = sc.nextLine().trim();
                    System.out.println("Enter Destination Airport IATA code> ");
                    String destination = sc.nextLine().trim();
                    Airport originAirport = airportSessionBeanRemote.findAirportByIATA(origin);
                    Airport destinationAirport = airportSessionBeanRemote.findAirportByIATA(destination);
                    if (originAirport.getIataCode().equals(destinationAirport.getIataCode())) {
                        throw new NoSuchMethodException("orgin cannot be the same as destination");
                    }
                    FlightRoute flightRoute = new FlightRoute(originAirport,destinationAirport);
                    
                    Long start = flightRouteSessionBeanRemote.createFlightRoute(flightRoute);
                    System.out.println("FlightRoute Created! FlightRoute ID >" + start);

                    while (true) {
                        System.out.println("Create return route? Yes/No\n");
                        String ans = sc.nextLine().trim();

                        if (ans.equalsIgnoreCase("yes")) {
                            FlightRoute returnRoute = new FlightRoute(destinationAirport,originAirport);
                            Long end = flightRouteSessionBeanRemote.createFlightRoute(returnRoute);
                            System.out.println("FlightRoute Created! FlightRoute ID >" + end);
                            flightRouteSessionBeanRemote.addReturnRoute(start, end);
                            break;
                        } else if (ans.equalsIgnoreCase("no")) {
                            break;
                        } else {
                            System.out.println("Invalid input, please try again!\n");           
                        }
                    }
                } catch (NoSuchMethodException e) {
                    System.out.println(e.getMessage());
                } catch (NoAirportException ex){
                    System.out.println(ex.getMessage());
                } catch (FlightRouteExistException ex) {
                    System.out.println(ex.getMessage());
                }
        }
        
            private void deleteFlightRoute() {
            Scanner sc = new Scanner(System.in);
            try{
                System.out.println("Delete Flight Route\n");
                System.out.println("Enter Origin Airport IATA code> ");
                String origin = sc.nextLine().trim();
                System.out.println("Enter Destination Airport IATA code> ");
                String destination = sc.nextLine().trim();
                Airport originAirport = airportSessionBeanRemote.findAirportByIATA(origin);
                Airport destinationAirport = airportSessionBeanRemote.findAirportByIATA(destination);
                FlightRoute fr = flightRouteSessionBeanRemote.findFlightRoute(originAirport,destinationAirport);
                System.out.println("\nFlightRoute: " + flightRouteSessionBeanRemote.deleteFlightRoute(originAirport,destinationAirport));
                
                if (fr.getReturnRoute() != null) {
                    System.out.println("Delete return route? Yes/No\n");
                    while (true) {
                        String ans = sc.nextLine().trim();
                        if (ans.equalsIgnoreCase("yes")) {
                             System.out.println("FlightRoute: " + 
                                flightRouteSessionBeanRemote.deleteFlightRoute(destinationAirport,originAirport));
                             break;
                        } else if (ans.equalsIgnoreCase("no")) {
                            break;
                        } else {
                            System.out.println("Invalid input, please try again!\n");           
                        }
                    }
                }
                    
            }  catch (NoFlightRouteException ex) {
                System.out.println(ex.getMessage());
            } catch (NoAirportException ex) {
                System.out.println(ex.getMessage());
            }
        }
}
