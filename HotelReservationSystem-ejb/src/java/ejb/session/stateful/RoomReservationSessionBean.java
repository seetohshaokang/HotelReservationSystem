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
import ejb.session.stateless.RoomReservationEntitySessionBeanLocal;
import entity.GuestEntity;
import entity.ReservationEntity;
import entity.RoomReservationEntity;
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
    private RoomReservationEntitySessionBeanLocal roomReservationEntitySessionBean;

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

    @Override
    public Long reserveRoomForGuest(Long guestId, LocalDate checkInDate, LocalDate checkOutDate, List<RoomsPerRoomType> roomsToReserve) {
        Double totalAmount = 0.0;
        ReservationEntity reservation = null;

        // Create the reservation without room reservations initially
        Long reservationId = reservationEntitySessionBean.createReservationForGuest(guestId, checkInDate, checkOutDate, totalAmount);

        if (reservationId == null) {
            System.out.println("Reservation could not be created; guest does not exist.");
            return null;
        }

        // Fetch the created reservation entity
        reservation = reservationEntitySessionBean.findReservationById(reservationId);

        // Iterate over each RoomsPerRoomType DTO in the list
        for (RoomsPerRoomType rooms : roomsToReserve) {
            RoomTypeName roomTypeName = rooms.getRoomTypeName();
            int numberOfRooms = rooms.getNumRooms();

            // Retrieve the latest availability for the required room type
            List<RoomEntity> availableRooms = roomEntitySessionBean.retrieveAvailableRooms(checkInDate, checkOutDate, roomTypeName);

            if (availableRooms.size() < numberOfRooms) {
                System.out.println("Not enough rooms available for room type: " + roomTypeName + ". Requested: " + numberOfRooms + ", Available: " + availableRooms.size());
                return null;
            }

            // Calculate the rate for the requested number of rooms for this room type
            Double roomRate = getWalkInRate(checkInDate, checkOutDate, roomTypeName);
            if (roomRate == null) {
                System.out.println("Rate not available for room type: " + roomTypeName);
                return null;
            }
            totalAmount += roomRate * numberOfRooms;

            // Reserve the specific rooms by creating RoomReservationEntity records
            for (int i = 0; i < numberOfRooms; i++) {
                RoomEntity roomToReserve = availableRooms.get(i);

                // Create a RoomReservationEntity for each room
                RoomReservationEntity roomReservation = roomReservationEntitySessionBean.createNewRoomReservation(roomToReserve, reservation);
            }
        }
        // Update the reservation's total amount
        reservation.setTotalAmount(totalAmount);
        reservationEntitySessionBean.confirmReservation(reservation);

        System.out.println("Reservation created with ID: " + reservationId);
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
    
    @Override
    public Double getReservationRate(LocalDate checkInDate, LocalDate checkOutDate, RoomTypeName rtName) {
        double totalFee = 0.0;

        // Iterate through each date in the stay period
        for (LocalDate date = checkInDate; !date.isAfter(checkOutDate); date = date.plusDays(1)) {
            Double dailyRate = getPrevailingRateForDate(date, rtName);
            if (dailyRate != null) {
                totalFee += dailyRate;
            }
        }

        return totalFee;
    }

    // helper method for getReservationRate
    private Double getPrevailingRateForDate(LocalDate date, RoomTypeName rtName) {
        // Fetch all rates for the given room type
        List<RoomRateEntity> roomRates = roomRateEntitySessionBean.getRoomRatesByRoomType(rtName);

        Double promotionRate = null;
        Double peakRate = null;
        Double normalRate = null;

        // Loop through each rate to find the applicable ones
        for (RoomRateEntity rate : roomRates) {
            if (rate.getRateType() == RateType.PROMOTION && rate.isValidForDate(date)) {
                promotionRate = rate.getRatePerNight();
            } else if (rate.getRateType() == RateType.PEAK && rate.isValidForDate(date)) {
                peakRate = rate.getRatePerNight();
            } else if (rate.getRateType() == RateType.NORMAL) {
                normalRate = rate.getRatePerNight();
            }
        }

        // Determine the prevailing rate based on the hierarchy
        if (promotionRate != null) {
            return promotionRate;
        } else if (peakRate != null) {
            return peakRate;
        } else {
            return normalRate; // Return normal rate if no promotion or peak rate
        }
    }

    // Getter for the stored roomTypeAvailabilityList
    @Override
    public List<AvailableRoomsPerRoomType> getRoomTypeAvailabilityList() {
        return roomTypeAvailabilityList;
    }

}
