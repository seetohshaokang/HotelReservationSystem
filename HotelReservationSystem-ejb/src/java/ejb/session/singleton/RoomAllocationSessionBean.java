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
import util.enumeration.ReservationStatus;
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
        RoomTypeName roomTypeName = reservation.getRoomType().getRoomTypeName();
        int numberOfRoomsNeeded = reservation.getNumberRooms();
        LocalDate checkInDate = reservation.getCheckInDate();
        LocalDate checkOutDate = reservation.getCheckOutDate();
        List<RoomEntity> allocatedRooms = new ArrayList<>();

        // Retrieve available rooms for the specified room type
        List<RoomEntity> availableRooms = roomEntitySessionBean.retrieveAvailableRooms(roomTypeName);

        // Check if there are enough available rooms of the requested type
        if (availableRooms.size() >= numberOfRoomsNeeded) {
            for (int i = 0; i < numberOfRoomsNeeded; i++) {
                RoomEntity room = availableRooms.get(i);

                // Create a RoomReservationEntity for each room using the constructor
                RoomReservationEntity roomReservation = new RoomReservationEntity(room, reservation);
                room.setStatus(RoomStatus.OCCUPIED); // Assume status changes on allocation

                // Add the RoomReservationEntity to the reservation
                reservation.getRoomReservations().add(roomReservation);
                allocatedRooms.add(room);
            }
            System.out.println("Rooms allocated for reservation with ID: " + reservation.getReservationId());
        } else {
            // Attempt to upgrade to a higher room type if there aren't enough rooms
            RoomTypeName nextHigherRoomType = reservation.getRoomType().getNextHigherRoomTypeName();
            if (nextHigherRoomType != null) {
                List<RoomEntity> higherAvailableRooms = roomEntitySessionBean.retrieveAvailableRooms(nextHigherRoomType);

                if (higherAvailableRooms.size() >= numberOfRoomsNeeded) {
                    for (int i = 0; i < numberOfRoomsNeeded; i++) {
                        RoomEntity upgradedRoom = higherAvailableRooms.get(i);

                        // Create RoomReservationEntity with upgraded room
                        RoomReservationEntity roomReservation = new RoomReservationEntity(upgradedRoom, reservation);
                        upgradedRoom.setStatus(RoomStatus.OCCUPIED);

                        reservation.getRoomReservations().add(roomReservation);
                        allocatedRooms.add(upgradedRoom);
                    }
                    exceptionReportSessionBean.createTypeOneException(availableRooms.get(0), allocatedRooms.get(0));
                    System.out.println("Upgraded rooms allocated for reservation with ID: " + reservation.getReservationId());
                } else {
                    // No rooms available in the higher room type
                    exceptionReportSessionBean.createTypeTwoException(availableRooms.get(0));
                    System.out.println("No available rooms found for the requested type or next higher type.");
                }
            } else {
                // No next higher room type available
                exceptionReportSessionBean.createTypeTwoException(availableRooms.get(0));
                System.out.println("No higher room type available for reservation.");
            }
        }
    }

}
