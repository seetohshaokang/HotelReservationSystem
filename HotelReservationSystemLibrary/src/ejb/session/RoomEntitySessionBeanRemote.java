/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session;

import entity.RoomEntity;
import javax.ejb.Remote;
import util.enumeration.RoomStatus;
import util.enumeration.RoomTypeName;
import util.exception.ExistingRoomException;
import util.exception.NoExistingRoomException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author shaokangseetoh
 */
@Remote
public interface RoomEntitySessionBeanRemote {

    public RoomEntity createNewRoom(RoomTypeName rtName, Integer floor, Integer sequence, RoomStatus roomStatus)throws RoomTypeNotFoundException, ExistingRoomException;
    
    public RoomEntity searchRoomByRoomNumber(String roomNumber) throws NoExistingRoomException;

}
