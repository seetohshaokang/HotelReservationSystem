/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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
    private Long reservationId;

    // *...1 r/s with Guest
    @ManyToOne
    @JoinColumn
    private GuestEntity guest;
    
    // *...1 r/s with partner
    /*
    @ManyToOne
    @JoinColumn
    private PartnerEntity partner;
    */
    
    // 1...* r/s with room
    @OneToMany(mappedBy = "reservation", cascade = {}, fetch = FetchType.LAZY)
    private List<RoomReservationEntity> roomReservations;
   
    @Column
    private LocalDate checkInDate;
    @Column
    private LocalDate checkOutDate;
    @Column
    private Double totalAmount;
    @Column
    private LocalDate reservationDate;


    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    // JPA Constructor
    public ReservationEntity() {
        this.roomReservations = new ArrayList<>();
    }

    // Constructor for guests
    public ReservationEntity(GuestEntity guest, LocalDate checkInDate, LocalDate checkOutDate, Double totalAmount, ReservationStatus status) {
        this.guest = guest;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.roomReservations = new ArrayList<>();
        this.reservationDate = LocalDate.now();
    }

    // Constructor for partners
    /*
    public ReservationEntity(PartnerEntity partner, LocalDate checkInDate, LocalDate checkOutDate, Double totalAmount, ReservationStatus status) {
        this.partner = partner;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.roomReservations = new ArrayList<>();
    }
    */
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationId fields are not set
        if (!(object instanceof ReservationEntity)) {
            return false;
        }
        ReservationEntity other = (ReservationEntity) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReservationEntity[ id=" + reservationId + " ]";
    }
    
    // Getters and Setters
    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
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

    /**
     * @return the roomReservations
     */
    public List<RoomReservationEntity> getRoomReservations() {
        return roomReservations;
    }

    /**
     * @param roomReservations the roomReservations to set
     */
    public void setRoomReservations(List<RoomReservationEntity> roomReservations) {
        this.roomReservations = roomReservations;
    }

    /**
     * @return the reservationDate
     */
    public LocalDate getReservationDate() {
        return reservationDate;
    }

    /**
     * @param reservationDate the reservationDate to set
     */
    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }
    
    
    
}
