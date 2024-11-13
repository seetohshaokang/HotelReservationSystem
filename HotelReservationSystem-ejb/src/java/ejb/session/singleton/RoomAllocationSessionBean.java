/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateful.RoomReservationSessionBean;
import ejb.session.stateless.RoomEntitySessionBean;
import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Timer;

/**
 *
 * @author bryan
 */
@Singleton
@LocalBean
public class RoomAllocationSessionBean {

    @EJB
    private RoomReservationSessionBean roomReservationSessionBean;

    @EJB
    private RoomEntitySessionBean roomEntitySessionBean;

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

    private void allocateRoomForReservation(ReservationEntity reservation) throws Exception {
        RoomTypeEntity roomType = reservation.getRoomType();
        LocalDate checkInDate = reservation.getCheckInDate();
        LocalDate checkOutDate = reservation.getCheckOutDate();

        RoomEntity availableRoom = roomEntitySessionBean.findAvailableRoom(roomType, checkInDate, checkOutDate);

        if (availableRoom != null) {
            roomReservationSessionBean.allocateRoom(reservation, availableRoom);
        } else {
            RoomTypeEntity upgradedRoomType = roomEntitySessionBean.findNextAvailableRoomType(roomType, checkInDate, checkOutDate);

            if (upgradedRoomType != null) {
                RoomEntity upgradedRoom = roomEntitySessionBean.findAvailableRoom(upgradedRoomType, checkInDate, checkOutDate);
                roomReservationSessionBean.allocateRoom(reservation, upgradedRoom);
            } else {
                throw new Exception("Overbooking: No available rooms for reservation ID " + reservation.getReservationId());
            }
        }
    }
*/
}
