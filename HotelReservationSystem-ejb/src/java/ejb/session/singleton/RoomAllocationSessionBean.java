/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.helper.RoomReservationSessionBeanLocal;
import ejb.session.stateless.RoomEntitySessionBeanLocal;
import ejb.session.stateless.RoomReservationEntitySessionBeanLocal;
import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomReservationEntity;
import entity.RoomTypeEntity;
import java.time.LocalDate;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import util.enumeration.ReservationStatus;
import util.enumeration.RoomStatus;

/**
 *
 * @author bryan
 */
@Singleton
@LocalBean
public class RoomAllocationSessionBean {

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

            for (ReservationEntity reservation : reservations) {
                try {
                    allocateRoomForReservation(reservation);
                } catch (Exception e) {
                    exceptionReportSessionBean.logException("Error allocating room for reservation ID "
                            + reservation.getReservationId() + ": " + e.getMessage());
                }
            }

        } catch (Exception e) {
            exceptionReportSessionBean.logException("Error in daily room allocation job: " + e.getMessage());
        }
    }
     */
    
    /*
    public void allocateRoomsForThatDay(LocalDate checkInDate) {
        // Step 1: Retrieve a list of room reservation entities with the given checkInDate.
        List<RoomReservationEntity> reservationsForDate = roomReservationEntitySessionBean.findRoomReservationsByDate(checkInDate);

        // Step 2: Iterate through the list of room reservation entities.
        for (RoomReservationEntity reservation : reservationsForDate) {
            RoomEntity reservedRoom = reservation.getReservedRoom();

            // Step 3: Check if the room associated with this reservation is disabled.
            if (reservedRoom.getStatus() == RoomStatus.DISABLED) {
                System.out.println("Room " + reservedRoom.getRoomNumber() + " is disabled and cannot be allocated.");
                // Mark the reservation as not assigned (or another status as needed).
                reservation.setIsAssigned(false); // Example: mark as unassigned or canceled
                continue; // Skip further processing for this reservation
            }

            // Step 4: If the room is available, proceed with allocation.
            if (reservedRoom.getStatus() == RoomStatus.AVAILABLE) {
                // Mark the room reservation as assigned.
                reservation.setIsAssigned(true);
                // Update the room status to not available (occupied).
                reservedRoom.setStatus(RoomStatus.NOT_AVAILABLE);
            }
        }
    }
    */
}
