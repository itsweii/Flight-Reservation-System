package ejb.session.stateless;

import entity.CabinFee;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hao
 */
@Remote
public interface FlightSchedulePlanSessionBeanRemote {

    public Long createWeeklyFlightSchedulePlan(Long flightId, Long returnFlightId, DayOfWeek dayOfWeek, String timeInput, String startDateInput, String endDateInput, String durationInput, String layoverInput, List<Long> cabinFees);

    public Long createCabinFee(Long id, String name, int fee);
    
    public List<String> getAllFlightSchedulePlan();

    public Long createNFlightSchedulePlan(Long flightId, Long returnFlightId, Long n, LocalDateTime startDate, LocalDateTime endDate, Duration duration, String layoverInput, List<Long> cabinFees);

    public Long createMultipleFlightSchedulePlan(Long flightId, Long returnFlightId, List<LocalDateTime> dateTimes, List<Duration> durations, String layoverInput, List<Long> cabinFees);

    public Long createSingleFlightSchedulePlan(Long flightId, Long returnFlightId, LocalDateTime dateTime, Duration duration, String layoverInput, List<Long> cabinFees);

    public String flightScheduleDetail(Long id) throws NoSuchFieldException;

    public void deleteFlightSchedule(Long id) throws NoSuchFieldException, NoSuchMethodException;

    public FlightSchedulePlan getFlightSchedulePlan(String flightNum) throws NoSuchFieldException;

    public FlightSchedule getFlightSchedule(Long id) throws NoSuchFieldException;

    public String flightScheduleprint(Long id);
    
    public String flightScheduleReservation(Long id);
    
    public boolean isBeingUsed(FlightSchedulePlan fsp);
    
    public void update(FlightSchedulePlan f);
    
    public void createFlightSchedule(FlightSchedule f);
    
    public void deleteFlightScheduleInd(Long id) throws NoSuchFieldException;
    
}
