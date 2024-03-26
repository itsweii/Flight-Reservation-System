package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AircraftType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aircraftId;
    
    @Column(nullable = false, length = 32)
    private String name;
    
    @Column(nullable = false)
    private int seatCapacity;

    public AircraftType() {
    }

    public AircraftType(String name, int seatCapacity) {
        this.name = name;
        this.seatCapacity = seatCapacity;
    }
    
    public Long getAircraftId() {
        return aircraftId;
    }

    public String getName() {
        return name;
    }

    public int getSeatCapacity() {
        return seatCapacity;
    }

    @Override
    public String toString() {
        return String.format("Aicraft: %s\nSeats: %d", this.name, this.seatCapacity);
    }
    
}
