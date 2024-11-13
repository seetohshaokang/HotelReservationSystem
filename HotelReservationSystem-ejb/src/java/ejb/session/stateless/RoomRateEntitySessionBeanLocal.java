/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomRateEntity;
import java.time.LocalDate;
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

    public RoomRateEntity getRoomRateById(Long id) throws RoomRateNotFoundException;

    public List<RoomRateEntity> viewAllRoomRates();

    public String createNewRoomRate(String name, Long roomTypeId, RateType selectedRateType, Double ratePerNight, LocalDate start, LocalDate end) throws RoomTypeNotFoundException;

    public RoomRateEntity updateRoomRate(Long id, String name, Long roomTypeId, RateType selectedRateType, Double ratePerNight, LocalDate start, LocalDate end) throws RoomTypeNotFoundException, RoomRateNotFoundException;
    
    public RoomRateEntity getRoomRateByRateAndRoomType(RoomTypeName roomTypeName, RateType rateType) throws RoomRateNotFoundException;

    public List<RoomRateEntity> getRoomRatesByRoomType(RoomTypeName roomTypeName);
    
}
