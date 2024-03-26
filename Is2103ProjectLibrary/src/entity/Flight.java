package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Flight implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long FlightId;
    
    @Column(nullable = false, length = 32)
    private String flightNumber;
  
    @Column (nullable = false)
    private boolean isReturnFlight;
    
    @Column(nullable = false)
    private boolean isDisabled;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private FlightRoute flightRoute;
    
    @ManyToOne 
    @JoinColumn(name = "aircraftConfigId")
    private AircraftConfiguration aircraftConfig;
    
    @OneToOne
    @JoinColumn (name = "returnFlightId")
    private Flight returnFlight;
    
    public Flight() {
        this.flightRoute = null;
        this.aircraftConfig = null;
        this.returnFlight = null;
        this.isReturnFlight = false;
        this.isDisabled = false;
    }

    public Flight(String flightNumber) {
        this();
        this.flightNumber = flightNumber;
      
    }
    
    public Long getFlightId() {
        return FlightId;
    }
    
    public String getFlightNum() {
        return flightNumber;
    }
    
    public void setFlightNum(String flightNum) {
        this.flightNumber = flightNum;
    }

    public void setFlightRoute(FlightRoute flightRoute) {
        this.flightRoute = flightRoute;
    }

    public void setAircraftConfiguration(AircraftConfiguration aircraftConfig) {
        this.aircraftConfig = aircraftConfig;
    }

    public AircraftConfiguration getAircraftConfiguration() {
        return this.aircraftConfig;
    }
    public FlightRoute getFlightRoute() {
        return this.flightRoute;
    }

    public Flight getReturnFlight() {
        return returnFlight;
    }

    public void setReturnFlight(Flight returnFlight) {
        this.returnFlight = returnFlight;
    }

    public boolean isIsReturnFlight() {
        return isReturnFlight;
    }

    public void setIsReturnFlight(boolean isReturnFlight) {
        this.isReturnFlight = isReturnFlight;
    }

    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public boolean isIsDisabled() {
        return isDisabled;
    }

    public void setAircraftConfig(AircraftConfiguration aircraftConfig) {
        this.aircraftConfig = aircraftConfig;
    }
    
    @Override
    public String toString() {
        String temp = String.format("%s, %s, %s, %s %s", 
                this.flightNumber, 
                this.flightRoute.getOrigin().getIataCode(), 
                this.flightRoute.getDestination().getIataCode(),
                this.aircraftConfig.getAircraftType().getName(),
                this.aircraftConfig.getName());
        if (isReturnFlight && returnFlight != null) {
            return "    " + temp;
        }
        return "\n" + temp;
    }
    
}