package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Partner implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PartnerId;
    
    @Column(nullable = false, length = 32)
    private String name;
    
    @Column(nullable = false, length = 32, unique = true)
    private String username;
    
    @Column(nullable = false, length = 32)
    private String password;

    public Partner() {
    }

    public Partner(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }
    
    public Long getPartnerId() {
        return PartnerId;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "entity.Partner[ id=" + PartnerId + " ]";
    }
    
}
