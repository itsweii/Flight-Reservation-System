package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import util.enumeration.EmployeeType;

@Entity
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long EmployteeId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeType employeeType;
    
    @Column(nullable = false, length = 32)
    private String name;
    
    @Column(nullable = false, length = 32, unique = true)
    private String username;
    
    @Column(nullable = false, length = 32)
    private String password;

    public Employee() {
    }

    public Employee(EmployeeType employeeType, String name, String userName, String password) {
        this.employeeType = employeeType;
        this.name = name;
        this.username = userName;
        this.password = password;
    }
    
    public Long getEmployteeId() {
        return EmployteeId;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "entity.Employee[ id=" + EmployteeId + " ]";
    }
    
}
