/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomReservationEntity;
import javax.ejb.Local;

/**
 *
 * @author shaokangseetoh
 */
@Local
public interface RoomReservationEntitySessionBeanLocal {

    public RoomReservationEntity createNewRoomReservation(RoomEntity room, ReservationEntity reservation);
    
}
