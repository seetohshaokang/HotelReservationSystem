/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatefulEjbClass.java to edit this template
 */
package ejb.session.stateless.helper;

import ejb.session.stateless.RoomEntitySessionBeanLocal;
import ejb.session.stateless.RoomRateEntitySessionBeanLocal;
import ejb.session.stateless.RoomTypeEntitySessionBeanLocal;
import entity.RoomEntity;
import entity.RoomRateEntity;
import dataaccessobject.AvailableRoomsPerRoomType;
import dataaccessobject.RoomsPerRoomType;
import ejb.session.singleton.RoomAllocationSessionBean;
import ejb.session.stateless.ReservationEntitySessionBeanLocal;
import ejb.session.stateless.RoomReservationEntitySessionBeanLocal;
import entity.GuestEntity;
import entity.ReservationEntity;
import entity.RoomReservationEntity;
import entity.RoomTypeEntity;
import entity.VisitorEntity;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RateType;
import util.enumeration.ReservationStatus;
import util.enumeration.RoomStatus;
import util.enumeration.RoomTypeName;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author shaokangseetoh
 */
@Stateless
public class RoomReservationSessionBean implements RoomReservationSessionBeanRemote, RoomReservationSessionBeanLocal {

    @EJB
    private RoomAllocationSessionBean roomAllocationSessionBean;

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
    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    // Temporarily store the roomAvailability for each roomType
    private List<AvailableRoomsPerRoomType> roomTypeAvailabilityList;

    @Override
    public List<AvailableRoomsPerRoomType> searchAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate) {
        // Create a new list to store room availability for each room type (not cached)
        List<AvailableRoomsPerRoomType> freshRoomTypeAvailabilityList = new ArrayList<>();
        Integer sequence = 0;

        for (RoomTypeName roomTypeName : RoomTypeName.values()) {
            // Retrieve total rooms for the room type
            int totalRoomsForType = 0;
            try {
                totalRoomsForType = roomTypeEntitySessionBean.getRoomTypeCount(roomTypeName);
            } catch (RoomTypeNotFoundException e) {
                System.out.println("Room Type Not Found"); // remember to propagate later
            }

            // Retrieve reservations that overlap with the provided date range and room type
            List<ReservationEntity> overlappingReservations = roomEntitySessionBean.retrieveOverlappingReservations(checkInDate, checkOutDate, roomTypeName);

            // Calculate the number of occupied rooms by summing up the room count for each overlapping reservation
            int occupiedRoomsCount = 0;
            for (ReservationEntity reservation : overlappingReservations) {
                occupiedRoomsCount += reservation.getNumberRooms(); // Assuming getNumberRooms() returns the count of rooms for the reservation
            }

            // Calculate available rooms by subtracting occupied rooms from the total rooms for that type
            int availableRoomsCount = totalRoomsForType - occupiedRoomsCount;

            // Create an AvailableRoomsPerRoomType object for the room type with the available rooms count
            AvailableRoomsPerRoomType roomTypeAvailability = new AvailableRoomsPerRoomType(roomTypeName, availableRoomsCount);
            sequence++;

            // Optionally, set a sequence number here if needed for sorting or printing
            roomTypeAvailability.setSequence(sequence);

            // Add the AvailableRoomsPerRoomType object to the fresh list
            freshRoomTypeAvailabilityList.add(roomTypeAvailability);
        }

        return freshRoomTypeAvailabilityList; // Return the fresh data directly without caching
    }

    public Long reserveRoomForGuest(Long guestId, LocalDate checkInDate, LocalDate checkOutDate, RoomsPerRoomType rooms) {
        Double totalAmount = 0.0;
        ReservationEntity reservation = null;
        Long reservationId = null;

        // Retrieve the guest entity
        GuestEntity guest = em.find(GuestEntity.class, guestId);
        if (guest == null) {
            System.out.println("Guest with ID " + guestId + " does not exist.");
            return null;
        }

        // Extract the room type name and requested number of rooms from the RoomsPerRoomType object
        RoomTypeName roomTypeName = rooms.getRoomTypeName();
        int numberOfRooms = rooms.getNumRooms();

        // Retrieve the RoomTypeEntity for the specified room type name
        RoomTypeEntity roomType = null;
        try {
            roomType = roomTypeEntitySessionBean.getRoomTypeByName(roomTypeName);
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Room Type Not Found");
            return null;
        }

        // Retrieve overlapping reservations for the given date range and room type
        List<ReservationEntity> overlappingReservations = roomEntitySessionBean.retrieveOverlappingReservations(checkInDate, checkOutDate, roomTypeName);

        // Calculate the total number of rooms occupied by summing up the number of rooms in each overlapping reservation
        int occupiedRoomsCount = 0;
        for (ReservationEntity overlappingReservation : overlappingReservations) {
            occupiedRoomsCount += overlappingReservation.getNumberRooms();
        }

        // Calculate available rooms by subtracting the occupied rooms from the total rooms for that type
        int totalRoomsForType;
        try {
            totalRoomsForType = roomTypeEntitySessionBean.getRoomTypeCount(roomTypeName);
        } catch (RoomTypeNotFoundException e) {
            System.out.println("Room Type Not Found");
            return null;
        }
        int availableRoomsCount = totalRoomsForType - occupiedRoomsCount;

        if (availableRoomsCount < numberOfRooms) {
            System.out.println("Not enough rooms available for room type: " + roomTypeName + ". Requested: " + numberOfRooms + ", Available: " + availableRoomsCount);
            return null;
        }

        // Calculate the rate for the requested number of rooms for this room type
        Double roomRate = getWalkInRate(checkInDate, checkOutDate, roomTypeName);
        if (roomRate == null) {
            System.out.println("Rate not available for room type: " + roomTypeName);
            return null;
        }

        // Calculate total amount for this room type and create the reservation
        totalAmount = roomRate * numberOfRooms;
        reservationId = reservationEntitySessionBean.createReservationForGuest(guestId, checkInDate, checkOutDate, totalAmount, roomType, numberOfRooms);

        if (reservationId == null) {
            System.out.println("Reservation could not be created for guest.");
            return null;
        }

        // Fetch the created reservation entity for further processing
        reservation = reservationEntitySessionBean.findReservationById(reservationId);

        // Update the reservation's total amount
        reservation.setTotalAmount(totalAmount);
        reservationEntitySessionBean.confirmReservation(reservation);

        System.out.println("Reservation created with ID: " + reservationId);

        // Check if reservation's check-in date is today and time is after 2 a.m.
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        if (checkInDate.equals(today) && currentTime.isAfter(LocalTime.of(2, 0))) {
            // Process reservation immediately
            roomAllocationSessionBean.processReservation(reservation);
        }

        return reservationId;
    }

    public Long reserveRoomForVisitor(VisitorEntity visitor, LocalDate checkInDate, LocalDate checkOutDate, RoomsPerRoomType rooms) {
        Double totalAmount = 0.0;
        ReservationEntity reservation = null;
        Long reservationId = null;

        // Extract the room type name and requested number of rooms from the RoomsPerRoomType object
        RoomTypeName roomTypeName = rooms.getRoomTypeName();
        int numberOfRooms = rooms.getNumRooms();

        // Retrieve the RoomTypeEntity for the specified room type name
        RoomTypeEntity roomType = null;
        try {
            roomType = roomTypeEntitySessionBean.getRoomTypeByName(roomTypeName);
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Room Type Not Found"); // Remember to propagate exception
            return null;
        }

        // Retrieve overlapping reservations for the given date range and room type
        List<ReservationEntity> overlappingReservations = roomEntitySessionBean.retrieveOverlappingReservations(checkInDate, checkOutDate, roomTypeName);

        // Calculate the total number of rooms occupied by summing up the number of rooms in each overlapping reservation
        int occupiedRoomsCount = 0;
        for (ReservationEntity overlappingReservation : overlappingReservations) {
            occupiedRoomsCount += overlappingReservation.getNumberRooms();
        }

        // Calculate available rooms by subtracting the occupied rooms from the total rooms for that type
        int totalRoomsForType;
        try {
            totalRoomsForType = roomTypeEntitySessionBean.getRoomTypeCount(roomTypeName);
        } catch (RoomTypeNotFoundException e) {
            System.out.println("Room Type Not Found"); // Remember to propagate later
            return null;
        }
        int availableRoomsCount = totalRoomsForType - occupiedRoomsCount;

        if (availableRoomsCount < numberOfRooms) {
            System.out.println("Not enough rooms available for room type: " + roomTypeName + ". Requested: " + numberOfRooms + ", Available: " + availableRoomsCount);
            return null;
        }

        // Calculate the rate for the requested number of rooms for this room type
        Double roomRate = getWalkInRate(checkInDate, checkOutDate, roomTypeName);
        if (roomRate == null) {
            System.out.println("Rate not available for room type: " + roomTypeName);
            return null;
        }

        // Calculate total amount for this room type and create the reservation
        totalAmount = roomRate * numberOfRooms;
        reservationId = reservationEntitySessionBean.createReservationForVisitor(visitor, checkInDate, checkOutDate, totalAmount, roomType, numberOfRooms);

        if (reservationId == null) {
            System.out.println("Reservation could not be created for visitor.");
            return null;
        }

        // Fetch the created reservation entity for further processing
        reservation = reservationEntitySessionBean.findReservationById(reservationId);

        // Update the reservation's total amount
        reservation.setTotalAmount(totalAmount);
        reservationEntitySessionBean.confirmReservation(reservation);

        System.out.println("Reservation created for visitor with ID: " + reservationId);

        // Check if reservation's check-in date is today and time is after 2 a.m.
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        if (checkInDate.equals(today) && currentTime.isAfter(LocalTime.of(2, 0))) {
            // Process reservation immediately
            roomAllocationSessionBean.processReservation(reservation);
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

    public void updateReservationToCheckedIn(ReservationEntity reservation) {
        ReservationStatus newStatus = ReservationStatus.CHECKED_IN;
        reservation.setReservationStatus(newStatus);
        em.merge(reservation);
        em.flush();
        System.out.println("Reservation ID " + reservation.getReservationId() + " status updated to " + newStatus);
    }

    public void updateReservationToCheckedOut(ReservationEntity reservation) {
        ReservationStatus newStatus = ReservationStatus.CHECKED_OUT;
        reservation.setReservationStatus(newStatus);
        em.merge(reservation);
        em.flush();
        System.out.println("Reservation ID " + reservation.getReservationId() + " status updated to " + newStatus);
    }
}
