/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session;

import entity.RoomRateEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.RateType;
import util.enumeration.RoomTypeName;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author shaokangseetoh
 */
@Local
public interface RoomRateEntitySessionBeanLocal {

    public String createNewRoomRate(String name, RoomTypeName selectedRoomType, RateType selectedRateType, Double ratePerNight, Date start, Date end) throws RoomTypeNotFoundException;

    public RoomRateEntity getRoomRateById(Long id) throws RoomRateNotFoundException;

    public List<RoomRateEntity> viewAllRoomRates();
    
}
