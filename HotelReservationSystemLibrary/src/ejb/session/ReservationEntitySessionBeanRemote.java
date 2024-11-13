/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session;

import entity.GuestEntity;
import entity.ReservationEntity;
import java.time.LocalDate;
import javax.ejb.Remote;

/**
 *
 * @author shaokangseetoh
 */
@Remote
public interface ReservationEntitySessionBeanRemote {

    public Long createReservationForGuest(Long guestId, LocalDate checkInDate, LocalDate checkOutDate, Double totalAmount);

    public ReservationEntity findReservationById(Long reservationId);

    public void updateReservation(ReservationEntity reservation);
    
}
