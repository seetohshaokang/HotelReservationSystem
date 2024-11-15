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
        RoomTypeName roomTypeName = reservation.getRoomType().getRoomTypeName();
        int numberOfRoomsNeeded = reservation.getNumberRooms();
        LocalDate checkInDate = reservation.getCheckInDate();
        LocalDate checkOutDate = reservation.getCheckOutDate();

        // Retrieve available rooms for the specified room type
        List<RoomEntity> availableRooms = roomEntitySessionBean.retrieveAvailableRooms(roomTypeName);

        if (availableRooms.size() >= numberOfRoomsNeeded) {
            for (int i = 0; i < numberOfRoomsNeeded; i++) {
                RoomEntity room = availableRooms.get(i);

                // Use the new method to add room reservations
                reservationEntitySessionBean.addRoomReservationToReservation(room, reservation);
            }
            System.out.println("Rooms allocated for reservation with ID: " + reservation.getReservationId());
        } else {
            // Handle upgrading to the next higher room type, similar to the original implementation
            RoomTypeName nextHigherRoomType = reservation.getRoomType().getNextHigherRoomTypeName();
            if (nextHigherRoomType != null) {
                List<RoomEntity> higherAvailableRooms = roomEntitySessionBean.retrieveAvailableRooms(nextHigherRoomType);

                if (higherAvailableRooms.size() >= numberOfRoomsNeeded) {
                    for (int i = 0; i < numberOfRoomsNeeded; i++) {
                        RoomEntity upgradedRoom = higherAvailableRooms.get(i);

                        // Use the new method to add upgraded room reservations
                        reservationEntitySessionBean.addRoomReservationToReservation(upgradedRoom, reservation);

                        upgradedRoom.setStatus(RoomStatus.OCCUPIED);
                    }
                    exceptionReportSessionBean.createTypeOneException(availableRooms.get(0), higherAvailableRooms.get(0));
                    System.out.println("Upgraded rooms allocated for reservation with ID: " + reservation.getReservationId());
                } else {
                    exceptionReportSessionBean.createTypeTwoException(availableRooms.get(0));
                    System.out.println("No available rooms found for the requested type or next higher type.");
                }
            } else {
                exceptionReportSessionBean.createTypeTwoException(availableRooms.get(0));
                System.out.println("No higher room type available for reservation.");
            }
        }
    }
}
