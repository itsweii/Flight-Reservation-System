package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author hao
 */
@Entity
public class CabinFee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cabinFeeId;

    @ManyToOne
    @JoinColumn
    private CabinConfiguration cabinConfiguration;
    
    @Column(nullable = false)
    private int fee;

    @Column (nullable = false)
    private String name;
    
    public CabinFee() {
    }

    public CabinFee(String name, int fee) {
        this.name = name;
        this.fee = fee;
    }
    
    public Long getCabinFeeId() {
        return cabinFeeId;
    }

    public CabinConfiguration getCabinConfiguration() {
        return cabinConfiguration;
    }

    public int getFee() {
        return fee;
    }

    public String getName() {
        return name;
    }

    public void setCabinConfiguration(CabinConfiguration cabinConfiguration) {
        this.cabinConfiguration = cabinConfiguration;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "entity.CabinFee[ id=" + cabinFeeId + " ]";
    }
    
}
