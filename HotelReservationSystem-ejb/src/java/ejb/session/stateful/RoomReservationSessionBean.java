/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatefulEjbClass.java to edit this template
 */
package ejb.session.stateful;

import ejb.session.stateless.RoomEntitySessionBeanLocal;
import ejb.session.stateless.RoomRateEntitySessionBeanLocal;
import ejb.session.stateless.RoomTypeEntitySessionBeanLocal;
import entity.RoomEntity;
import entity.RoomRateEntity;
import entity.RoomTypeEntity;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.Query;
import util.enumeration.RateType;
import util.enumeration.RoomTypeName;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author shaokangseetoh
 */
@Stateful
public class RoomReservationSessionBean implements RoomReservationSessionBeanRemote, RoomReservationSessionBeanLocal {

    @EJB
    private RoomRateEntitySessionBeanLocal roomRateEntitySessionBean;

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

    @Override
    public Double getWalkInRate(LocalDate checkInDate, LocalDate checkoutDate, RoomTypeName rtName) {
        // Calculate the number of days stayed

        long numberOfDays = checkInDate.until(checkOutDate, ChronoUnit.DAYS);

        try {
            // Retrieve the published rate for the specified room type
            RoomRateEntity publishedRate = roomRateEntitySessionBean.getRoomRateByRateAndRoomType(rtName, RateType.PUBLISHED);

            // Calculate the total cost by multiplying the rate per night by the number of days
            Double totalCost = publishedRate.getRatePerNight() * numberOfDays;

            return totalCost;

        } catch (RoomRateNotFoundException ex) {
            System.out.println("Published rate not found for room type: " + rtName);
            return null;
        }

    }

}
