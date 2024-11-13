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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

/**
 *
 * @author shaokangseetoh
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class VisitorEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long visitorId;
    
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    // 1...* relationship with ReservationEntity
    @OneToMany(mappedBy = "visitor", cascade = {}, fetch = FetchType.LAZY)
    private List<ReservationEntity> reservations;

    public VisitorEntity() {
        this.reservations = new ArrayList<>();
    }

    public VisitorEntity(String name, String email) {
        this.name = name;
        this.email = email;
        this.reservations= new ArrayList<>();
    }

    public Long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(Long visitorId) {
        this.visitorId = visitorId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (visitorId != null ? visitorId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the visitorId fields are not set
        if (!(object instanceof VisitorEntity)) {
            return false;
        }
        VisitorEntity other = (VisitorEntity) object;
        if ((this.visitorId == null && other.visitorId != null) || (this.visitorId != null && !this.visitorId.equals(other.visitorId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.VisitorEntity[ id=" + visitorId + " ]";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the reservations
     */
    public List<ReservationEntity> getReservations() {
        return reservations;
    }

    /**
     * @param reservations the reservations to set
     */
    public void setReservations(List<ReservationEntity> reservations) {
        this.reservations = reservations;
    }
    
}
