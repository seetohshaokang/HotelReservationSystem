/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import ejb.session.RoomReservationEntitySessionBeanRemote;
import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomReservationEntity;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author shaokangseetoh
 */
@Stateless
public class RoomReservationEntitySessionBean implements RoomReservationEntitySessionBeanRemote, RoomReservationEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public RoomReservationEntity createNewRoomReservation(RoomEntity room, ReservationEntity reservation) {
        // Get primary key of existing room
        Long roomId = room.getRoomId();
        // Persist unmanaged reservation
        em.persist(reservation);
        // Retriev extising room from database
        RoomEntity retrievedRoom = em.find(RoomEntity.class, roomId);
        // Initalise new RR record
        RoomReservationEntity newRR = new RoomReservationEntity(retrievedRoom, reservation);
        // Add RR to Room Record
        retrievedRoom.getRoomReservations().add(newRR);
        // Add RR to Reservation
        reservation.getRoomReservations().add(newRR);
        em.persist(newRR);
        em.flush();
        return newRR;
    }

    public List<RoomReservationEntity> findRoomReservationsByDate(LocalDate date) {
        Query query = em.createQuery(
                "SELECT rr FROM RoomReservationEntity rr "
                + "WHERE rr.checkInDate = :date");
        query.setParameter("date", date);
        return query.getResultList();
    }

}
