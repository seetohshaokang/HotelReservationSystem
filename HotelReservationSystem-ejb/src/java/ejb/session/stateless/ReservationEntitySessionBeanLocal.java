/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.GuestEntity;
import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomReservationEntity;
import entity.RoomTypeEntity;
import entity.VisitorEntity;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Local;
import util.exception.ReservationNotFoundException;
import util.exception.VisitorNotFoundException;

/**
 *
 * @author shaokangseetoh
 */
@Local
public interface ReservationEntitySessionBeanLocal {

    public Long createReservationForGuest(Long guestId, LocalDate checkInDate, LocalDate checkOutDate, Double totalAmount, RoomTypeEntity roomType, Integer numberRooms);

    public ReservationEntity findReservationById(Long reservationId);

    public Long createReservationForVisitor(VisitorEntity visitor, LocalDate checkInDate, LocalDate checkOutDate, Double totalAmount, RoomTypeEntity roomType, Integer numberRooms);

    public void confirmReservation(ReservationEntity reservation);

    public List<ReservationEntity> findReservationsByCheckInDate(LocalDate checkInDate);

    public List<ReservationEntity> getAllReservations();

    public RoomReservationEntity addRoomReservationToReservation(RoomEntity room, ReservationEntity reservation);

    public boolean hasRoomAllocations(Long reservationId) throws ReservationNotFoundException;

    public VisitorEntity getVisitorByEmail(String email) throws VisitorNotFoundException;

}
