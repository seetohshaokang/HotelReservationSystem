/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsreservationclient;

import ejb.session.GuestEntitySessionBeanRemote;
import ejb.session.RoomEntitySessionBeanRemote;
import ejb.session.stateful.RoomReservationSessionBeanRemote;
import entity.GuestEntity;
import entity.RoomEntity;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import util.enumeration.RoomTypeName;
import util.exception.InvalidInputException;
import util.exception.InvalidLoginCredentialException;

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
        
        if(email.length() > 0 && password.length() > 0) {
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
            System.out.printf("Guest with name %s email %s has been successfully registed.%n", name, email);            
        } catch (InvalidInputException ex) {
            System.out.println("Invalid Input: " + ex.getMessage());
        }
    }

    // Same as Guest Relo Officer search hotel case
    private void searchHotelRoom() {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate checkInDate = null;
        LocalDate checkOutDate = null;

        // Read and parse checkInDate
        while (checkInDate == null) {
            System.out.print("Enter check-in date (yyyy-MM-dd): ");
            String checkInInput = scanner.nextLine();
            try {
                checkInDate = LocalDate.parse(checkInInput, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
            }
        }
        // Read and parse checkOutDate
        while (checkOutDate == null) {
            System.out.print("Enter check-out date (yyyy-MM-dd): ");
            String checkOutInput = scanner.nextLine();
            try {
                checkOutDate = LocalDate.parse(checkOutInput, formatter);

                // Ensure checkOutDate is after checkInDate
                if (checkOutDate.isBefore(checkInDate)) {
                    System.out.println("Check-out date must be after check-in date. Please enter again.");
                    checkOutDate = null; // Reset checkOutDate to re-prompt user
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
            }
        }
        System.out.printf("%-20s || %-20s%n", "Room Type", "Number of Available Rooms");

        // Loop through each RoomTypeName and fetch available rooms
        for (RoomTypeName roomTypeName : RoomTypeName.values()) {
            List<RoomEntity> availableRooms = roomReservationSessionBeanRemote.searchAvailableRooms(checkInDate, checkOutDate, roomTypeName);

            // Print the room type name and the number of available rooms
            System.out.printf("%-20s || %-20d%n", roomTypeName, availableRooms.size());
        }
    }

    private void menuGuest() {

    }

    private void reserveHotelRoom() {
        
    }

    private void viewMyReservationDetails() {
    }

    private void viewAllMyReservations() {
    }

}
