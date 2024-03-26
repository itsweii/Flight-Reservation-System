package entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class FlightRoute implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightRouteId;
   
    @Column(nullable = false)
    private boolean isDisabled;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private Airport origin;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private Airport destination;
    
    @OneToOne
    @JoinColumn
    private FlightRoute returnRoute;
    
    public FlightRoute() {
    }

    public FlightRoute(Airport origin, Airport destination) {
        this.origin = origin;
        this.destination = destination;
        this.returnRoute = null;
//        this.flight = new ArrayList<> ();
        this.isDisabled = false;
    }

    public Long getFlightRouteId() {
        return flightRouteId;
    }

    public Airport getOrigin() {
        return origin;
    }

    public void setOrigin(Airport origin) {
        this.origin = origin;
    }

    public Airport getDestination() {
        return destination;
    }

    public void setDestination(Airport destination) {
        this.destination = destination;
    }

    public FlightRoute getReturnRoute() {
        return returnRoute;
    }

    public void setReturnRoute(FlightRoute returnRoute) {
        this.returnRoute = returnRoute;
    }

    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public boolean isIsDisabled() {
        return isDisabled;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FlightRoute other = (FlightRoute) obj;
        if (!Objects.equals(this.flightRouteId, other.flightRouteId)) {
            return false;
        }
        if (!Objects.equals(this.origin, other.origin)) {
            return false;
        }
        if (!Objects.equals(this.destination, other.destination)) {
            return false;
        }
        if (!Objects.equals(this.returnRoute, other.returnRoute)) {
            return false;
        }
        return true;
    }
    
    public String printout() {
        String out = origin.getIataCode() + ", " + destination.getIataCode();
        if (isDisabled) {
            return out + "(disabled)";
        }
        return out;
    }

    @Override
    public String toString() {
        return origin.getIataCode() + "-" + destination.getIataCode();
    }

   
    
}
