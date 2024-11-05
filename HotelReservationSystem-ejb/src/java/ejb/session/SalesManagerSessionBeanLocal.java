/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session;

import entity.RoomRateEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.RoomRateNotFoundException;

/**
 *
 * @author bryan
 */
@Local
public interface SalesManagerSessionBeanLocal {
    
    public RoomRateEntity getRoomRateById(Long id) throws RoomRateNotFoundException;

    public List<RoomRateEntity> viewAllRoomRates();

    public void deleteRoomRateEntity(Long id) throws RoomRateNotFoundException;

    public RoomRateEntity updateRoomRate(Long id, String newName, Double newRatePerNight, Date newStartDate, Date newEndDate) throws RoomRateNotFoundException;

    public Long createNewRoomRate(RoomRateEntity roomRate);
}
