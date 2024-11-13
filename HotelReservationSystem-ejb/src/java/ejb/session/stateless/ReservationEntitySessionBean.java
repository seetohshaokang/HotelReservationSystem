/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import ejb.session.ReservationEntitySessionBeanRemote;
import entity.GuestEntity;
import entity.ReservationEntity;
import entity.VisitorEntity;
import java.time.LocalDate;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.ReservationStatus;

/**
 *
 * @author shaokangseetoh
 */
@Stateless
public class ReservationEntitySessionBean implements ReservationEntitySessionBeanRemote, ReservationEntitySessionBeanLocal {

    @EJB
    private GuestEntitySessionBeanLocal guestEntitySessionBean;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    // Method to create a new newReservation for a guest, verifying the guest exists
    public Long createReservationForGuest(Long guestId, LocalDate checkInDate, LocalDate checkOutDate, Double totalAmount) {
        // Retrieve guest from database
        GuestEntity guest = em.find(GuestEntity.class, guestId);
        if (guest == null) {
            System.out.println("Guest with ID " + guestId + " does not exist.");
            return null;
        }
        // Create new newReservation entity
        ReservationEntity newReservation = new ReservationEntity(guest, checkInDate, checkOutDate, totalAmount, ReservationStatus.PENDING);
        // Add reservation entity to guest
        guest.getReservations().add(newReservation);
        em.persist(newReservation);
        em.flush();
        return newReservation.getReservationId();
    }

    @Override
    public ReservationEntity findReservationById(Long reservationId) {
        ReservationEntity reservation = em.find(ReservationEntity.class, reservationId);
        return reservation;
    }

    public Long createReservationForVisitor(VisitorEntity visitor, LocalDate checkInDate, LocalDate checkOutDate, Double totalAmount) {
        // Merge the visitor entity, ensuring it is persisted in the database if not already present
        VisitorEntity managedVisitor = em.merge(visitor);
        em.flush();  // Ensures managedVisitor has a generated ID if it was newly persisted

        // Create a new reservation entity associated with the visitor
        ReservationEntity newReservation = new ReservationEntity(managedVisitor, checkInDate, checkOutDate, totalAmount, ReservationStatus.PENDING);

        // Add the reservation entity to the visitor's list of reservations
        managedVisitor.getReservations().add(newReservation);
        em.persist(newReservation);
        em.flush();  // Persist the new reservation to the database

        return newReservation.getReservationId();
    }

    @Override
    public void confirmReservation(ReservationEntity reservation) {
        reservation.setStatus(ReservationStatus.RESERVED);
        ReservationEntity managedReservation = em.merge(reservation);
        em.flush();
    }

}
