/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsmanagementclient;

import ejb.session.RoomRateEntitySessionBeanRemote;
import ejb.session.RoomTypeEntitySessionBeanRemote;
import entity.EmployeeEntity;
import entity.RoomRateEntity;
import entity.RoomTypeEntity;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import util.enumeration.EmployeeRole;
import util.enumeration.RateType;
import util.exception.InvalidAccessRightException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author bryan
 */
public class SalesManagerModule {

    // Insert relevant sessionbeans
    private RoomRateEntitySessionBeanRemote roomRateEntitySessionBeanRemote;
    private RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;

    // State for login
    private EmployeeEntity currentEmployee;

    public SalesManagerModule() {
    }

    // Insert constructor with appropriate sessionbean
    public SalesManagerModule(RoomRateEntitySessionBeanRemote roomRateEntitySessionBeanRemote, RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote, EmployeeEntity currentEmployee) {
        this.roomRateEntitySessionBeanRemote = roomRateEntitySessionBeanRemote;
        this.roomTypeEntitySessionBeanRemote = roomTypeEntitySessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }

    public void menuSalesManager() throws InvalidAccessRightException {
        if (currentEmployee.getRole() != EmployeeRole.SALES_MANAGER) {
            throw new InvalidAccessRightException("You don't have SALES MANAGER rights to access the sales manager module.");
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("*** HORS System :: Sales Manager Module ***\n");
            System.out.println("1: Create New Room Rate");
            System.out.println("2: View Room Rate Details");
            System.out.println("3: Update Room Rate");
            System.out.println("4: Delete Room Rate");
            System.out.println("5: View all Room Rates");
            System.out.println("---------------------------");
            System.out.println("6: Back\n");
            Integer response = 0;

            while (response < 1 || response > 6) {

                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1) {
                    createNewRoomRate();
                } else if (response == 2) {
                    viewRoomRateDetails();
                } else if (response == 3) {
                    updateRoomRateDetails();
                } else if (response == 4) {
                    deleteRoomRate();
                } else if (response == 5) {
                    viewAllRoomRates();
                } else if (response == 6) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 6) {
                break;
            }
        }
    }

    // Use case 17
    private void createNewRoomRate() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HORS System :: Sales Manager :: Create New Room Rate ***\n");
        System.out.println("-------------------------------------------------------------------");
        // Input name
        String name = "";
        while (name.trim().isEmpty()) {
            System.out.print("Enter name > ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty. Please enter a valid name.");
            }
        }
        // Input Room Type
        viewAllRoomTypes();
        Integer roomTypeId = null;
        while (roomTypeId == null) {
            System.out.print("Enter the Room Type Id you wish to create a new room rate for> ");
            if (scanner.hasNextInt()) {
                roomTypeId = scanner.nextInt();
                scanner.nextLine();  // Clear buffer
            } else {
                System.out.println("Please enter a valid Room Type Id!");
                scanner.nextLine();  // Clear invalid input
            }
        }

        // Select Rate Type
        Integer response = 0;
        RateType selectedRateType = null;
        while (selectedRateType == null) {
            System.out.println("Please Select Rate Type");
            System.out.println("1: PUBLISHED");
            System.out.println("2: NORMAL");
            System.out.println("3: PEAK");
            System.out.println("4: PROMOTION");
            System.out.print("Enter rate type number> ");

            if (scanner.hasNextInt()) {
                response = scanner.nextInt();
                scanner.nextLine();  // Clear buffer

                if (response >= 1 && response <= 4) {
                    selectedRateType = RateType.values()[response - 1];
                } else {
                    System.out.println("Invalid option. Please select a number between 1 and 4.");
                }
            } else {
                System.out.println("Please enter a valid number!");
                scanner.nextLine();  // Clear invalid input
            }
        }

        // Input rate per night
        Double ratePerNight = null;
        while (ratePerNight == null) {
            System.out.print("Enter rate per night > ");
            if (scanner.hasNextDouble()) {
                ratePerNight = scanner.nextDouble();
                scanner.nextLine();  // Clear buffer
                System.out.println("Entered Rate per Night: " + ratePerNight);  // Debug statement
            } else {
                System.out.println("Please input a valid Rate per night!");
                scanner.nextLine();  // Clear invalid input
            }
        }

        LocalDate startDate = null, endDate = null;
        if (response == 3 || response == 4) {
            while (true) {
                System.out.print("Enter start date (YYYY-MM-DD) > ");
                String input = scanner.nextLine();
                try {
                    startDate = parseDate(input);
                    break;
                } catch (DateTimeParseException e) {
                    System.out.println("Please input a valid start date in the format YYYY-MM-DD!");
                }
            }
            while (true) {
                System.out.print("Enter end date (YYYY-MM-DD) > ");
                String input2 = scanner.nextLine();
                try {
                    endDate = parseDate(input2);
                    break;
                } catch (DateTimeParseException e) {
                    System.out.println("Please input a valid end date in the format YYYY-MM-DD!");
                }
            }
        }

        try {
            String newRateName = roomRateEntitySessionBeanRemote.createNewRoomRate(name, Long.valueOf(roomTypeId), selectedRateType, ratePerNight, startDate, endDate);
            System.out.println("New room rate has been created: " + newRateName);
        } catch (RoomTypeNotFoundException e) {
            System.out.println("Invalid creation of new room rate: " + e.getMessage());
        }
    }

    // Use case 18
    private void viewRoomRateDetails() {
        Scanner scanner = new Scanner(System.in);
        viewAllRoomRates(); // Show the list of roomrates
        Integer roomRateId = 0;
        System.out.print("Enter ID of a displayed room rate to view more details > ");
        roomRateId = scanner.nextInt();
        scanner.nextLine();
        try {
            RoomRateEntity roomRate = roomRateEntitySessionBeanRemote.getRoomRateById(Long.valueOf(roomRateId));
            String start, end;
            if (roomRate.getRateType() == RateType.PEAK || roomRate.getRateType() == RateType.PROMOTION) {
                start = roomRate.getStartDate().toString();
                end = roomRate.getEndDate().toString();
            } else {
                start = "NA";
                end = "NA";
            }
            System.out.printf("%-20s || %-20s || %-20s || %-20s || %-20s || %-20s || %-20s%n",
                    "Room Rate Id", "Room Rate Name", "Room Type", "Rate Type", "Rate per night", "Start Date", "End Date");
            System.out.printf("%-20d || %-20s || %-20s || %-20s || %-20f || %-20s || %-20s%n",
                    roomRate.getRoomRateId(), roomRate.getName(), roomRate.getRoomType().getRoomTypeName().toString(), roomRate.getRateType().toString(), roomRate.getRatePerNight(), start, end);
        } catch (RoomRateNotFoundException ex) {
            System.out.println("Invalid room type: " + ex.getMessage());
        }
    }

    private void updateRoomRateDetails() {
        Scanner scanner = new Scanner(System.in);
        viewAllRoomRates();
        Integer id = 0;
        System.out.print("Enter Room Rate Id of the room rate you wish to update > ");
        id = scanner.nextInt();
        scanner.nextLine();

        // Input name
        String name = "";
        while (true) {
            System.out.print("Enter new name > ");
            if (scanner.hasNextLine()) {
                name = scanner.nextLine();
                break;
            } else {
                System.out.println("Please input a valid name!");
            }
        }
        // Input Room Type
        viewAllRoomTypes();
        Integer roomTypeId = 0;
        System.out.println("Enter the Room Type Id of the room type you wish to update the room rate > ");
        roomTypeId = scanner.nextInt();
        scanner.nextLine();

        Integer response;
        while (true) {
            response = 0;
            System.out.println("Please Select Rate Type");
            System.out.println("1: PUBLISHED");
            System.out.println("2: NORMAL");
            System.out.println("3: PEAK");
            System.out.println("4: PROMOTION");
            System.out.print("Enter rate type number> ");
            response = scanner.nextInt();
            scanner.nextLine();

            if (response < 1 || response > 4) {
                System.out.println("Invalid option. Please select a number between 1 and 5.");
            } else {
                System.out.println("You have selected option " + response);
                break;
            }
        }
        RateType selectedRateType = RateType.values()[response - 1];
        // Input rate per night
        double ratePerNight = 0;
        while (true) {
            System.out.print("Enter rate per night > ");
            if (scanner.hasNextDouble()) {
                ratePerNight = scanner.nextDouble();
                scanner.nextLine();
                break;
            } else {
                System.out.println("Please input a valid Rate per night!");
            }
        }
        LocalDate startDate = null;
        LocalDate endDate = null;
        // Input start date and end date (For peak and promotion rate types only)
        if (response == 3 || response == 4) {
            while (true) {
                System.out.print("Enter start date (YYYY-MM-DD) > ");
                String input = scanner.nextLine();
                try {
                    startDate = parseDate(input);
                    break;
                } catch (DateTimeParseException e) {
                    System.out.println("Please input a valid start date in the format YYYY-MM-DD!");
                }
            }
            while (true) {
                System.out.print("Enter end date (YYYY-MM-DD) > ");
                String input2 = scanner.nextLine();
                try {
                    endDate = parseDate(input2);
                    break;
                } catch (DateTimeParseException e) {
                    System.out.println("Please input a valid end date in the format YYYY-MM-DD!");
                }
            }
        }
        try {
            RoomRateEntity updated = roomRateEntitySessionBeanRemote.updateRoomRate(Long.valueOf(id), name, Long.valueOf(roomTypeId), selectedRateType, ratePerNight, startDate, endDate);
            String start, end;
            if (updated.getRateType() == RateType.PEAK || updated.getRateType() == RateType.PROMOTION) {
                start = updated.getStartDate().toString();
                end = updated.getEndDate().toString();
            } else {
                start = "NA";
                end = "NA";
            }
            System.out.println("*** Updated details of rate type: ***\n");
            System.out.printf("%-20s || %-20s || %-20s || %-20s || %-20s || %-20s%n", "Name", "Room Type", "Rate Type", "Rate per night", "Start Date", "End Date");
            System.out.printf("%-20s || %-20s || %-20s || %-20.2f || %-20s || %s%n",
                    updated.getName(),
                    updated.getRoomType().getRoomTypeName().toString(),
                    updated.getRateType().toString(),
                    updated.getRatePerNight(),
                    start,
                    end
            );
        } catch (RoomTypeNotFoundException | RoomRateNotFoundException ex) {
            System.out.println("Invalid update of room rate: " + ex.getMessage());
        }
    }

    private void deleteRoomRate() {
        Scanner scanner = new Scanner(System.in);

        // Display available room rates for selection
        viewAllRoomRates();
        System.out.print("Enter the ID of the room rate to delete > ");
        Long roomRateId = scanner.nextLong();
        scanner.nextLine();

        try {
            // Check if the room rate is currently in use
            boolean isRoomRateInUse = roomRateEntitySessionBeanRemote.isRoomRateInUse(roomRateId);

            if (isRoomRateInUse) {
                // If in use, mark as disabled instead of deleting
                roomRateEntitySessionBeanRemote.disableRoomRate(roomRateId);
                System.out.println("Room rate " + roomRateId + " is currently in use and has been marked as disabled.");
            } else {
                // If not in use, delete the room rate
                roomRateEntitySessionBeanRemote.deleteRoomRate(roomRateId);
                System.out.println("Room rate " + roomRateId + " has been successfully deleted.");
            }

        } catch (RoomRateNotFoundException ex) {
            System.out.println("Room rate not found: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("An error occurred while deleting the room rate: " + ex.getMessage());
        }
    }

    // Use case 21
    private void viewAllRoomRates() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HORS System :: Sales Manager :: View all Room Rates ");
        List<RoomRateEntity> roomRates = roomRateEntitySessionBeanRemote.viewAllRoomRates();
        Integer roomRateCount = 0;

        System.out.printf("%-20s || %-20s || %-30s || %-30s%n", "S/N", "Room Rate Id", "Room Rate Name", "Room Type Name");

        for (RoomRateEntity roomRate : roomRates) {
            roomRateCount++;
            System.out.printf("%-20d || %-20d || %-30s || %-30s%n", roomRateCount, roomRate.getRoomRateId(), roomRate.getName(), roomRate.getRoomType().getRoomTypeName().toString());
        }
    }

    // Used in createNewRoomRate()
    private void viewAllRoomTypes() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HORS System :: Operations Manager :: View all Room Types");
        List<RoomTypeEntity> roomTypes = roomTypeEntitySessionBeanRemote.viewAllRoomTypes();
        Integer roomTypeCount = 0;

        System.out.printf("%-20s || %-20s|| %-20s%n", "S/N", "Room Type Id", "Room Type Names");

        for (RoomTypeEntity roomType : roomTypes) {
            roomTypeCount++;
            System.out.printf("%-20d || %-20d || %-20s%n", roomTypeCount, roomType.getRoomTypeId(), roomType.getRoomTypeName().toString());
        }
    }

    // Used in createNewRoomRate()
    public static LocalDate parseDate(String dateStr) throws DateTimeParseException {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateStr, dateFormat);
    }
}
