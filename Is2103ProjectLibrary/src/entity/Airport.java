package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Airport implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AirportId;
    
    @Column(nullable = false, length = 32)
    private String name;
    
    @Column(nullable = false, length = 3)
    private String IataCode;
    
    @Column(nullable = false, length = 32)
    private String city;
    
    @Column(nullable = false, length = 32)
    private String state;
    
    @Column(nullable = false, length = 32)
    private String country;

    public Airport() {
    }

    public Airport(String name, String IataCode, String city, String state, String country) {
        this.name = name;
        this.IataCode = IataCode;
        this.city = city;
        this.state = state;
        this.country = country;
    }
    
    public Long getAirportId() {
        return AirportId;
    }
    
    public String getIataCode() {
        return this.IataCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    

    @Override
    public String toString() {
        return "entity.Airport[ id=" + AirportId + " ]";
    }
    
}
