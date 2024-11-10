/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session;

import entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.Remote;
import util.enumeration.RoomTypeName;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author shaokangseetoh
 */
@Remote
public interface RoomTypeEntitySessionBeanRemote {
    // Use case 7
    public String createNewRoomType(RoomTypeEntity newRoomType);
    
    // Use case 8
    public RoomTypeEntity getRoomTypeByName(RoomTypeName name) throws RoomTypeNotFoundException;
    
    // Use case 11
    public List<RoomTypeEntity> viewAllRoomTypes();
}
