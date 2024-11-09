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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
    private Long roomId;
    
    @Column(nullable = false)
    private String roomNumber;
    
    @Enumerated(EnumType.STRING)
    private RoomStatus status;
    
    // *...1 r/s with roomtype
    @ManyToOne
    @JoinColumn(name = "roomtype_id", nullable = false)
    private RoomTypeEntity roomType;
    
    @OneToMany(mappedBy = "reservedRoom", cascade = {}, fetch = FetchType.LAZY)
    private List<RoomReservationEntity> roomReservations;
    

    // JPA Constructor
    public RoomEntity() {
        this.roomReservations = new ArrayList<>();
    }

    // Default Constructor
    public RoomEntity(RoomTypeEntity roomType, String roomNumber, RoomStatus status) {
        this.roomType = roomType;
        this.roomNumber = roomNumber;
        this.status = status;
        this.roomReservations = new ArrayList<>();
        
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomId != null ? roomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomId fields are not set
        if (!(object instanceof RoomEntity)) {
            return false;
        }
        RoomEntity other = (RoomEntity) object;
        if ((this.roomId == null && other.roomId != null) || (this.roomId != null && !this.roomId.equals(other.roomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomEntity[ id=" + roomId + " ]";
    }
    
    // Getters and Setters
    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
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
    
    
    
}
