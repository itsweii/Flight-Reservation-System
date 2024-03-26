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
import javax.persistence.CascadeType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
public class AircraftConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AircraftConfigurationId;
    
    @Column(nullable = false, length = 32)
    private String name;
       
    @Column(nullable = false)
    @Min(value = 1, message = "Number of cabin class cannot be less than 1")
    @Max(value = 4, message = "Number of cabin class cannot be more than 4")
    private int numOfCabinClass;
    
    @Column(nullable = false)
    private int numberOfSeats;
    
    @ManyToMany
    private List<CabinConfiguration> cabinConfigurations;
 
    @ManyToOne
    @JoinColumn
    private AircraftType aircraftType;
    
    public AircraftConfiguration() {
    }

    public AircraftConfiguration(String name, int numOfCabinClass,int numberOfSeats,
            List<CabinConfiguration> cabinConfigurations, AircraftType aircraftType) {
        
        this.name = name;
        this.numOfCabinClass = numOfCabinClass;
        this.numberOfSeats = numberOfSeats;
        this.cabinConfigurations = cabinConfigurations;
        this.aircraftType = aircraftType;
    }
    
    public AircraftConfiguration(String name, int numOfCabinClass,int numberOfSeats, AircraftType aircraftType) {
        
        this.name = name;
        this.numOfCabinClass = numOfCabinClass;
        this.numberOfSeats = numberOfSeats;
        this.cabinConfigurations = new ArrayList<>();
        this.aircraftType = aircraftType;
    }
    
    public Long getAircraftConfigurationId() {
        return AircraftConfigurationId;
    }

    public void setAircraftConfigurationId(Long AircraftConfigurationId) {
        this.AircraftConfigurationId = AircraftConfigurationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfCabinClass() {
        return numOfCabinClass;
    }

    public void setNumOfCabinClass(int numOfCabinClass) {
        this.numOfCabinClass = numOfCabinClass;
    }

    public List<CabinConfiguration> getCabinConfigurations() {
        return cabinConfigurations;
    }

    public void setCabinConfigurations(List<CabinConfiguration> cabinConfigurations) {
        this.cabinConfigurations = cabinConfigurations;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public AircraftType getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(AircraftType aircraftType) {
        this.aircraftType = aircraftType;
    }
    
    @Override
    public String toString() {
        String temp = String.format("%s %s, cabin(s):%d, seats:%d\n", this.aircraftType.getName(), this.name, this.numOfCabinClass, this.numberOfSeats);
        
        return temp;
    }
    
}


    /*public String printAircraftConfiguration(String name) throws UnknownPersistenceException, NoAircraftConfigurationException {
        System.out.println("AircraftConfigurationSessionBean.printAircraftConfiguration(String name)");
        Query query = em.createQuery("SELECT e FROM AircraftConfiguration e WHERE e.name = :name");
        query.setParameter("name", name);
        
        AircraftConfiguration aircraftConfiguration = null;
        String out = ""; 
        try {
            aircraftConfiguration = (AircraftConfiguration) query.getSingleResult();
            System.out.println(aircraftConfiguration);
            out += aircraftConfiguration.print();
            out += "\ncabin(s):\n";
            for(CabinConfiguration c : aircraftConfiguration.getCabinConfigurations()) {
                out += c.toString();
                out += "\n";
            }
        } catch (NoResultException e) {
            throw new NoAircraftConfigurationException();
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
        return out;
    }*/

      /*for (Aircraft aircraft : aircraftList) {
            // Format each part with a specified width
            System.out.printf("%-30s%-20s%-10d%-10d\n",
                    aircraft.getName(),
                    aircraft.getConfiguration(),
                    aircraft.getAisleCount(),
                    aircraft.getCapacity());
        }
        
        
        String aircraftTypeString = "";
        for (AircraftConfiguration ac: acs) {
            if (!aircraftTypeString.equals(ac.getAircraftType().getName())) {
                list.add(" ");
                //list.add(ac.getAircraftType().getName());
                aircraftTypeString = ac.getAircraftType().getName();
            }
            String temp = "Name: " + ac.getName() + " (id = " + ac.getAircraftConfigurationId() + ")   seats: " + ac.getNumberOfSeats();
            System.out.println(temp);
            list.add(temp);
        }
        list.add(" ");
        return list;*/