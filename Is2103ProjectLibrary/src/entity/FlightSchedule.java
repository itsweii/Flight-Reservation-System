package entity;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class FlightSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightScheduleId;
    
    @Column(nullable = false)
    private LocalDateTime departureDateTime;
    
    @Column(nullable = false)
    private Duration estimatedDuration;
    
    @Column(nullable = false)
    private int totalSeats;
    
    @Column(nullable = false)
    private int remainingSeats;
    
    @ManyToOne
    @JoinColumn
    private FlightSchedulePlan flightSchedulePlan;
    
    @OneToMany
    private List<ReservedSeat> reservedSeats;

    @ManyToOne
    @JoinColumn
    private Flight flight;
    
    public FlightSchedule() {
        this.flightSchedulePlan = null;
        this.reservedSeats = new ArrayList<>();
        this.totalSeats = 0;
        this.remainingSeats = 0;
    }

    public FlightSchedule(LocalDateTime departureDateTime, Duration estimatedDuration, FlightSchedulePlan flightSchedulePlan, Flight flight) {
        this.departureDateTime = departureDateTime;
        this.estimatedDuration = estimatedDuration;
        this.flightSchedulePlan = flightSchedulePlan;
        this.flight = flight;
        this.reservedSeats = new ArrayList<>();
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }
    
    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public Duration getEstimatedDuration() {
        return estimatedDuration;
    }

    public FlightSchedulePlan getFlightSchedulePlan() {
        return flightSchedulePlan;
    }

    public List<ReservedSeat> getReservedSeats() {
        return reservedSeats;
    }

    public void setDepartureDateTime(LocalDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getRemainingSeats() {
        return remainingSeats;
    }
    
    public Long getFlightScheduleId() {
        return flightScheduleId;
    }

    public void setSeats(int totalSeats) {
        this.totalSeats = totalSeats;
        this.remainingSeats = totalSeats;
    }

    public void setFlightScheduleId(Long flightScheduleId) {
        this.flightScheduleId = flightScheduleId;
    }

    public void setEstimatedDuration(Duration estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public void setRemainingSeats(int remainingSeats) {
        this.remainingSeats = remainingSeats;
    }

    public void setFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        this.flightSchedulePlan = flightSchedulePlan;
    }

    public void setReservedSeats(List<ReservedSeat> reservedSeats) {
        this.reservedSeats = reservedSeats;
    }
    
    
 
    public String printfscDetail() {
        return String.format("Flight Schedule(id: %s) dept: %s  dur: %shr(s)", flightScheduleId, departureDateTime.toString(), 
                estimatedDuration.toHours());
    }
    
    public LocalDateTime getArrivalTime() {
        return departureDateTime.plus(estimatedDuration);
    }

    @Override
    public String toString() {
        return String.format("Flight Schedule(id: %s) dept: %s  dur: %shr(s)  tol: %d  rem: %d", flightScheduleId, departureDateTime.toString(), 
                estimatedDuration.toHours(), totalSeats, remainingSeats);
    }
    
    
}
