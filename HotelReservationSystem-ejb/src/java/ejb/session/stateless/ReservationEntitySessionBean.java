/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import ejb.session.ReservationEntitySessionBeanRemote;
import entity.GuestEntity;
import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomReservationEntity;
import entity.RoomTypeEntity;
import entity.VisitorEntity;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    public Long createReservationForGuest(Long guestId, LocalDate checkInDate, LocalDate checkOutDate, Double totalAmount, RoomTypeEntity roomType, Integer numberRooms) {
        // Retrieve guest from database
        GuestEntity guest = em.find(GuestEntity.class, guestId);
        if (guest == null) {
            System.out.println("Guest with ID " + guestId + " does not exist.");
            return null;
        }

        // Create new reservation entity with room type and number of rooms
        ReservationEntity newReservation = new ReservationEntity(guest, roomType, checkInDate, checkOutDate, totalAmount, numberRooms);

        // Add reservation entity to guest's list of reservations
        guest.getReservations().add(newReservation);
        em.persist(newReservation);
        em.flush();  // Persist the new reservation to the database

        return newReservation.getReservationId();
    }

    @Override
    public ReservationEntity findReservationById(Long reservationId) {
        ReservationEntity reservation = em.find(ReservationEntity.class, reservationId);
        return reservation;
    }

    public Long createReservationForVisitor(VisitorEntity visitor, LocalDate checkInDate, LocalDate checkOutDate, Double totalAmount, RoomTypeEntity roomType, Integer numberRooms) {
        // Merge the visitor entity, ensuring it is persisted in the database if not already present
        VisitorEntity managedVisitor = em.merge(visitor);
        em.flush();  // Ensures managedVisitor has a generated ID if it was newly persisted

        // Create a new reservation entity associated with the visitor
        ReservationEntity newReservation = new ReservationEntity(managedVisitor, roomType, checkInDate, checkOutDate, totalAmount, numberRooms);

        // Add the reservation entity to the visitor's list of reservations
        managedVisitor.getReservations().add(newReservation);
        em.persist(newReservation);
        em.flush();  // Persist the new reservation to the database

        return newReservation.getReservationId();
    }

    @Override
    public void confirmReservation(ReservationEntity reservation) {
        reservation.setReservationStatus(ReservationStatus.RESERVED);
        ReservationEntity managedReservation = em.merge(reservation);
        em.flush();
    }

    // For room allocation
    public List<ReservationEntity> findReservationsByCheckInDate(LocalDate checkInDate) {
        // Create a JPQL query to retrieve reservations with the specified check-in date
        Query query = em.createQuery(
                "SELECT r FROM ReservationEntity r WHERE r.checkInDate = :checkInDate", ReservationEntity.class);

        // Set the query parameter
        query.setParameter("checkInDate", checkInDate);

        // Execute the query and return the result list
        return query.getResultList();
    }

    public List<ReservationEntity> getAllReservations() {
        Query query = em.createQuery("SELECT r FROM ReservationEntity r");
        return query.getResultList();

    }

    public RoomReservationEntity addRoomReservationToReservation(RoomEntity room, ReservationEntity reservation) {
        // Create the RoomReservationEntity with the provided room and reservation
        RoomReservationEntity roomReservation = new RoomReservationEntity(room, reservation);

        // Persist the RoomReservationEntity
        em.persist(roomReservation);

        // Add to the reservation's list of room reservations
        reservation.getRoomReservations().add(roomReservation);

        // Update the reservation in persistence context
        em.merge(reservation);
        em.flush();

        return roomReservation;
    }

}
