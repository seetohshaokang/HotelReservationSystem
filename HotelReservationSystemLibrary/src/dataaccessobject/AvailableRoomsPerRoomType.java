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
    
    private Integer availableRoomsCount; // Stores the count of available rooms instead of the list
    private Integer sequence;

    // Constructor to initialize with room type name and available rooms count
    public AvailableRoomsPerRoomType(RoomTypeName roomTypeName, Integer availableRoomsCount) {
        this.roomTypeName = roomTypeName;
        this.availableRoomsCount = availableRoomsCount;
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
        if (!(object instanceof AvailableRoomsPerRoomType)) {
            return false;
        }
        AvailableRoomsPerRoomType other = (AvailableRoomsPerRoomType) object;
        return (this.roomTypeAvailabilityId != null || other.roomTypeAvailabilityId == null) && (this.roomTypeAvailabilityId == null || this.roomTypeAvailabilityId.equals(other.roomTypeAvailabilityId));
    }

    @Override
    public String toString() {
        return "entity.RoomTypeAvailability[ id=" + roomTypeAvailabilityId + " ]";
    }

    /**
     * @return the availableRoomsCount
     */
    public Integer getAvailableRoomsCount() {
        return availableRoomsCount;
    }

    /**
     * @param availableRoomsCount the availableRoomsCount to set
     */
    public void setAvailableRoomsCount(Integer availableRoomsCount) {
        this.availableRoomsCount = availableRoomsCount;
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
