/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateful;

import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;
import util.enumeration.RoomTypeName;

/**
 *
 * @author shaokangseetoh
 */
@Remote
public interface RoomReservationSessionBeanRemote {

    public List<RoomEntity> searchAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, RoomTypeName rtName);
    
    public Double getWalkInRate(LocalDate checkInDate, LocalDate checkoutDate, RoomTypeName rtName);
}
