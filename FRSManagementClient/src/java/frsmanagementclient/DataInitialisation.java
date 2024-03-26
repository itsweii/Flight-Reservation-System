package frsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import entity.Employee;
import entity.Partner;
import java.util.Scanner;
import util.Menu;
import util.enumeration.EmployeeType;
import util.exception.CancelledException;
import util.exception.UnknownPersistenceException;

//not in use
public class DataInitialisation {
    private Scanner sc;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;

    public DataInitialisation(Scanner sc, EmployeeSessionBeanRemote employeeSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote) {
        this.sc = sc;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
    }
    
    public void run() {
        while (true) {
            Menu.print("Data Initialisation", new String[] 
                {"Create New Employee", "Create New Partner", "Create New Airport", "Create New Aircraft Type"});

            String input = getSelection();
            if (input.equals("1")) {
                try {
                    Menu.clear();
                    System.out.println(String.format("Employee (ID: %d) successfully created\n", createNewEmployee()));
                } catch (UnknownPersistenceException e) {
                    Menu.clear();
                    System.out.println("Create new employee failed");
                    System.out.println(e.toString());
                    System.out.println("");
                } catch (CancelledException e) {
                    Menu.clear();
                    System.out.println(e.getMessage());
                    System.out.println("");
                }
            } else if (input.equals("2")) {
                try {
                    Menu.clear();
                    System.out.println(String.format("Partner (ID: %d) successfully created\n", createNewPartner()));
                } catch (UnknownPersistenceException e) {
                    Menu.clear();
                    System.out.println("Create new Partner failed");
                    System.out.println(e.toString());
                    System.out.println("");
                } catch (CancelledException e) {
                    Menu.clear();
                    System.out.println(e.getMessage());
                    System.out.println("");
                }
            } else if (input.equals("3")) {
                try {
                    Menu.clear();
                    System.out.println(String.format("Airport (ID: %d) successfully created\n", createNewAirport()));
                } catch (UnknownPersistenceException e) {
                    Menu.clear();
                    System.out.println("Create new Airport failed");
                    System.out.println(e.toString());
                    System.out.println("");
                } catch (CancelledException e) {
                    Menu.clear();
                    System.out.println(e.getMessage());
                    System.out.println("");
                }
            } else if (input.equals("4")) {
                createNewAircraftType();
            } else {
                break;
            }
        }
    }
    
    private long createNewEmployee() throws UnknownPersistenceException, CancelledException {
        System.out.println("Create New Employee\n");
        
        System.out.println("Enter username (enter without input to stop creating)");
        String username = "";
        while (true) {
            username = getInput();
            if (username.length() > 0) {
                if (employeeSessionBeanRemote.usernameExist(username)) {
                    System.out.println("username exist, please enter a new username");
                } else {
                    System.out.println("");
                    break;
                }
            } else {
                throw new CancelledException("Create New Employee");
            }
        }
        
        System.out.println("Enter password (enter without input to stop creating)");
        String password = "";
        while (true) {
            password = getInput();
            if (password.length() > 0) {
                System.out.println("");
                break;
            } else {
                throw new CancelledException("Create New Employee");
            }
        }
        
        EmployeeType type = null;
        Menu.print("Select the Employee Type (enter without input to stop creating)", new String[] 
            {"FleetManager", "RoutePlanner", "ScheduleManger", "SaleManager"});
        while(true) {
            String input = getInput();
            if (input.length() > 0) {
                int selection = Integer.parseInt(input.substring(0,1));
                if (selection > 0 && selection < 5) {
                    switch(selection) {
                        case 1:
                            type = EmployeeType.FleetManager;
                            break;
                        case 2:
                            type = EmployeeType.RoutePlanner;
                            break;
                        case 3:
                            type = EmployeeType.ScheduleManger;
                            break;
                        case 4:
                            type = EmployeeType.SaleManager;
                    }
                    break;
                } else {
                    System.out.println("Invalid Selection");
                }
                Menu.clear();
            } else {
                throw new CancelledException("Create New Employee");
            }
        }
        
        System.out.println("Enter the name (enter without input to stop creating)");
        System.out.print("> ");
        String name = sc.nextLine();
        if (name.length() == 0) {
            throw new CancelledException("Create New Employee");
        }
        
        Employee employee = new Employee(type, name, username, password);
        Menu.clear();
        return employeeSessionBeanRemote.createEmployee(employee);
    }


    private long createNewPartner() throws UnknownPersistenceException, CancelledException {
        System.out.println("Create New Partner\n");
        
        System.out.println("Enter username (enter without input to stop creating)");
        String username = "";
        while (true) {
            username = getInput();
            if (username.length() > 0) {
                if (partnerSessionBeanRemote.usernameExist(username)) {
                    System.out.println("username exist, please enter a new username");
                } else {
                    System.out.println("");
                    break;
                }
            } else {
                throw new CancelledException("Create New Partner");
            }
        }
        
        System.out.println("Enter password (enter without input to stop creating)");
        String password = "";
        while (true) {
            password = getInput();
            if (password.length() > 0) {
                System.out.println("");
                break;
            } else {
                throw new CancelledException("Create New Partner");
            }
        }

        System.out.println("Enter the name (enter without input to stop creating)");
        System.out.print("> ");
        String name = sc.nextLine();
        if (name.length() == 0) {
            throw new CancelledException("Create New Partner");
        }
        
        Partner partner = new Partner(name, username, password);
        Menu.clear();
        return partnerSessionBeanRemote.createPartner(partner);
    }

    private long createNewAirport() throws UnknownPersistenceException, CancelledException {
        System.out.println("Create New Airport");
        Menu.clear();
        return 1;
    }

    private void createNewAircraftType() {
        System.out.println("Create New Aircraft Type");
        Menu.clear();
    }

    //get only the number when getting the selection
    private String getSelection() {
        System.out.print(">");
        String temp = sc.nextLine().trim();
        return temp.substring(0, 1);
    }
    
    private String getInput() {
        System.out.print(">");
        String temp = sc.nextLine().trim();
        return temp;
    }
}
