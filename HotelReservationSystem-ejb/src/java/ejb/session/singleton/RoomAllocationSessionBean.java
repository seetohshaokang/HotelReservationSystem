/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import dataaccessobject.RoomsPerRoomType;
import ejb.session.stateless.ReservationEntitySessionBeanLocal;
import ejb.session.stateless.helper.RoomReservationSessionBeanLocal;
import ejb.session.stateless.RoomEntitySessionBeanLocal;
import ejb.session.stateless.RoomReservationEntitySessionBeanLocal;
import ejb.session.stateless.RoomTypeEntitySessionBeanLocal;
import ejb.session.stateless.helper.ExceptionReportSessionBeanLocal;
import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomReservationEntity;
import entity.RoomTypeEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Timer;
import util.enumeration.ReservationStatus;
import util.enumeration.RoomStatus;
import util.enumeration.RoomTypeName;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author bryan
 */
@Singleton
@LocalBean // Can use no-interface view locally
public class RoomAllocationSessionBean {

    @EJB
    private ExceptionReportSessionBeanLocal exceptionReportSessionBean;

    @EJB
    private ReservationEntitySessionBeanLocal reservationEntitySessionBean;

    @EJB
    private RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBean;

    @EJB
    private RoomReservationEntitySessionBeanLocal roomReservationEntitySessionBean;

    @EJB
    private RoomReservationSessionBeanLocal roomReservationSessionBean;

    @EJB
    private RoomEntitySessionBeanLocal roomEntitySessionBean;

    // Daily scheduled task to allocate rooms for reservations checking in on the current date
    @Schedule(hour = "2", minute = "0", second = "0", info = "Daily Room Allocation Job at 2 a.m.")
    public void allocateRoomsDaily(Timer timer) {
        System.out.println("Executing daily room allocation job at 2 a.m.");

        // Get today's date to allocate rooms for today's check-ins
        LocalDate today = LocalDate.now();
        allocateRoomsForThatDay(today);
    }

    // Allocate rooms for a particular day
    public void allocateRoomsForThatDay(LocalDate checkInDate) {
        // Step 1: Retrieve a list of reservations with the given check-in date
        List<ReservationEntity> reservationsForDate = reservationEntitySessionBean.findReservationsByCheckInDate(checkInDate);

        // Step 2: Iterate through each reservation and process associated room reservations
        for (ReservationEntity reservation : reservationsForDate) {
            processReservation(reservation);
        }
    }

    // Can be used to immediately process reservation for same day check in
    public void processReservation(ReservationEntity reservation) {
        try {
            // Step 1: Check if the reservation already has room allocations
            if (reservationEntitySessionBean.hasRoomAllocations(reservation.getReservationId())) {
                System.out.println("Reservation with ID: " + reservation.getReservationId() + " already has room allocations.");
                return; // Exit the method to avoid duplicate allocations
            }
        } catch (ReservationNotFoundException e) {
            System.out.println("Reservation not found: " + e.getMessage());
            return; // Exit early since the reservation does not exist
        }

        RoomTypeName roomTypeName = reservation.getRoomType().getRoomTypeName();
        int numberOfRoomsNeeded = reservation.getNumberRooms();
        LocalDate checkInDate = reservation.getCheckInDate();
        LocalDate checkOutDate = reservation.getCheckOutDate();

        // Step 2: Retrieve available rooms for the specified room type
        List<RoomEntity> availableRooms = roomEntitySessionBean.retrieveAvailableRooms(roomTypeName);

        int roomsAllocated = 0;

        // Allocate rooms of the requested type first
        for (RoomEntity room : availableRooms) {
            if (roomsAllocated < numberOfRoomsNeeded) {
                reservationEntitySessionBean.addRoomReservationToReservation(room, reservation);
                roomsAllocated++;
            }
        }

        // Step 3: Check if more rooms are needed
        int remainingRooms = numberOfRoomsNeeded - roomsAllocated;

        if (remainingRooms > 0) {
            // Retrieve rooms from the next higher room type
            RoomTypeName nextHigherRoomType = reservation.getRoomType().getNextHigherRoomTypeName();
            if (nextHigherRoomType != null) {
                List<RoomEntity> higherAvailableRooms = roomEntitySessionBean.retrieveAvailableRooms(nextHigherRoomType);

                if (higherAvailableRooms.size() >= remainingRooms) {
                    // Allocate remaining rooms from the next higher room type
                    for (int i = 0; i < remainingRooms; i++) {
                        RoomEntity upgradedRoom = higherAvailableRooms.get(i);
                        reservationEntitySessionBean.addRoomReservationToReservation(upgradedRoom, reservation);

                        // Update room status to occupied
                        upgradedRoom.setStatus(RoomStatus.OCCUPIED);
                    }

                    // Create an exception report for the upgrade
                    exceptionReportSessionBean.createTypeOneException(
                            availableRooms.isEmpty() ? null : availableRooms.get(0),
                            higherAvailableRooms.get(0)
                    );
                    System.out.println("Upgraded rooms allocated for reservation with ID: " + reservation.getReservationId());
                } else {
                    // Step 4: Handle cases where not enough rooms are available in the next higher type
                    exceptionReportSessionBean.createTypeTwoException(availableRooms.isEmpty() ? null : availableRooms.get(0));
                    System.out.println("Not enough rooms available for the requested type or next higher type.");
                }
            } else {
                // Step 5: Handle cases where no higher room type exists
                exceptionReportSessionBean.createTypeTwoException(availableRooms.isEmpty() ? null : availableRooms.get(0));
                System.out.println("No higher room type available for reservation.");
            }
        } else {
            System.out.println("All rooms allocated for reservation with ID: " + reservation.getReservationId());
        }
    }
}
