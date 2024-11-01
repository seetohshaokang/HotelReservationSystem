/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private RoomTypeName name;
    
    private String description;
    private Double size;
    private String bed;
    private Integer capacity;
    private List<String> amenities;

    // 1...* r/s with room
    @OneToMany (mappedBy = "roomType")
    private List<RoomEntity> rooms;
    
    // 1...* r/s with roomrate
    @OneToMany (mappedBy = "roomType")
    private List<RoomRateEntity> roomRates;
    
    
    // JPA Constructor
    public RoomTypeEntity() {
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
        if (!(object instanceof RoomTypeEntity)) {
            return false;
        }
        RoomTypeEntity other = (RoomTypeEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomTypeEntity[ id=" + id + " ]";
    }
    
    public Long getId() {
        return id;
    }

    // Getters and Setters
    public void setId(Long id) {
        this.id = id;
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
    
    
    
}
