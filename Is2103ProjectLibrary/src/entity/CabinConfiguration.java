package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
public class CabinConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cabinConfigurationId;
    
    @Column(nullable = false, length = 10)
    private String cabinclass; 
    
    @Column(nullable = false)
    @Min(value = 0, message = "Number of cabin class cannot be less than 0")
    @Max(value = 2, message = "Number of cabin class cannot be more than 2")
    private int numOfAisle;
    
    @Column(nullable = false)
    private int numberOfRow;
    
    @Column(nullable = false)
    private int numberOfSeatAbreast;
    
    @Column(nullable = false)
    private List<Integer> configuration;
    
    @Column(nullable = false)
    private int cabinCapacity;
    
    @ManyToMany(mappedBy = "cabinConfigurations")
    private List<AircraftConfiguration> aircraftList; 
    //one cabinconfig can have diff aircraft 
    //one aircraft can have diff config
    //owned side
    
    @OneToMany
    private List<Seat> seats;
    
    /*@OneToMany
    private CabinFee cabinFee;*/

    public CabinConfiguration() {
        this.aircraftList = new ArrayList<AircraftConfiguration> ();
    }

    public CabinConfiguration(String name, int numOfAisle, int numberOfRow, int numberOfSeatAbreast, List<Integer> configuration) {
        this.cabinclass = name;
        this.numOfAisle = numOfAisle;
        this.numberOfRow = numberOfRow;
        this.numberOfSeatAbreast = numberOfSeatAbreast;
        this.configuration = configuration;
        this.cabinCapacity = numberOfRow * numberOfSeatAbreast;
        this.aircraftList = new ArrayList<>();
        this.seats = new ArrayList<>();

    }

    public int getNumOfAisle() {
        return numOfAisle;
    }

    public void setNumOfAisle(int numOfAisle) {
        this.numOfAisle = numOfAisle;
    }

    public int getNumberOfRow() {
        return numberOfRow;
    }

    public void setNumberOfRow(int numberOfRow) {
        this.numberOfRow = numberOfRow;
    }

    public int getNumberOfSeatAbreast() {
        return numberOfSeatAbreast;
    }

    public void setNumberOfSeatAbreast(int numberOfSeatAbreast) {
        this.numberOfSeatAbreast = numberOfSeatAbreast;
    }

    public List<Integer> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(List<Integer> configuration) {
        this.configuration = configuration;
    }

    public Long getCabinConfigurationId() {
        return cabinConfigurationId;
    }

    public void setCabinConfigurationId(Long cabinConfigurationId) {
        this.cabinConfigurationId = cabinConfigurationId;
    }

    public String getCabinclass() {
        return cabinclass;
    }

    public void setCabinclass(String cabinclass) {
        this.cabinclass = cabinclass;
    }

    public int getCabinCapacity() {
        return cabinCapacity;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setMaxCapacity(int numOfRow, int numberOfSeatAbreast) {
        this.cabinCapacity = numOfRow * numberOfSeatAbreast;
    }

    public List<AircraftConfiguration> getAircraftList() {
        return aircraftList;
    }

    public void setAircraftList(List<AircraftConfiguration> aircraftList) {
        this.aircraftList = aircraftList;
    }

    /*public CabinFee getCabinFee() {
        return cabinFee;
    }

    public void setCabinFee(CabinFee cabinFee) {
        this.cabinFee = cabinFee;
    }*/
    
    
    
    public String printSeatConfiguration() {
        StringBuilder sb = new StringBuilder();
        
         for (int i = 0; i < configuration.size(); i++) {
            sb.append(configuration.get(i));

            if (i < configuration.size() - 1) {
                sb.append("-");
            }
        }
      
        return sb.toString();
    }
    
    
    @Override
    public String toString() {
        return (this.cabinclass + ", " + this.numOfAisle + ", " + this.numberOfRow + ", " + this.numberOfSeatAbreast + ", " +
               this.printSeatConfiguration() + this.cabinCapacity);
    }
  
}
//3- 3