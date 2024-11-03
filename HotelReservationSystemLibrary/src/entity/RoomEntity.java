/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import util.enumeration.RoomStatus;

/**
 *
 * @author shaokangseetoh
 */
@Entity
public class RoomEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String roomNumber;
    
    @Enumerated(EnumType.STRING)
    private RoomStatus status;
    
    // *...1 r/s with roomtype
    @ManyToOne
    @JoinColumn(name = "roomtype_id")
    private RoomTypeEntity roomType;
    
    // *...1 r/s with reservation
    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private ReservationEntity reservation;

    // JPA Constructor
    public RoomEntity() {
    }

    // Default Constructor
    public RoomEntity(RoomTypeEntity roomType, String roomNumber, RoomStatus status) {
        this.roomType = roomType;
        this.roomNumber = roomNumber;
        this.status = status;
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
        if (!(object instanceof RoomEntity)) {
            return false;
        }
        RoomEntity other = (RoomEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomEntity[ id=" + id + " ]";
    }
    
    // Getters and Setters
    public Long getRoomId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoomTypeEntity getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypeEntity roomType) {
        this.roomType = roomType;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }
    
    
    
}
