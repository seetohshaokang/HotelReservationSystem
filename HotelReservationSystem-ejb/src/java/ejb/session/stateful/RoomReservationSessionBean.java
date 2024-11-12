/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatefulEjbClass.java to edit this template
 */
package ejb.session.stateful;

import ejb.session.RoomEntitySessionBeanLocal;
import ejb.session.RoomTypeEntitySessionBeanLocal;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import util.enumeration.RoomTypeName;

/**
 *
 * @author shaokangseetoh
 */
@Stateful
public class RoomReservationSessionBean implements RoomReservationSessionBeanRemote, RoomReservationSessionBeanLocal {

    @EJB
    private RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBean;

    @EJB
    private RoomEntitySessionBeanLocal roomEntitySessionBean;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    // Insert payment logic later
    @Override
    public List<RoomEntity> searchAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, RoomTypeName rtName) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        List<RoomEntity> availableRooms = roomEntitySessionBean.retrieveAvailableRooms(checkInDate, checkOutDate, rtName);
        return availableRooms;
    }
}
