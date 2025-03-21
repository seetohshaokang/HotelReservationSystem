/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session;

import entity.RoomRateEntity;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;
import util.enumeration.RateType;
import util.enumeration.RoomTypeName;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author shaokangseetoh
 */
@Remote
public interface RoomRateEntitySessionBeanRemote {

    public RoomRateEntity getRoomRateById(Long id) throws RoomRateNotFoundException;

    public List<RoomRateEntity> viewAllRoomRates();

    public String createNewRoomRate(String name, Long roomTypeId, RateType selectedRateType, Double ratePerNight, LocalDate start, LocalDate end) throws RoomTypeNotFoundException;

    public RoomRateEntity updateRoomRate(Long id, String name, Long roomTypeId, RateType selectedRateType, Double ratePerNight, LocalDate start, LocalDate end) throws RoomTypeNotFoundException, RoomRateNotFoundException;

    public RoomRateEntity getRoomRateByRateAndRoomType(RoomTypeName roomTypeName, RateType rateType) throws RoomRateNotFoundException;

    public List<RoomRateEntity> getRoomRatesByRoomType(RoomTypeName roomTypeName);

    public boolean isRoomRateInUse(Long roomRateId);

    public void disableRoomRate(Long roomRateId) throws RoomRateNotFoundException;

    public void deleteRoomRate(Long roomRateId) throws RoomRateNotFoundException;

}
