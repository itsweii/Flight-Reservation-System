package ejb.session.stateless;

import entity.CabinConfiguration;
import entity.CabinFee;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.ReservedSeat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class FlightScheduleSessionBean implements FlightScheduleSessionBeanRemote {
    
    
    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;
    
 
    @Override
    public List<FlightSchedule> searchFlightSchedules(String origin,String destination,LocalDateTime startTime,LocalDateTime endTime) {
        
        Query query = em.createQuery(
            "SELECT fs FROM FlightSchedule fs "
                    + "WHERE fs.departureDateTime BETWEEN :startDateTime AND :endDateTime",
            FlightSchedule.class
        ); 
        query.setParameter("startDateTime", startTime);
        query.setParameter("endDateTime", endTime);

        List<FlightSchedule> resultList = new ArrayList<FlightSchedule> ();
        
        for (FlightSchedule fsc : (List<FlightSchedule>)query.getResultList()) {
            
            if (fsc.getFlight().getFlightRoute().getOrigin().getIataCode().equals(origin) &&
            fsc.getFlight().getFlightRoute().getDestination().getIataCode().equals(destination))
            {
                resultList.add(fsc);
            }
        }
    
        
        return resultList;
    }
    
    @Override
    public String printFlightSchedule(List<FlightSchedule> list, int passengerNum, String Cpref) {
        
        StringBuilder sb = new StringBuilder();
        
            for (FlightSchedule fsc : list) {
                sb.append(String.format("%-20s%-15s%-20s%-30s\n", "FlightSchedule ID","FlightNumber", "Time", "Duration"));
                LocalDateTime departureDateTime = fsc.getDepartureDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String formattedDate = formatter.format(departureDateTime);
              
                    sb.append(String.format("%-20s%-15s%-20s%-30s\n",
                                fsc.getFlightScheduleId(),
                                fsc.getFlight().getFlightNum(),
                                formattedDate,
                                fsc.getEstimatedDuration().toHours()));

                sb.append(String.format("%-20s%-20s%-20s%-20s\n","CabinAvailability","Price/Ticket","TotalPrice","Seats Available"));
                FlightSchedule temp = em.find(FlightSchedule.class, fsc.getFlightScheduleId());
                FlightSchedulePlan plan = em.find(FlightSchedulePlan.class, temp.getFlightSchedulePlan().getFlightSchedulePlanId());
                //temp.getFlight().getAircraftConfiguration().getCabinConfigurations()
                
                for (CabinFee cf: plan.getCabinFees()) {
                    cf = em.find(CabinFee.class, cf.getCabinFeeId());//managed
                    CabinConfiguration config = em.find(CabinConfiguration.class, cf.getCabinConfiguration().getCabinConfigurationId());
                    String cabinClass = config.getCabinclass();//get cc class letter
               
                        if(!Cpref.equals("N")) {// check cc preference
                            if (cabinClass.equals(Cpref)) {
                                sb.append(String.format("%-20s%-20d%-20d%-20d\n",
                                cf.getCabinConfiguration().getCabinclass(),
                                cf.getFee(),
                                (cf.getFee() * passengerNum),
                                checkSeatAvailbility(fsc.getFlightScheduleId(),cabinClass)));
                        }
                            
                            
                    } else {
                        sb.append(String.format("%-20s%-20d%-20d%-20d\n",
                                cf.getCabinConfiguration().getCabinclass(),
                                cf.getFee(),
                                (cf.getFee() * passengerNum),
                                checkSeatAvailbility(fsc.getFlightScheduleId(),cabinClass)));
                        }
                    }
                }
            
            return sb.toString();
        }
    
    
    @Override
    public List<FlightSchedule>  searchConnectingFscOrigin (String origin,LocalDateTime startTime,LocalDateTime endTime) {
        
           Query query = em.createQuery(
            "SELECT fs FROM FlightSchedule fs "
                    + "WHERE fs.departureDateTime BETWEEN :startDateTime AND :endDateTime",
            FlightSchedule.class); 
            query.setParameter("startDateTime", startTime);
            query.setParameter("endDateTime", endTime);

            List<FlightSchedule> resultList = new ArrayList<> ();

            for (FlightSchedule fsc : (List<FlightSchedule>)query.getResultList()) {

                if (fsc.getFlight().getFlightRoute().getOrigin().getIataCode().equals(origin)){
                    resultList.add(fsc);
                }
            }
        
        return resultList;
    }
    
    @Override
    public List<FlightSchedule>  searchConnectingFscDestination (String destination ,LocalDateTime startTime,LocalDateTime endTime){
        
            Query query = em.createQuery(
            "SELECT fs FROM FlightSchedule fs "
                    + "WHERE fs.departureDateTime BETWEEN :startDateTime AND :endDateTime",
            FlightSchedule.class); 
            
            query.setParameter("startDateTime", startTime);
            query.setParameter("endDateTime", endTime);

            List<FlightSchedule> resultList = new ArrayList<> ();

            for (FlightSchedule fsc : (List<FlightSchedule>)query.getResultList()) {

                if (fsc.getFlight().getFlightRoute().getDestination().getIataCode().equals(destination)){
                    resultList.add(fsc);
                }
            }
        
        return resultList;
    }
    
    @Override
    public List<FlightSchedule> getConnectingFlights(List<FlightSchedule> originFlights,List<FlightSchedule> destinationFlights) {
        
        List<FlightSchedule> resultList = new ArrayList<FlightSchedule> ();

        for (FlightSchedule ofc: originFlights) {
            for (FlightSchedule dfc: destinationFlights) {
                ofc = em.find(FlightSchedule.class, ofc.getFlightScheduleId());
                dfc = em.find(FlightSchedule.class, dfc.getFlightScheduleId());
                
                if(ofc.getArrivalTime().isBefore(dfc.getDepartureDateTime())) {
                    if(ofc.getFlight().getFlightRoute().getDestination()
                            .equals(dfc.getFlight().getFlightRoute().getOrigin())) {
                                resultList.add(ofc);
                                resultList.add(dfc);
                    }
                }
            }
       }
        
        System.out.print(resultList.size());
        return resultList;
            
    }
    
    @Override
    public String printConnectingFlights(List<FlightSchedule> list) {
        StringBuilder sb = new StringBuilder();
        
            for (int i = 0; i < list.size(); i ++) {
            sb.append(String.format("%-20s%-20s%-15s%-20s%-30s\n", "FlightSchedule ID","FlightNumber","O-D","Time", "Duration"));  
                FlightSchedule fsc = list.get(i);
                LocalDateTime departureDateTime = fsc.getDepartureDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String formattedDate = formatter.format(departureDateTime);
              
                    sb.append(String.format("%-15s%-15s%-15s%-20s%-30s\n",
                        fsc.getFlightScheduleId(),
                        fsc.getFlight().getFlightNum(),
                        fsc.getFlight().getFlightRoute().toString(),
                        formattedDate,
                        fsc.getEstimatedDuration().toHours()));

                FlightSchedule temp = em.find(FlightSchedule.class, fsc.getFlightScheduleId());
                List<CabinFee> cf = temp.getFlightSchedulePlan().getCabinFees();
                int fare = 0;
                for (int j = 0; j < cf.size(); j++) {
                    CabinFee fee = cf.get(j);
                    sb.append(" Cabin Availability: ");
                    sb.append(fee.getCabinConfiguration().getCabinclass());
                    fare += fee.getFee();
                    
                    if(j % 2 != 0) {
                     sb.append(" Total Fare: ");
                     sb.append(fare);
                     fare = 0;
                    }
                }
            }
               return sb.toString();
       
    }
    
    @Override
    public int checkSeatAvailbility(Long id, String cclass) {
    
        FlightSchedule fsc = em.find(FlightSchedule.class,id);
        int seatAvail = 0;
        
        for(CabinConfiguration cc: fsc.getFlight().getAircraftConfiguration().getCabinConfigurations()) {
            cc = em.find(CabinConfiguration.class, cc.getCabinConfigurationId());
            
            if(cc.getCabinclass().equals(cclass)){
                seatAvail = cc.getCabinCapacity();
                
                    for(ReservedSeat rS :fsc.getReservedSeats()){
                        rS = em.find(ReservedSeat.class, rS.getReservedSeatId());
                        if (rS.getCabinClass().equals(cclass)){//check the reserve seat list
                            seatAvail --;
                        
                        }
                    }
                }
                
            }
        
        return seatAvail;
    }
    
    
    @Override
    public FlightSchedule findFlightSchedule(Long id) {
        return em.find(FlightSchedule.class,id);
       
    }
    /*@Override
    public int getTicketPrice(Long id,String cc) {
        FlightSchedule temp = em.find(FlightSchedule.class,id);
        
        for(CabinFee cb: temp.getFlightSchedulePlan().getCabinFees()) {
            cb = em.find(CabinFee.class, cb.getCabinFeeId());
            if(cb.getName().equals(cc)) {
                return cb.getFee();
            }
        }
        return 0;
                
    }*/
    
    @Override
    public String findSeatConfiguration (FlightSchedule fsc,String cabinClass){
        FlightSchedule temp = em.find(FlightSchedule.class, fsc.getFlightScheduleId());
        StringBuilder sb = new StringBuilder();
        sb.append("\n***Cabin Configuration***\n");
        
        for(CabinConfiguration cf: temp.getFlight().getAircraftConfiguration().getCabinConfigurations()){
            
        
                if (cf.getCabinclass().equals(cabinClass)) { //retrieve the required cabinclass
                    sb.append("Number of row: ");
                    sb.append(cf.getNumberOfRow() + "\n");
                    
                    char baseLetter = 'A';
                    StringBuilder seatLetter = new StringBuilder();
                    //to print the seat letter eg:ABCDEF
                    for (int i = 0; i < cf.getNumberOfSeatAbreast(); i++) {
                        char currentLetter = (char) (baseLetter + i);
                        seatLetter.append(currentLetter);
                    }
                    sb.append("Seat Designation: ");
                    sb.append(seatLetter + "\n");
                    sb.append("Seat Configuration: ");
                    sb.append(cf.printSeatConfiguration() + "\n");
                    

                    // the configuration of 3-3
                    
                }
        }
        
        return sb.toString();
    }
    
    
               
           
                    
                    
               
    
    
    }
    

    /*
                    for(ReservedSeat rS :temp.getReservedSeats()){
                        if(!temp.getReservedSeats().isEmpty()) {
                        rS = em.find(ReservedSeat.class, rS.getReservedSeatId());
                        if (rS.getCabinClass().equals(cabinClass)){//check the reserve seat list
                            count ++;//if match, ++
                            System.out.print(count);
                            }
                        }
                    }
    
                    if (config.getCabinCapacity() - count >= passengerNum) { //if still have seats avail, print cc

                         //System.out.print(cabinClass);
                    //int count = 0;
              */