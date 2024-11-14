/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import util.enumeration.RateType;

/**
 *
 * @author shaokangseetoh
 */
@Entity
public class RoomRateEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomRateId;

    @Column(length = 32, nullable = false)
    private String name;
    
    @Enumerated(EnumType.STRING)
    private RateType rateType;
    
    @Column(nullable = false)
    private Double ratePerNight;
    
    private LocalDate startDate; // Only for peak and promotion rates
    
    private LocalDate endDate; // Only for peak and promotion rates
    
    @Column(nullable = false)
    private Boolean isDisabled = false;

    // *...1 r/s with roomtype
    @ManyToOne
    @JoinColumn(name = "roomtype_id")
    private RoomTypeEntity roomType;
    
    // JPA Constructor
    public RoomRateEntity() {
    }

    // Default Constructor
    public RoomRateEntity(String name, RateType rateType) {
        this.name = name;
        this.rateType = rateType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomRateId != null ? roomRateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomRateId fields are not set
        if (!(object instanceof RoomRateEntity)) {
            return false;
        }
        RoomRateEntity other = (RoomRateEntity) object;
        if ((this.roomRateId == null && other.roomRateId != null) || (this.roomRateId != null && !this.roomRateId.equals(other.roomRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomRateEntity[ id=" + roomRateId + " ]";
    }
    
    // Getters and Setters
    public Long getRoomRateId() {
        return roomRateId;
    }

    public void setRoomRateId(Long roomRateId) {
        this.roomRateId = roomRateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RateType getRateType() {
        return rateType;
    }

    public void setRateType(RateType rateType) {
        this.rateType = rateType;
    }

    /**
     * @return the ratePerNight
     */
    public Double getRatePerNight() {
        return ratePerNight;
    }

    /**
     * @param ratePerNight the ratePerNight to set
     */
    public void setRatePerNight(Double ratePerNight) {
        this.ratePerNight = ratePerNight;
    }

    /**
     * @return the startDate
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the roomType
     */
    public RoomTypeEntity getRoomType() {
        return roomType;
    }

    /**
     * @param roomType the roomType to set
     */
    public void setRoomType(RoomTypeEntity roomType) {
        this.roomType = roomType;
    }
    
    // Method to check if a rate is valid for a specific date
    public boolean isValidForDate(LocalDate date) {
        // Check if the rate has a validity period
        if (rateType == RateType.PEAK || rateType == RateType.PROMOTION) {
            // Valid if date is within the start and end date range
            return (startDate != null && endDate != null &&
                    (date.isEqual(startDate) || date.isAfter(startDate)) &&
                    (date.isEqual(endDate) || date.isBefore(endDate)));
        }
        // Normal and Published rates are always valid (assuming no date restrictions)
        return true;
    }

    /**
     * @return the isDisabled
     */
    public Boolean getIsDisabled() {
        return isDisabled;
    }

    /**
     * @param isDisabled the isDisabled to set
     */
    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }
}
