/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataaccessobject;

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
public class RoomsPerRoomType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roomsToReserveId;
    
    @Enumerated(EnumType.STRING)
    private RoomTypeName roomTypeName;
    
    private Integer numRooms;

    
    
    public Long getRoomsToReserveId() {
        return roomsToReserveId;
    }

    public void setRoomsToReserveId(Long roomsToReserveId) {
        this.roomsToReserveId = roomsToReserveId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomsToReserveId != null ? roomsToReserveId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomsToReserveId fields are not set
        if (!(object instanceof RoomsPerRoomType)) {
            return false;
        }
        RoomsPerRoomType other = (RoomsPerRoomType) object;
        if ((this.roomsToReserveId == null && other.roomsToReserveId != null) || (this.roomsToReserveId != null && !this.roomsToReserveId.equals(other.roomsToReserveId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dataaccessobject.RoomsToReserve[ id=" + roomsToReserveId + " ]";
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
     * @return the numRooms
     */
    public Integer getNumRooms() {
        return numRooms;
    }

    /**
     * @param numRooms the numRooms to set
     */
    public void setNumRooms(Integer numRooms) {
        this.numRooms = numRooms;
    }
    
}
