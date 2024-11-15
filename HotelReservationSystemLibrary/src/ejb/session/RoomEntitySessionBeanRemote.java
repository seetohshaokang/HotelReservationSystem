/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session;

import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;
import util.enumeration.RoomStatus;
import util.enumeration.RoomTypeName;
import util.exception.DisabledException;
import util.exception.ExistingRoomException;
import util.exception.NoExistingRoomException;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author shaokangseetoh
 */
@Remote
public interface RoomEntitySessionBeanRemote {

 public RoomEntity searchRoomByRoomNumber(String roomNumber) throws NoExistingRoomException;

    public RoomEntity updateRoom(Long roomId, Long roomTypeId, String newRoomNumber, RoomStatus newStatus) throws RoomNotFoundException, RoomTypeNotFoundException;

    // Insert delete room here
    public List<RoomEntity> viewAllRooms();

    public boolean isRoomInUse(Long roomId) throws RoomNotFoundException;

    public void disableRoom(Long roomId) throws RoomNotFoundException;

    public void deleteRoom(Long roomId) throws RoomNotFoundException;
    
    public List<ReservationEntity> retrieveOverlappingReservations(LocalDate checkInDate, LocalDate checkOutDate, RoomTypeName roomTypeName);
    
    public List<RoomEntity> retrieveAvailableRooms(RoomTypeName roomTypeName);

    public RoomEntity createNewRoom(Long roomtypeid, Integer floor, Integer sequence, RoomStatus roomStatus) throws RoomTypeNotFoundException, ExistingRoomException, DisabledException;

}
