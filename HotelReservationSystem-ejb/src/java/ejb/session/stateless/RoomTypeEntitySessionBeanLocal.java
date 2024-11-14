/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.RoomTypeName;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author shaokangseetoh
 */
@Local
public interface RoomTypeEntitySessionBeanLocal {

    // Use case 7
    public String createNewRoomType(RoomTypeEntity newRoomType);

    // Use case 8
    public RoomTypeEntity getRoomTypeByName(RoomTypeName name) throws RoomTypeNotFoundException;

    // Use case 9
    public RoomTypeEntity updateRoomType(Long roomTypeId, String newDescription, Double newSize, String newBed, Integer newCapacity, List<String> newAmenities,
            RoomTypeName newNextHigherRoomTypeName) 
            throws RoomTypeNotFoundException;
   

    // Use case 11  
    public List<RoomTypeEntity> viewAllRoomTypes();
    
    public boolean isRoomTypeInUse(Long roomTypeId);
    
    public void deleteRoomType(Long roomTypeId) throws RoomTypeNotFoundException;
    
    public Integer getRoomTypeCount(RoomTypeName roomTypeName)throws RoomTypeNotFoundException;

}
