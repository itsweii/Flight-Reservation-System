
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
public class ReservedSeat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservedSeatId;
    
    @Column(nullable = false)
    private String cabinClass; //added attribute
    
    @Column (nullable = false)
    private String seatNumber;
    
    @Column (nullable = false)
    private String passengerName;
    
    @Column (nullable = false)
    private String passportNumber;
    
    @Column (nullable = false)
    private String ccInfo;
    
    @Column (nullable = false)
    private int ticketprice;
    
    @OneToOne
    @JoinColumn
    private Seat seat;
    
    @ManyToOne
    @JoinColumn
    private Customer customer;
    
    @ManyToOne
    @JoinColumn
    private FlightSchedule flightSchedule;

    public ReservedSeat() {
    }

    public ReservedSeat(String seatNumber, String cabinClass, String passengerName, String passportNumber,String ccInfo,int ticketprice) {
        this.seatNumber = seatNumber;
        this.cabinClass = cabinClass;
        this.passengerName = passengerName;
        this.passportNumber = passportNumber;
        this.ccInfo = ccInfo;
        this.ticketprice = ticketprice;
    }
    
    public Long getReservedSeatId() {
        return reservedSeatId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public FlightSchedule getFlightSchedule() {
        return flightSchedule;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getCabinClass() {
        return cabinClass;
    }

    public void setCabinClass(String cabinClass) {
        this.cabinClass = cabinClass;
    }

    public void setReservedSeatId(Long reservedSeatId) {
        this.reservedSeatId = reservedSeatId;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setFlightSchedule(FlightSchedule flightSchedule) {
        this.flightSchedule = flightSchedule;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getCcInfo() {
        return ccInfo;
    }

    public void setCcInfo(String ccInfo) {
        this.ccInfo = ccInfo;
    }

    public int getTicketprice() {
        return ticketprice;
    }

    public void setTicketprice(int ticketprice) {
        this.ticketprice = ticketprice;
    }
    
    
    
    @Override
    public String toString() {
        return "entity.ReservedSeat[ id=" + reservedSeatId + " ]";
    }

}
