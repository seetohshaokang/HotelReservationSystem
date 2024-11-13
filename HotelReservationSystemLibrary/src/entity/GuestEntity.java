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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author shaokangseetoh
 */
@Entity
public class GuestEntity extends VisitorEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private String password;

    public GuestEntity() {
        super();
    }

    // Default Constructor
    public GuestEntity(String name, String email, String password) {
        super(name, email);
        this.password = password;
    }

    // Alias for visitorId as guestId
    public Long getGuestId() {
        return getVisitorId();
    }

    public void setGuestId(Long guestId) {
        setVisitorId(guestId);
    }

    // Alias for name
    public String getGuestName() {
        return getName();
    }

    public void setGuestName(String name) {
        setName(name);
    }

    // Alias for email
    public String getGuestEmail() {
        return getEmail();
    }

    public void setGuestEmail(String email) {
        setEmail(email);
    }

    // Alias for reservations
    public List<ReservationEntity> getGuestReservations() {
        return getReservations();
    }

    public void setGuestReservations(List<ReservationEntity> reservations) {
        setReservations(reservations);
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "GuestEntity[ id=" + getGuestId() + ", name=" + getGuestName() + ", email=" + getGuestEmail() + " ]";
    }

}
