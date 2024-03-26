/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author mw
 */
@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    
    @Column(nullable = false, length = 32)
    private String firstName;
    
    @Column(nullable = false, length = 32)
    private String lastName;
    
    @Column(nullable = false, length = 32)
    private String email;
    
    @Column(nullable = false, length = 32)
    private String address;
    
    @Column(nullable = false, length = 32, unique = true)
    private String userName;
    
    @Column(nullable = false, length = 32)
    private String password;
    
    @OneToMany
    private List<ReservedSeat> reservedSeat;
    
   
    public Customer () {
        
    }

    public Customer(String firstName, String lastName, String email, String address, String userName, String password,
    List<ReservedSeat> reservedSeat) {
        
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.userName = userName;
        this.password = password;
        this.reservedSeat = new ArrayList<ReservedSeat> ();
    }
    
    
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ReservedSeat> getReservedSeat() {
        return reservedSeat;
    }

    public void setReservedSeat(List<ReservedSeat> reservedSeat) {
        this.reservedSeat = reservedSeat;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerId fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Customer[ id=" + customerId + userName +" ]";
    }
    
}
