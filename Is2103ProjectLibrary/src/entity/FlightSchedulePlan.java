package entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class FlightSchedulePlan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightSchedulePlanId;
    
    @Column(nullable = false)
    private String FlightNumber;
    
    @Column(nullable = false)
    private String planType;
    
    @Column(nullable = false)
    private boolean isReturnFlight;
    
    @Column(nullable = true)
    private LocalDateTime firstDateTime;
    
    @Column(nullable = false)
    private boolean disable;
    
    @OneToMany
    private List<FlightSchedule> flightSchedules;
    
    @ManyToMany
    private List<CabinFee> cabinFees;
    
    @OneToOne
    @JoinColumn
    private FlightSchedulePlan returnFlightSchedulePlan;
    
    public FlightSchedulePlan() {
        this.flightSchedules = new ArrayList<>();
        this.cabinFees = new ArrayList<>();
        this.returnFlightSchedulePlan = null;
        this.isReturnFlight = false;
        this.firstDateTime = null;
        this.disable = false;
    }

    public FlightSchedulePlan(String name, List<CabinFee> cabinFees, String planType) {
        this();
        this.FlightNumber = name;
        this.cabinFees = cabinFees;
        this.planType = planType;
    }

    public List<FlightSchedule> getFlightSchedules() {
        return flightSchedules;
    }

    public String getFlightNumber() {
        return FlightNumber;
    }

    public FlightSchedulePlan getReturnFlightSchedulePlan() {
        return returnFlightSchedulePlan;
    }

    public boolean isIsReturnFlight() {
        return isReturnFlight;
    }

    public void setReturnFlightSchedulePlan(FlightSchedulePlan returnFlightSchedulePlan) {
        this.returnFlightSchedulePlan = returnFlightSchedulePlan;
    }

    public void setIsReturnFlight(boolean isReturnFlight) {
        this.isReturnFlight = isReturnFlight;
    }
    
    public Long getFlightSchedulePlanId() {
        return flightSchedulePlanId;
    }

    public void setFlightNumber(String FlightNumber) {
        this.FlightNumber = FlightNumber;
    }

    public List<CabinFee> getCabinFees() {
        return cabinFees;
    }

    public void setCabinFees(List<CabinFee> cabinFees) {
        this.cabinFees = cabinFees;
    }

    public String getPlanType() {
        return planType;
    }

    public LocalDateTime firstDateTime() {
        LocalDateTime out = flightSchedules.get(0).getDepartureDateTime();
        for (int i = 1; i < flightSchedules.size(); i++) {
            if (out.isAfter(flightSchedules.get(i).getDepartureDateTime())) {
                out = flightSchedules.get(i).getDepartureDateTime();
            }
        }
        this.firstDateTime = out;
        return out;
    }
    
    @Override
    public String toString() {
        String temp = String.format( "%s Flight Schedule Plan(id: %d) Flight %s  %s", planType, flightSchedulePlanId, FlightNumber, firstDateTime().toString());
        if (this.isReturnFlight) {
            return "   " + temp;
        }
        return "\n" + temp;
    }
    
}
