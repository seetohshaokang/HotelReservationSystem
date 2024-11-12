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
import javax.persistence.OneToMany;
import util.enumeration.RoomTypeName;

/**
 *
 * @author shaokangseetoh
 */
@Entity
public class RoomTypeEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeId;

    @Enumerated(EnumType.STRING)
    private RoomTypeName name;
    
    //@Enumerated(EnumType.STRING)
    //private RoomTypeName nextHighestRT;

    @Column
    private String description;
    @Column
    private Double size;
    @Column
    private String bed;
    @Column
    private Integer capacity;
    @Column
    private List<String> amenities;
    @Column
    private Boolean isDisabled;

    // 1...* r/s with room
    @OneToMany(mappedBy = "roomType", cascade = {}, fetch = FetchType.LAZY)
    private List<RoomEntity> rooms;

    // 1...* r/s with roomrate
    @OneToMany(mappedBy = "roomType", cascade = {}, fetch = FetchType.LAZY)
    private List<RoomRateEntity> roomRates;

    // JPA Constructor
    public RoomTypeEntity() {
        this.rooms = new ArrayList<>();
        this.roomRates = new ArrayList<>();
    }

    public RoomTypeEntity(RoomTypeName name, String description, Double size, String bed, Integer capacity, List<String> amenities) {
        this.name = name;
        // this.nextHighestRT = nextHighest;
        this.description = description;
        this.size = size;
        this.bed = bed;
        this.capacity = capacity;
        this.amenities = amenities;
        this.isDisabled = false;
        // Relationship fields
        this.rooms = new ArrayList<>();
        this.roomRates = new ArrayList<>();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomTypeId != null ? roomTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomTypeId fields are not set
        if (!(object instanceof RoomTypeEntity)) {
            return false;
        }
        RoomTypeEntity other = (RoomTypeEntity) object;
        if ((this.roomTypeId == null && other.roomTypeId != null) || (this.roomTypeId != null && !this.roomTypeId.equals(other.roomTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomTypeEntity[ id=" + roomTypeId + " ]";
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    // Getters and Setters
    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public RoomTypeName getName() {
        return name;
    }

    public void setName(RoomTypeName name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    /**
     * @return the rooms
     */
    public List<RoomEntity> getRooms() {
        return rooms;
    }

    /**
     * @param rooms the rooms to set
     */
    public void setRooms(List<RoomEntity> rooms) {
        this.rooms = rooms;
    }

    /**
     * @return the roomRates
     */
    public List<RoomRateEntity> getRoomRates() {
        return roomRates;
    }

    /**
     * @param roomRates the roomRates to set
     */
    public void setRoomRates(List<RoomRateEntity> roomRates) {
        this.roomRates = roomRates;
    }

}
