/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless.helper;

import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomReservationEntity;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.ReservationStatus;
import util.enumeration.RoomStatus;

/**
 *
 * @author shaokangseetoh
 */
@Stateless
public class RoomCheckInOutSessionBean implements RoomCheckInOutSessionBeanRemote, RoomCheckInOutSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public List<ReservationEntity> findReservedReservationsByEmail(String email) {
        Query query = em.createQuery(
                "SELECT r FROM ReservationEntity r "
                + "JOIN r.visitor v "
                + "WHERE v.email = :email "
                + "AND r.status = :status", ReservationEntity.class);

        query.setParameter("email", email);
        query.setParameter("status", ReservationStatus.RESERVED);

        return query.getResultList();
    }

    public void checkInRoomReservation(RoomReservationEntity roomReservation) {
        // Ensure the room reservation is assigned
        if (!roomReservation.getIsAssigned()) {
            throw new IllegalArgumentException("Room reservation is not assigned and cannot be checked in.");
        }

        // Retrieve the associated room and check its status
        RoomEntity room = roomReservation.getReservedRoom();
        if (room.getStatus() == RoomStatus.AVAILABLE) {
            // Set the room status to occupied and update the room entity
            room.setStatus(RoomStatus.OCCUPIED);
            em.merge(room); // Persist room status change

            // Update the status of the parent reservation to CHECKED_IN if not already done
            ReservationEntity reservation = roomReservation.getReservation();
            if (reservation.getStatus() == ReservationStatus.RESERVED) {
                reservation.setStatus(ReservationStatus.CHECKED_IN);
                em.merge(reservation); // Persist reservation status change
            }

            em.flush(); // Ensure all changes are saved to the database

            System.out.println("Room " + room.getRoomNumber() + " has been checked in for reservation ID: "
                    + reservation.getReservationId());
        } else {
            System.out.println("Room " + room.getRoomNumber() + " is not available for check-in.");
            throw new IllegalStateException("Room " + room.getRoomNumber() + " is not available for check-in.");
        }
    }

    public void replaceRoomReservation(RoomReservationEntity roomReservation, RoomEntity replacementRoom) {

        // Update the room reservation to use the replacement room
        roomReservation.setReservedRoom(replacementRoom);
        roomReservation.setIsAssigned(true);

        // Change the replacement room status to OCCUPIED
        replacementRoom.setStatus(RoomStatus.OCCUPIED);
        // Add roomreservation to replacement room
        replacementRoom.getRoomReservations().add(roomReservation);
        em.merge(replacementRoom);

        // Persist changes to the room reservation
        em.merge(roomReservation);
        em.flush();

        System.out.println("Room reservation updated. Replacement room " + replacementRoom.getRoomNumber()
                + " has been assigned to reservation ID: " + roomReservation.getReservation().getReservationId());
    }
    
    public List<ReservationEntity> findReservationsByEmailAndStatus(String email, ReservationStatus status) {
        Query query = em.createQuery(
                "SELECT r FROM ReservationEntity r "
                + "JOIN r.visitor v "
                + "WHERE v.email = :email "
                + "AND r.status = :status", ReservationEntity.class);
        
        query.setParameter("email", email);
        query.setParameter("status", status);

        return query.getResultList();
    }
    
    public void checkOutRoomReservation(RoomReservationEntity roomReservation) {
        if (!roomReservation.getIsAssigned()) {
            throw new IllegalArgumentException("Room reservation is not assigned and cannot be checked out.");
        }

        RoomEntity room = roomReservation.getReservedRoom();
        if (room.getStatus() == RoomStatus.OCCUPIED) {
            // Set room status to AVAILABLE
            room.setStatus(RoomStatus.AVAILABLE);
            em.merge(room);

            // Update reservation status to COMPLETED if this is the last room reservation
            ReservationEntity reservation = roomReservation.getReservation();
            boolean allRoomsCheckedOut = reservation.getRoomReservations().stream()
                    .allMatch(rr -> rr.getReservedRoom().getStatus() == RoomStatus.AVAILABLE);
            
            if (allRoomsCheckedOut) {
                reservation.setStatus(ReservationStatus.CHECKED_OUT);
                em.merge(reservation);
            }

            em.flush();

            System.out.println("Room " + room.getRoomNumber() + " has been checked out for reservation ID: "
                    + reservation.getReservationId());
        } else {
            System.out.println("Room " + room.getRoomNumber() + " is not occupied and cannot be checked out.");
            throw new IllegalStateException("Room " + room.getRoomNumber() + " is not occupied and cannot be checked out.");
        }
    }

}
