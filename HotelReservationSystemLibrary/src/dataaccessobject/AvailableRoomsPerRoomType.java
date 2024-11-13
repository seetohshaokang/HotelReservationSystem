/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataaccessobject;

import entity.RoomEntity;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import util.enumeration.RoomTypeName;

/**
 *
 * @author shaokangseetoh
 */
public class AvailableRoomsPerRoomType implements Serializable { // Container class

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeAvailabilityId;
    
    @Enumerated(EnumType.STRING)
    private RoomTypeName roomTypeName;
    private List<RoomEntity> availableRooms;
    private Integer sequence;

    public AvailableRoomsPerRoomType(RoomTypeName roomTypeName, List<RoomEntity> availableRooms) {
        this.roomTypeName = roomTypeName;
        this.availableRooms = availableRooms;
    }
    
    public Long getRoomTypeAvailabilityId() {
        return roomTypeAvailabilityId;
    }

    public void setRoomTypeAvailabilityId(Long roomTypeAvailabilityId) {
        this.roomTypeAvailabilityId = roomTypeAvailabilityId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomTypeAvailabilityId != null ? roomTypeAvailabilityId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomTypeAvailabilityId fields are not set
        if (!(object instanceof AvailableRoomsPerRoomType)) {
            return false;
        }
        AvailableRoomsPerRoomType other = (AvailableRoomsPerRoomType) object;
        if ((this.roomTypeAvailabilityId == null && other.roomTypeAvailabilityId != null) || (this.roomTypeAvailabilityId != null && !this.roomTypeAvailabilityId.equals(other.roomTypeAvailabilityId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomTypeAvailability[ id=" + roomTypeAvailabilityId + " ]";
    }

    /**
     * @return the availableRooms
     */
    public List<RoomEntity> getAvailableRooms() {
        return availableRooms;
    }

    /**
     * @param availableRooms the availableRooms to set
     */
    public void setAvailableRooms(List<RoomEntity> availableRooms) {
        this.availableRooms = availableRooms;
    }

    /**
     * @return the roomTypeName
     */
    public RoomTypeName getRoomTypeName() {
        return roomTypeName;
    }

    /**
     * @param roomTypeName the roomTypeName to set
     */
    public void setRoomTypeName(RoomTypeName roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    /**
     * @return the sequence
     */
    public Integer getSequence() {
        return sequence;
    }

    /**
     * @param sequence the sequence to set
     */
    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
    
}
