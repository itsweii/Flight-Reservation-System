package ejb.session.stateless;

import entity.Customer;
import entity.FlightSchedule;
import entity.ReservedSeat;
import java.time.LocalDateTime;
import javax.ejb.Remote;

@Remote
public interface ReservedSeatSessionBeanRemote {
    public long createReservedSeat(ReservedSeat rS,Customer customer,FlightSchedule fsc);
    public String getAllReservedSeat(Customer cus);
    public String viewReservationDetail(Long id);
   
}
