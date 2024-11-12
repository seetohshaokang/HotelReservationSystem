/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session;

import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomReservationEntity;
import java.time.LocalDate;
import javax.ejb.Remote;
import util.enumeration.RoomTypeName;

/**
 *
 * @author shaokangseetoh
 */
@Remote
public interface RoomReservationEntitySessionBeanRemote {

    public RoomReservationEntity createNewRoomReservation(RoomEntity room, ReservationEntity reservation);
    

}
