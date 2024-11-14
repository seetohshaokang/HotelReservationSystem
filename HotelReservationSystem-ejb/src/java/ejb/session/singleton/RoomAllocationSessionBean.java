/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

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
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import util.enumeration.RoomStatus;
import util.enumeration.RoomTypeName;

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
    
    

    //goatgpt stuff
    /*@EJB
    private ExceptionReportSessionBean exceptionReportSessionBean;

    // Scheduled method to run daily at 2 a.m.
    @Schedule(hour = "2", minute = "0", second = "0", info = "Daily room allocation job")
    public void allocateRooms(Timer timer) {
        System.out.println("Executing daily room allocation job at 2 a.m.");

        try {
            List<ReservationEntity> reservations = roomReservationSessionBean.getPendingReservations();

            for (ReservationEntity roomReservation : reservations) {
                try {
                    allocateRoomForReservation(roomReservation);
                } catch (Exception e) {
                    exceptionReportSessionBean.logException("Error allocating room for roomReservation ID "
                            + roomReservation.getReservationId() + ": " + e.getMessage());
                }
            }

        } catch (Exception e) {
            exceptionReportSessionBean.logException("Error in daily room allocation job: " + e.getMessage());
        }
    }
     */
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
        // Step 3: Iterate through the list of room reservations associated with this reservation
        for (RoomReservationEntity roomReservation : reservation.getRoomReservations()) {
            RoomEntity reservedRoom = roomReservation.getReservedRoom();

            // Step 4: Check if the reserved room is disabled
            if (reservedRoom.getStatus() == RoomStatus.DISABLED) {
                System.out.println("Room " + reservedRoom.getRoomNumber() + " is disabled and cannot be allocated.");

                // Retrieve the current room type
                RoomTypeEntity currentRoomType = reservedRoom.getRoomType();

                // Find the next higher room type with an available room for the given date range
                RoomTypeName nextHigherRoomType = currentRoomType.getNextHigherRoomTypeName();
                if (nextHigherRoomType != null) {
                    List<RoomEntity> availableRooms = roomEntitySessionBean.retrieveAvailableRooms(
                            roomReservation.getReservation().getCheckInDate(),
                            roomReservation.getReservation().getCheckOutDate(),
                            nextHigherRoomType
                    );

                    // If there's an available room in the next higher room type
                    if (!availableRooms.isEmpty()) {
                        RoomEntity upgradedRoom = availableRooms.get(0); // Get the first available room
                        roomReservation.setReservedRoom(upgradedRoom); // Assign the upgraded room
                        roomReservation.setIsAssigned(true); // Mark as assigned

                        // Generate TypeOne exception report
                        exceptionReportSessionBean.createTypeOneException(reservedRoom, upgradedRoom);
                        System.out.println("Upgraded Room " + upgradedRoom.getRoomNumber() + " assigned for reservation.");
                    } else {
                        System.out.println("No available rooms found in next higher room type for reservation.");

                        // Generate TypeTwo exception report
                        exceptionReportSessionBean.createTypeTwoException(reservedRoom);
                        roomReservation.setIsAssigned(false); // Mark as not assigned
                    }
                } else {
                    System.out.println("No higher room type available for reservation.");

                    // Generate TypeTwo exception report
                    exceptionReportSessionBean.createTypeTwoException(reservedRoom);
                    roomReservation.setIsAssigned(false); // Mark as not assigned
                }
            } else if (reservedRoom.getStatus() == RoomStatus.AVAILABLE) {
                // Step 5: If the room is available, proceed with allocation.
                roomReservation.setIsAssigned(true);
                // Dont change room status yet
                System.out.println("Room " + reservedRoom.getRoomNumber() + " allocated to reservation.");
            }
        }
    }
}
