package holidayreservationsystem;

import entity.Partner;
import java.util.Scanner;
import ws.entity.NewWebService;

public class App {
    private Scanner sc = new Scanner(System.in);
    private NewWebService port;
    
    public App(NewWebService port) {
        this.port = port;
    }
    
    
    public void run() {
        Partner partner = null;
        while (true) {
            if (partner == null) {
                partner = login();
            }
            
        }
    }
    
    private Partner login() {
        System.out.print("Username: ");
        String username = sc.nextLine().trim();
        System.out.print("Password: ");
        String password = sc.nextLine().trim();
//        Partner = port.g
        return null;
    }
}
