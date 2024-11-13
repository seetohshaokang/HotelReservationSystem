/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsmanagementclient;

import ejb.session.RoomEntitySessionBeanRemote;
import ejb.session.stateful.RoomReservationSessionBeanRemote;
import entity.EmployeeEntity;
import entity.RoomEntity;
import dataaccessobject.AvailableRoomsPerRoomType;
import entity.RoomTypeEntity;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import util.enumeration.EmployeeRole;
import util.enumeration.RoomTypeName;
import util.exception.InvalidAccessRightException;

/**
 *
 * @author shaokangseetoh
 */
public class GuestRelationOfficerModule {

    // Insert relevant sessionbeans;
    private RoomReservationSessionBeanRemote roomReservationSessionBeanRemote;

    // State for employee
    private EmployeeEntity currentEmployee;

    public GuestRelationOfficerModule() {
    }

    public GuestRelationOfficerModule(RoomReservationSessionBeanRemote roomReservationSessionBeanRemote, EmployeeEntity currentEmployee) {
        this.roomReservationSessionBeanRemote = roomReservationSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }

    // Insert constructor with appropriate sessionbean
    public void menuGuestRelationOfficer() throws InvalidAccessRightException {

        if (currentEmployee.getRole() != EmployeeRole.GUEST_RELATION_OFFICER) {
            throw new InvalidAccessRightException("You dont' have GUEST RELATION OFFICER rights to access the guest relation officer module.");

        }
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** HORS System :: Guest Relation Officer Module ***\n");
            System.out.println("1: Walk-in Search Room");
            System.out.println("2: Walk-in Reserve Room");
            System.out.println("3: Check-in Guest");
            System.out.println("4: Check-out Guest");
            System.out.println("---------------------------");
            System.out.println("5: Back\n");
            response = 0;

            while (response < 1 || response > 5) {
                System.out.print("> ");
                response = scanner.nextInt();
                if (response == 1) {
                    System.out.println("Feature not implemented yet");
                    walkInSearchRoom();
                } else if (response == 2) {
                    System.out.println("Feature not implemented yet");
                } else if (response == 3) {
                    System.out.println("Feature not implemented yet");
                } else if (response == 4) {
                    System.out.println("Feature not implemented yet");
                } else if (response == 5) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 5) {
                break;
            }
        }
    }

    private void walkInSearchRoom() {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate checkInDate = null;
        LocalDate checkOutDate = null;

        // Prompt for check-in and check-out dates
        while (checkInDate == null) {
            System.out.print("Enter check-in date (yyyy-MM-dd): ");
            String checkInInput = scanner.nextLine();
            try {
                checkInDate = LocalDate.parse(checkInInput, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
            }
        }
        while (checkOutDate == null) {
            System.out.print("Enter check-out date (yyyy-MM-dd): ");
            String checkOutInput = scanner.nextLine();
            try {
                checkOutDate = LocalDate.parse(checkOutInput, formatter);
                if (checkOutDate.isBefore(checkInDate)) {
                    System.out.println("Check-out date must be after check-in date. Please enter again.");
                    checkOutDate = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
            }
        }

        // Call searchAvailableRooms to get a list of AvailableRoomsPerRoomType objects
        List<AvailableRoomsPerRoomType> roomTypeAvailabilityList = roomReservationSessionBeanRemote.searchAvailableRooms(checkInDate, checkOutDate);

        // Header for the output table
        System.out.printf("%-20s || %-20s || %-20s%n", "Room Type", "Available Rooms", "Total Rate Per Room For Given Duration");

        // Print each AvailableRoomsPerRoomType object with details
        for (AvailableRoomsPerRoomType roomTypeAvailability : roomTypeAvailabilityList) {
            RoomTypeName roomTypeName = roomTypeAvailability.getRoomTypeName();
            List<RoomEntity> availableRooms = roomTypeAvailability.getAvailableRooms();
            Double totalRate = roomReservationSessionBeanRemote.getWalkInRate(checkInDate, checkOutDate, roomTypeName);

            System.out.printf("%-20s || %-20d || %-20.2f%n", roomTypeName, availableRooms.size(), (totalRate != null ? totalRate : 0.0));
        }
    }

    private void walkInReserveRoom() {

    }

}
