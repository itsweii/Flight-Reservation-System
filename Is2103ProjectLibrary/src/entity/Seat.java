package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class Seat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;
    
    @Column(nullable = false)
    private String seatNumber;

    @ManyToOne
    @JoinColumn
    private CabinConfiguration cabinConfiguration;

    public Seat() {
    }

    public Seat(String seatNumber) {
        this.cabinConfiguration = null;
        this.seatNumber = seatNumber;
    }

    public CabinConfiguration getCabinConfiguration() {
        return cabinConfiguration;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setCabinConfiguration(CabinConfiguration cabinConfiguration) {
        this.cabinConfiguration = cabinConfiguration;
    }
    
    public Long getSeatId() {
        return seatId;
    }

    @Override
    public String toString() {
        return "entity.Seat[ id=" + seatId + " ]";
    }
    
}