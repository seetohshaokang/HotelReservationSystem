/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsreservationclient;

import dataaccessobject.AvailableRoomsPerRoomType;
import dataaccessobject.RoomsPerRoomType;
import ejb.session.GuestEntitySessionBeanRemote;
import ejb.session.RoomEntitySessionBeanRemote;
import ejb.session.stateless.helper.RoomReservationSessionBeanRemote;
import entity.GuestEntity;
import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomReservationEntity;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.enumeration.RoomTypeName;
import util.exception.InvalidInputException;
import util.exception.InvalidLoginCredentialException;
import util.exception.VisitorFoundException;

/**
 *
 * @author shaokangseetoh
 */
public class MainApp {

    // Session Beans
    private GuestEntitySessionBeanRemote guestEntitySessionBeanRemote;
    private RoomEntitySessionBeanRemote roomEntitySessionBeanRemote;
    private RoomReservationSessionBeanRemote roomReservationSessionBeanRemote;

    // Module -> No seperation of modules but have 2 "menus"
    private GuestEntity loggedInGuest;

    public MainApp() {
    }

    public MainApp(GuestEntitySessionBeanRemote guestEntitySessionBeanRemote, RoomEntitySessionBeanRemote roomEntitySessionBeanRemote, RoomReservationSessionBeanRemote roomReservationSessionBean) {
        this.guestEntitySessionBeanRemote = guestEntitySessionBeanRemote;
        this.roomEntitySessionBeanRemote = roomEntitySessionBeanRemote;
        this.roomReservationSessionBeanRemote = roomReservationSessionBean;
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to HORS - Reservation System ***");
            System.out.println("1: Guest Login");
            System.out.println("2: Register as Guest");
            System.out.println("3: Search Hotel Room");
            System.out.println("---------------------------");
            System.out.println("4: Exit\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login Successful");
                        menuGuest();
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid Login : " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    registerAsGuest();
                } else if (response == 3) {
                    searchHotelRoom();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid Option, please try again!\n");
                }
            }
            if (response == 4) {
                break;
            }
        }
    }

    private void doLogin() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        String email;
        String password;

        System.out.println("*** Reservation System :: Login ***\n");
        System.out.print("Enter email > ");
        email = scanner.nextLine().trim();
        System.out.print("Enter password > ");
        password = scanner.nextLine().trim();

        if (email.length() > 0 && password.length() > 0) {
            loggedInGuest = guestEntitySessionBeanRemote.guestLogin(email, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential");
        }

    }

    private void registerAsGuest() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HORS System :: System Administrator :: Create New Guest");
        System.out.print("Enter Guest Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Guest Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter Guest Password: ");
        String password = scanner.nextLine().trim();

        try {
            Long guestId = guestEntitySessionBeanRemote.createNewGuest(name, email, password);
            System.out.printf("Guest with name %s and email %s has been successfully registered.%n", name, email);
        } catch (InvalidInputException ex) {
            System.out.println("Invalid Input: " + ex.getMessage());
        }
    }

    private void menuGuest() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** HORS - Reservation System ***\n");
            System.out.println("You are logged in as " + loggedInGuest.getName() + "\n");

            System.out.println("1. Reserve a Room");
            System.out.println("2. View My Reservation Details");
            System.out.println("3. View All My Reservations");
            System.out.println("4. Logout\n");

            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (response == 1) {
                    reserveHotelRoom();
                } else if (response == 2) {
                    viewMyReservationDetails();
                } else if (response == 3) {
                    viewAllMyReservations();
                } else if (response == 4) {
                    break; // Exit the loop and log out
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 4) {
                System.out.println("Logging out...");
                break; // Break out of the menu loop to return to the main menu
            }
        }
    }

    // Same as Guest Relo Officer search hotel case, with reservationrate instead of walkinrate
    private void searchHotelRoom() {
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
            Double totalRate = roomReservationSessionBeanRemote.getReservationRate(checkInDate, checkOutDate, roomTypeName);

            System.out.printf("%-20s || %-20d || %-20.2f%n", roomTypeName, availableRoomsCount, (totalRate != null ? totalRate : 0.0));
        }
    }

    private void reserveHotelRoom() {
        if (loggedInGuest == null) {
            System.out.println("Please login before making a reservation.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
                if (numberOfRooms <= 0) {
                    System.out.println("Number of rooms must be a positive integer. Please try again.");
                    continue;
                }
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

        // Make the reservation
        Long reservationId = roomReservationSessionBeanRemote.reserveRoomForGuest(loggedInGuest.getGuestId(), checkInDate, checkOutDate, roomsToReserve);
        if (reservationId != null) {
            System.out.println("Reservation successful. Your reservation ID is: " + reservationId);
        } else {
            System.out.println("Reservation failed. Please ensure your requested rooms are available.");
        }
    }

    private void viewMyReservationDetails() {
        if (loggedInGuest == null) {
            System.out.println("Please log in to view your reservation details.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Reservation ID to view details: ");
        Long reservationId;
        try {
            reservationId = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Reservation ID. Please enter a valid number.");
            return;
        }

        // Retrieve the reservation details by ID, checking that it belongs to the logged-in guest
        ReservationEntity reservation = guestEntitySessionBeanRemote.retrieveGuestReservationById(reservationId, loggedInGuest.getGuestId());

        if (reservation == null) {
            System.out.println("Reservation not found or you do not have access to this reservation.");
        } else {
            System.out.println("\n*** Reservation Details ***");
            System.out.println("Reservation ID: " + reservation.getReservationId());
            System.out.println("Reservation Date: " + reservation.getReservationDate());
            System.out.println("Check-in Date: " + reservation.getCheckInDate());
            System.out.println("Check-out Date: " + reservation.getCheckOutDate());
            System.out.println("Total Amount: $" + reservation.getTotalAmount());
            System.out.println("Status: " + reservation.getReservationStatus());
        }
    }

    private void viewAllMyReservations() {
        if (loggedInGuest == null) {
            System.out.println("Please log in to view your reservations.");
            return;
        }

        // Retrieve all reservations for the logged-in guest
        List<ReservationEntity> reservations = guestEntitySessionBeanRemote.retrieveAllReservations(loggedInGuest);

        if (reservations.isEmpty()) {
            System.out.println("You have no reservations.");
        } else {
            System.out.println("*** My Reservations ***");

            // Display the serial number, reservation ID, and reservation date for each reservation
            int serialNumber = 1;
            System.out.printf("%-5s %-15s %-20s%n", "S/N", "Reservation ID", "Reservation Date");
            System.out.println("--------------------------------------------------");

            for (ReservationEntity reservation : reservations) {
                System.out.printf("%-5d %-15d %-20s%n", serialNumber++, reservation.getReservationId(), reservation.getReservationDate());
            }
        }
    }
}
