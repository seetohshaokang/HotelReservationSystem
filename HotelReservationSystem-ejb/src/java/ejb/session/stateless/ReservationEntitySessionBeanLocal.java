/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.GuestEntity;
import entity.ReservationEntity;
import entity.VisitorEntity;
import java.time.LocalDate;
import javax.ejb.Local;

/**
 *
 * @author shaokangseetoh
 */
@Local
public interface ReservationEntitySessionBeanLocal {

    public Long createReservationForGuest(Long guestId, LocalDate checkInDate, LocalDate checkOutDate, Double totalAmount);
    
    public ReservationEntity findReservationById(Long reservationId);
    
    public Long createReservationForVisitor(VisitorEntity visitor, LocalDate checkInDate, LocalDate checkOutDate, Double totalAmount);
    
    public void confirmReservation(ReservationEntity reservation);
    
   
}
