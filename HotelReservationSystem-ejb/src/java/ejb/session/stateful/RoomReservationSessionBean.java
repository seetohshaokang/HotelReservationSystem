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
import dataaccessobject.AvailableRoomsPerRoomType;
import dataaccessobject.RoomsPerRoomType;
import ejb.session.stateless.ReservationEntitySessionBeanLocal;
import entity.GuestEntity;
import entity.RoomTypeEntity;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
    private ReservationEntitySessionBeanLocal reservationEntitySessionBean;

    @EJB
    private RoomRateEntitySessionBeanLocal roomRateEntitySessionBean;

    @EJB
    private RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBean;

    @EJB
    private RoomEntitySessionBeanLocal roomEntitySessionBean;

    // Temporarily store the roomAvailability for each roomType
    private List<AvailableRoomsPerRoomType> roomTypeAvailabilityList;
    
     @Override
    public List<AvailableRoomsPerRoomType> searchAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate) {
        // Create a new list to store room availability for each room type (not cached)
        List<AvailableRoomsPerRoomType> freshRoomTypeAvailabilityList = new ArrayList<>();
        Integer sequence = 0;

        for (RoomTypeName roomTypeName : RoomTypeName.values()) {
            // Retrieve available rooms for this room type and date range directly from the database
            List<RoomEntity> availableRooms = roomEntitySessionBean.retrieveAvailableRooms(checkInDate, checkOutDate, roomTypeName);
            
            // Create an AvailableRoomsPerRoomType object for the room type with available rooms
            AvailableRoomsPerRoomType roomTypeAvailability = new AvailableRoomsPerRoomType(roomTypeName, availableRooms);
            sequence++;
            
            // Optionally, set a sequence number here if needed for sorting or printing
            roomTypeAvailability.setSequence(sequence);

            // Add the AvailableRoomsPerRoomType object to the fresh list
            freshRoomTypeAvailabilityList.add(roomTypeAvailability);
        }

        return freshRoomTypeAvailabilityList; // Return the fresh data directly without caching
    }
    
    
    public Long reserveRoomForGuest(Long guestId, LocalDate checkInDate, LocalDate checkOutDate, List<RoomsPerRoomType> roomsToReserve) {
        Double totalAmount = 0.0;
        
        // Create the reservation for the guest with the total calculated amount
        Long reservationId = reservationEntitySessionBean.createReservationForGuest(guestId, checkInDate, checkOutDate, totalAmount);

        if (reservationId != null) {
            System.out.println("Reservation created with ID: " + reservationId);
        } else {
            System.out.println("Reservation could not be created; guest does not exist.");
        }

        // Iterate over each RoomsPerRoomType DTO in the list
        for (RoomsPerRoomType rooms : roomsToReserve) { // For each room type to reserve
            RoomTypeName roomTypeName = rooms.getRoomTypeName(); // Get room type name
            int numberOfRooms = rooms.getNumRooms(); // Get required rooms

            // Directly query the database to check room availability
            List<RoomEntity> availableRooms = roomEntitySessionBean.retrieveAvailableRooms(checkInDate, checkOutDate, roomTypeName);

            // Check if enough rooms are available
            if (availableRooms.size() < numberOfRooms) {
                System.out.println("Not enough rooms available for room type: " + roomTypeName + ". Requested: " + numberOfRooms + ", Available: " + availableRooms.size());
                return null; // Optionally, handle partial reservations if needed
            }

            // Calculate the rate for the requested number of rooms for this room type
            Double roomRate = getWalkInRate(checkInDate, checkOutDate, roomTypeName);
            if (roomRate == null) {
                System.out.println("Rate not available for room type: " + roomTypeName);
                return null;
            }
            totalAmount += roomRate * numberOfRooms;
        }
        return reservationId;
    }    
    
    @Override // Computes walk in rate for 1 room for duration for that room type
    public Double getWalkInRate(LocalDate checkInDate, LocalDate checkOutDate, RoomTypeName rtName) {
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

    // Getter for the stored roomTypeAvailabilityList
    public List<AvailableRoomsPerRoomType> getRoomTypeAvailabilityList() {
        return roomTypeAvailabilityList;
    }
    
}
