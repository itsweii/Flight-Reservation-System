package ejb.session.stateless;

import entity.Airport;
import entity.Customer;
import entity.FlightSchedule;
import entity.ReservedSeat;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ReservedSeatSessionBean implements ReservedSeatSessionBeanRemote {


    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;

    @Override
    public long createReservedSeat(ReservedSeat rS,Customer cus,FlightSchedule fsc) {
        cus = em.find(Customer.class, cus.getCustomerId());
        fsc = em.find(FlightSchedule.class, fsc.getFlightScheduleId());
        em.persist(rS);
        rS.setCustomer(cus);
        rS.setFlightSchedule(fsc);
        fsc.getReservedSeats().add(rS);
        cus.getReservedSeat().add(rS);
        em.flush();
        
        return rS.getReservedSeatId();
    }
    
    public String getAllReservedSeat(Customer cus) {
        Customer customer = em.find(Customer.class, cus.getCustomerId());
        
        Query query = em.createQuery(
            "SELECT s FROM ReservedSeat s WHERE s.customer.customerId = :customerId");
        query.setParameter("customerId", customer.getCustomerId());
        List<ReservedSeat> reservedSeats = query.getResultList();
        
        StringBuilder sb = new StringBuilder();
        for(ReservedSeat s :reservedSeats){
            sb.append("Reservation Id: " );
            sb.append(s.getReservedSeatId() +"\n");
            sb.append("Flight Number: ");
            sb.append(s.getFlightSchedule().getFlight().getFlightNum() + "\n");
            sb.append("Origin Aiport: ");
            sb.append(s.getFlightSchedule().getFlight().getFlightRoute().getOrigin().getName()+ "\n");
            sb.append("Desination Aiport: ");
            sb.append(s.getFlightSchedule().getFlight().getFlightRoute().getDestination().getName()+ "\n");
            sb.append(s.getFlightSchedule().printfscDetail()+ "\n");
        }
        
        return sb.toString();
       
    }
    
    @Override
    public String viewReservationDetail(Long id) {
  

        
        ReservedSeat s = em.find(ReservedSeat.class, id);
        StringBuilder sb = new StringBuilder();
        
        sb.append("Passenger Name: ");
        sb.append(s.getPassengerName()+ "\n");
        sb.append("Passport Number: ");
        sb.append(s.getPassportNumber()+ "\n");
        sb.append("Seat Number: ");
        sb.append(s.getSeatNumber()+ "\n");
        sb.append("Cvv: ");
        sb.append(s.getCcInfo()+ "\n");
       
        
       return sb.toString();
    }
        
    
    
    
    

}
