/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import util.enumeration.ReservationStatus;

/**
 *
 * @author shaokangseetoh
 */
@Entity
public class ReservationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // *...1 r/s with Guest
    @ManyToOne
    @JoinColumn
    private GuestEntity guest;
    
    // *...1 r/s with partner
    @ManyToOne
    @JoinColumn
    private PartnerEntity partner;
    
    // A reservation must be tied to either a guest or partner, how to resolve?
    // Unable to put optional = false for both fields as its either or
    
    // 1...* r/s with room
    @OneToMany(mappedBy = "reservation")
    private List<RoomEntity> rooms;
   
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    // JPA Constructor
    public ReservationEntity() {
    }

    // Default Constructor
    public ReservationEntity(GuestEntity guest, PartnerEntity partner, LocalDate checkInDate, LocalDate checkOutDate, Double totalAmount, ReservationStatus status) {
        this.guest = guest;
        this.partner = partner;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.rooms = new ArrayList<>();
    }
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReservationEntity)) {
            return false;
        }
        ReservationEntity other = (ReservationEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReservationEntity[ id=" + id + " ]";
    }
    
    // Getters and Setters
    public Long getReservationId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GuestEntity getGuest() {
        return guest;
    }

    public void setGuest(GuestEntity guest) {
        this.guest = guest;
    }
    
    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
    
    
    
}
