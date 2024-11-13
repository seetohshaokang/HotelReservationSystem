/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateful;

import entity.ReservationEntity;
import entity.RoomEntity;
import dataaccessobject.AvailableRoomsPerRoomType;
import dataaccessobject.RoomsPerRoomType;
import entity.RoomTypeEntity;
import entity.VisitorEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import util.enumeration.RoomTypeName;

/**
 *
 * @author shaokangseetoh
 */
@Local
public interface RoomReservationSessionBeanLocal {

    public List<AvailableRoomsPerRoomType> searchAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate);

    public Double getWalkInRate(LocalDate checkInDate, LocalDate checkoutDate, RoomTypeName rtName);

    public Long reserveRoomForVisitor(VisitorEntity visitor, LocalDate checkInDate, LocalDate checkOutDate, List<RoomsPerRoomType> roomsToReserve);

    public Long reserveRoomForGuest(Long guestId, LocalDate checkInDate, LocalDate checkOutDate, List<RoomsPerRoomType> roomsToReserve);
}
