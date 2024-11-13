/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateful;

import entity.RoomEntity;
import dataaccessobject.AvailableRoomsPerRoomType;
import dataaccessobject.RoomsPerRoomType;
import entity.RoomTypeEntity;
import entity.VisitorEntity;
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

    public Double getWalkInRate(LocalDate checkInDate, LocalDate checkoutDate, RoomTypeName rtName);
    
    public Double getReservationRate(LocalDate checkInDate, LocalDate checkOutDate, RoomTypeName rtName);

    public List<AvailableRoomsPerRoomType> getRoomTypeAvailabilityList();

    public Long reserveRoomForVisitor(VisitorEntity visitor, LocalDate checkInDate, LocalDate checkOutDate, List<RoomsPerRoomType> roomsToReserve);
    
    public Long reserveRoomForGuest(Long guestId, LocalDate checkInDate, LocalDate checkOutDate, List<RoomsPerRoomType> roomsToReserve);


}
