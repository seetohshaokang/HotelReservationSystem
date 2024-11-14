/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless.helper;

import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomReservationEntity;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.ReservationStatus;

/**
 *
 * @author shaokangseetoh
 */
@Local
public interface RoomCheckInOutSessionBeanLocal {

    public List<ReservationEntity> findReservedReservationsByEmail(String email);

    public void checkInRoomReservation(RoomReservationEntity roomReservation);

    public void replaceRoomReservation(RoomReservationEntity roomReservation, RoomEntity replacementRoom);

    public List<ReservationEntity> findReservationsByEmailAndStatus(String email, ReservationStatus status);
    
    public void checkOutRoomReservation(RoomReservationEntity roomReservation);
}
