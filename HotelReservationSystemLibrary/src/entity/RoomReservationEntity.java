/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author shaokangseetoh
 */
@Entity
public class RoomReservationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roomReservationid;
    
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity reservedRoom;
    
    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private ReservationEntity reservation;
    
    // Date attributes
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    public RoomReservationEntity() {
    }

    public RoomReservationEntity(RoomEntity reservedRoom, ReservationEntity reservation) {
        this.reservedRoom = reservedRoom;
        this.reservation = reservation;
        this.checkInDate = reservation.getCheckInDate();
        this.checkOutDate = reservation.getCheckOutDate();
    }
    public Long getRoomReservationid() {
        return roomReservationid;
    }

    public void setRoomReservationid(Long roomReservationid) {
        this.roomReservationid = roomReservationid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomReservationid != null ? roomReservationid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomReservationid fields are not set
        if (!(object instanceof RoomReservationEntity)) {
            return false;
        }
        RoomReservationEntity other = (RoomReservationEntity) object;
        if ((this.roomReservationid == null && other.roomReservationid != null) || (this.roomReservationid != null && !this.roomReservationid.equals(other.roomReservationid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomReservation[ id=" + roomReservationid + " ]";
    }

    /**
     * @return the reservedRoom
     */
    public RoomEntity getReservedRoom() {
        return reservedRoom;
    }

    /**
     * @param reservedRoom the reservedRoom to set
     */
    public void setReservedRoom(RoomEntity reservedRoom) {
        this.reservedRoom = reservedRoom;
    }

    /**
     * @return the reservation
     */
    public ReservationEntity getReservation() {
        return reservation;
    }

    /**
     * @param reservation the reservation to set
     */
    public void setReservation(ReservationEntity reservation) {
        this.reservation = reservation;
    }
    
}
