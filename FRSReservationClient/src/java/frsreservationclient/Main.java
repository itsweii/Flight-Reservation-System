/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package frsreservationclient;

import ejb.session.singleton.TestcaseRemote;
import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.ReservedSeatSessionBeanRemote;
import java.util.Scanner;
import javax.ejb.EJB;


public class Main {

    @EJB
    private static TestcaseRemote testcase;
    
    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;
    @EJB
    private static FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
    @EJB
    private static FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    @EJB
    private static ReservedSeatSessionBeanRemote reservedSeatSessionBeanRemote;
    
    @EJB
    private static AirportSessionBeanRemote airportSessionBeanRemote;

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            flightSchedulePlanSessionBeanRemote.getFlightSchedule(1l);
        } catch (Exception e) {
            testcase.build();
        }
        new FrsReservation(sc, customerSessionBeanRemote, flightScheduleSessionBeanRemote,
                flightSchedulePlanSessionBeanRemote, reservedSeatSessionBeanRemote,airportSessionBeanRemote).run();
        sc.close();
    }
    
}
