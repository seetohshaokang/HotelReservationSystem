/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateful;

import entity.RoomEntity;
import dataaccessobject.AvailableRoomsPerRoomType;
import dataaccessobject.RoomsPerRoomType;
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

    public List<AvailableRoomsPerRoomType> searchAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate);
    
    public Long reserveRoomForGuest(Long guestId, LocalDate checkInDate, LocalDate checkOutDate, List<RoomsPerRoomType> roomsToReserve);

    public Double getWalkInRate(LocalDate checkInDate, LocalDate checkoutDate, RoomTypeName rtName);
}
