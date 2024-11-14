/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsmanagementclient;

import ejb.session.RoomEntitySessionBeanRemote;
import ejb.session.stateless.helper.RoomReservationSessionBeanRemote;
import entity.EmployeeEntity;
import entity.RoomEntity;
import dataaccessobject.AvailableRoomsPerRoomType;
import dataaccessobject.RoomsPerRoomType;
import ejb.session.stateless.helper.ExceptionReportSessionBeanRemote;
import ejb.session.stateless.helper.RoomCheckInOutSessionBeanRemote;
import entity.ReservationEntity;
import entity.RoomReservationEntity;
import entity.RoomTypeEntity;
import entity.VisitorEntity;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import util.enumeration.EmployeeRole;
import util.enumeration.ReservationStatus;
import util.enumeration.RoomStatus;
import util.enumeration.RoomTypeName;
import util.exception.InvalidAccessRightException;

/**
 *
 * @author shaokangseetoh
 */
public class GuestRelationOfficerModule {

    // Insert relevant sessionbeans;
    private RoomReservationSessionBeanRemote roomReservationSessionBeanRemote;
    private RoomCheckInOutSessionBeanRemote roomCheckInOutSessionBean;

    // State for employee
    private EmployeeEntity currentEmployee;

    public GuestRelationOfficerModule() {
    }

    public GuestRelationOfficerModule(RoomReservationSessionBeanRemote roomReservationSessionBeanRemote, RoomCheckInOutSessionBeanRemote roomCheckInOutSessionBean, EmployeeEntity currentEmployee) {
        this.roomReservationSessionBeanRemote = roomReservationSessionBeanRemote;
        this.roomCheckInOutSessionBean = roomCheckInOutSessionBean;
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
                    walkInSearchRoom();
                } else if (response == 2) {
                    walkInReserveRoom();
                } else if (response == 3) {
                    // checkInReservation();
                } else if (response == 4) {
                    // checkOutReservation();
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
            int availableRoomsCount = roomTypeAvailability.getAvailableRoomsCount();
            Double totalRate = roomReservationSessionBeanRemote.getWalkInRate(checkInDate, checkOutDate, roomTypeName);

            System.out.printf("%-20s || %-20d || %-20.2f%n", roomTypeName, availableRoomsCount, (totalRate != null ? totalRate : 0.0));
        }
    }

    private void walkInReserveRoom() {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Gather visitor details
        System.out.print("Enter Visitor Name: ");
        String visitorName = scanner.nextLine().trim();

        System.out.print("Enter Visitor Email: ");
        String visitorEmail = scanner.nextLine().trim();

        // Create a new VisitorEntity for the walk-in visitor
        VisitorEntity visitor = new VisitorEntity(visitorName, visitorEmail);

        // Prompt for check-in and check-out dates
        LocalDate checkInDate = null;
        LocalDate checkOutDate = null;
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

        // Show available room types for the selected dates
        List<AvailableRoomsPerRoomType> roomTypeAvailabilityList = roomReservationSessionBeanRemote.searchAvailableRooms(checkInDate, checkOutDate);
        System.out.println("\nAvailable Room Types:");
        System.out.printf("%-20s || %-20s%n", "Room Type", "Available Rooms");
        for (AvailableRoomsPerRoomType roomTypeAvailability : roomTypeAvailabilityList) {
            System.out.printf("%-20s || %-20d%n", roomTypeAvailability.getRoomTypeName(), roomTypeAvailability.getAvailableRoomsCount());
        }

        // Prompt for a single room type and quantity
        System.out.print("Enter the room type name to reserve: ");
        RoomTypeName roomTypeName;
        while (true) {
            String roomTypeNameInput = scanner.nextLine().trim();
            try {
                roomTypeName = RoomTypeName.valueOf(roomTypeNameInput.toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid room type. Please try again.");
                System.out.print("Enter the room type name to reserve: ");
            }
        }

        System.out.print("Enter the number of rooms to reserve for " + roomTypeName + ": ");
        int numberOfRooms;
        while (true) {
            try {
                numberOfRooms = Integer.parseInt(scanner.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number of rooms. Please enter a valid integer.");
                System.out.print("Enter the number of rooms to reserve for " + roomTypeName + ": ");
            }
        }

        // Prepare the RoomsPerRoomType object
        RoomsPerRoomType roomsToReserve = new RoomsPerRoomType();
        roomsToReserve.setRoomTypeName(roomTypeName);
        roomsToReserve.setNumRooms(numberOfRooms);

        // Make the reservation for the visitor
        Long reservationId = roomReservationSessionBeanRemote.reserveRoomForVisitor(visitor, checkInDate, checkOutDate, roomsToReserve);
        if (reservationId != null) {
            System.out.println("Reservation successful. The reservation ID is: " + reservationId);
        } else {
            System.out.println("Reservation failed. Please ensure your requested rooms are available.");
        }
    }

    /*
    private void checkInReservation() {
        Scanner scanner = new Scanner(System.in);

        // Prompt for visitor/guest email
        System.out.print("Enter Visitor/Guest Email: ");
        String email = scanner.nextLine().trim();

        try {
            // Retrieve reserved reservations by email
            List<ReservationEntity> reservedReservations = roomCheckInOutSessionBean.findReservedReservationsByEmail(email);

            if (reservedReservations.isEmpty()) {
                System.out.println("No reserved reservations found for email: " + email);
                return;
            }

            System.out.println("\nReserved Reservations for Email: " + email);
            for (ReservationEntity reservation : reservedReservations) {
                System.out.println("Reservation ID: " + reservation.getReservationId()
                        + " | Check-In Date: " + reservation.getCheckInDate()
                        + " | Check-Out Date: " + reservation.getCheckOutDate());
            }

            System.out.print("\nEnter Reservation ID to check-in: ");
            Long reservationId = scanner.nextLong();
            scanner.nextLine(); // Consume newline

            // Find the reservation to check-in
            ReservationEntity reservationToCheckIn = reservedReservations.stream()
                    .filter(res -> res.getReservationId().equals(reservationId))
                    .findFirst()
                    .orElse(null);

            if (reservationToCheckIn == null) {
                System.out.println("Invalid Reservation ID. Please ensure the reservation ID is correct.");
                return;
            }

            // Process each room reservation associated with this reservation
            for (RoomReservationEntity roomReservation : reservationToCheckIn.getRoomReservations()) {
                RoomEntity reservedRoom = roomReservation.getReservedRoom();

                try {
                    // Check if the room is disabled and requires replacement
                    if (reservedRoom.getStatus() == RoomStatus.DISABLED) {
                        System.out.println("Room " + reservedRoom.getRoomNumber()
                                + " cannot be allocated due to its status. Searching for a replacement...");

                        // Use check-in and check-out dates to search for available rooms per room type
                        List<AvailableRoomsPerRoomType> roomTypeAvailabilityList = roomReservationSessionBeanRemote.searchAvailableRooms(
                                reservationToCheckIn.getCheckInDate(), reservationToCheckIn.getCheckOutDate());

                        // Display available room types with at least one available room
                        System.out.println("\nAvailable Room Types for Replacement:");
                        List<RoomTypeName> availableRoomTypes = new ArrayList<>();
                        for (AvailableRoomsPerRoomType roomTypeAvailability : roomTypeAvailabilityList) {
                            if (!roomTypeAvailability.getAvailableRooms().isEmpty()) {
                                System.out.printf("%-20s || %-20d%n", roomTypeAvailability.getRoomTypeName(), roomTypeAvailability.getAvailableRooms().size());
                                availableRoomTypes.add(roomTypeAvailability.getRoomTypeName());
                            }
                        }

                        if (availableRoomTypes.isEmpty()) {
                            System.out.println("No available rooms found for replacement. Manual intervention may be required.");
                            continue;
                        }

                        // Prompt the Guest Relation Officer to select a room type for replacement
                        RoomTypeName selectedRoomType = null;
                        while (selectedRoomType == null) {
                            System.out.print("Select a room type for replacement: ");
                            String roomTypeInput = scanner.nextLine().trim();
                            try {
                                RoomTypeName inputType = RoomTypeName.valueOf(roomTypeInput.toUpperCase());
                                if (availableRoomTypes.contains(inputType)) {
                                    selectedRoomType = inputType;
                                } else {
                                    System.out.println("Selected room type has no available rooms. Please choose another.");
                                }
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid room type. Please try again.");
                            }
                        }

                        // Find and assign the first available room in the selected room type
                        for (AvailableRoomsPerRoomType roomTypeAvailability : roomTypeAvailabilityList) {
                            if (roomTypeAvailability.getRoomTypeName().equals(selectedRoomType)) {
                                RoomEntity replacementRoom = roomTypeAvailability.getAvailableRooms().get(0);
                                roomCheckInOutSessionBean.replaceRoomReservation(roomReservation, replacementRoom);
                                System.out.println("Replacement room " + replacementRoom.getRoomNumber()
                                        + " of type " + selectedRoomType + " has been assigned for check-in.");
                                break; // Replacement found and assigned, exit loop
                            }
                        }

                    } else if (reservedRoom.getStatus() == RoomStatus.AVAILABLE) {
                        // Proceed with standard check-in for assigned room
                        roomCheckInOutSessionBean.checkInRoomReservation(roomReservation);
                        System.out.println("Room " + reservedRoom.getRoomNumber() + " checked-in successfully.");
                    }
                } catch (Exception ex) {
                    System.out.println("Error checking in room " + reservedRoom.getRoomNumber() + ": " + ex.getMessage());
                }
            }

            // Update reservation status to CHECKED_IN after all room reservations are processed
            roomReservationSessionBeanRemote.updateReservationToCheckedIn(reservationToCheckIn);

            System.out.println("Reservation ID " + reservationId + " has been checked in successfully.");

        } catch (Exception ex) {
            System.out.println("An error occurred while checking in the reservation: " + ex.getMessage());
        }
    }

    private void checkOutReservation() {
        Scanner scanner = new Scanner(System.in);

        // Prompt for visitor/guest email
        System.out.print("Enter Visitor/Guest Email: ");
        String email = scanner.nextLine().trim();

        try {
            // Retrieve checked-in reservations by email
            List<ReservationEntity> checkedInReservations = roomCheckInOutSessionBean.findReservationsByEmailAndStatus(email, ReservationStatus.CHECKED_IN);

            if (checkedInReservations.isEmpty()) {
                System.out.println("No checked-in reservations found for email: " + email);
                return;
            }

            // Display checked-in reservations
            System.out.println("\nChecked-In Reservations for Email: " + email);
            for (ReservationEntity reservation : checkedInReservations) {
                System.out.println("Reservation ID: " + reservation.getReservationId()
                        + " | Check-In Date: " + reservation.getCheckInDate()
                        + " | Check-Out Date: " + reservation.getCheckOutDate());
            }

            // Prompt for reservation ID to check-out
            System.out.print("\nEnter Reservation ID to check-out: ");
            Long reservationId = scanner.nextLong();
            scanner.nextLine(); // Consume newline

            // Find the selected reservation for check-out
            ReservationEntity reservationToCheckOut = checkedInReservations.stream()
                    .filter(res -> res.getReservationId().equals(reservationId))
                    .findFirst()
                    .orElse(null);

            if (reservationToCheckOut == null) {
                System.out.println("Invalid Reservation ID. Please ensure the reservation ID is correct.");
                return;
            }

            // Process each room reservation within the selected reservation
            for (RoomReservationEntity roomReservation : reservationToCheckOut.getRoomReservations()) {
                RoomEntity reservedRoom = roomReservation.getReservedRoom();

                try {
                    // Only proceed if the room is currently occupied
                    if (reservedRoom.getStatus() == RoomStatus.OCCUPIED) {
                        // Call the session bean to perform checkout for this room reservation
                        roomCheckInOutSessionBean.checkOutRoomReservation(roomReservation);
                        System.out.println("Room " + reservedRoom.getRoomNumber() + " checked-out successfully.");
                    }
                } catch (Exception ex) {
                    System.out.println("Error checking out room " + reservedRoom.getRoomNumber() + ": " + ex.getMessage());
                }
            }

            // Update the reservation status to CHECKED_OUT using session bean method
            roomReservationSessionBeanRemote.updateReservationToCheckedOut(reservationToCheckOut);

            System.out.println("Reservation ID " + reservationId + " has been checked out successfully.");

        } catch (Exception ex) {
            System.out.println("An error occurred while checking out the reservation: " + ex.getMessage());
        }
    }

     */
}
