/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hao
 */
@Remote
public interface FlightScheduleSessionBeanRemote {
    public List<FlightSchedule> searchFlightSchedules(String origin,String destination,LocalDateTime startTime,LocalDateTime endTime);
    
    
    public List<FlightSchedule>  searchConnectingFscOrigin (String location,LocalDateTime startTime,LocalDateTime endTime);
    
    public List<FlightSchedule>  searchConnectingFscDestination (String destination ,LocalDateTime startTime,LocalDateTime endTime);
    
    //public FlightSchedule findFlightSchedule(Long id);
    
    public String printFlightSchedule(List<FlightSchedule> list, int passengerNum, String Cpref);
    
    public List<FlightSchedule> getConnectingFlights(List<FlightSchedule> originFlights,List<FlightSchedule> destinationFlights);
    
    public String printConnectingFlights(List<FlightSchedule> list);
    
    public String findSeatConfiguration (FlightSchedule fsc,String cabinClass);
    
    public int checkSeatAvailbility(Long id, String cclass);
    
    public FlightSchedule findFlightSchedule(Long id);
    //public int getTicketPrice(Long id,String cc);
    
    
 
    
}
