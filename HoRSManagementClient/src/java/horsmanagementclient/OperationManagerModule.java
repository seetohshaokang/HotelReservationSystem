package horsmanagementclient;

import ejb.session.RoomEntitySessionBeanRemote;
import ejb.session.RoomTypeEntitySessionBeanRemote;
import ejb.session.stateless.helper.ExceptionReportSessionBeanRemote;
import entity.EmployeeEntity;
import entity.ExceptionReportEntity;
import entity.PartnerEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeRole;
import util.enumeration.RoomStatus;
import util.enumeration.RoomTypeName;
import util.exception.DisabledException;
import util.exception.ExistingRoomException;
import util.exception.InvalidAccessRightException;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeNotFoundException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author shaokangseetoh
 */
public class OperationManagerModule {

    // Insert relevant sessionbeans
    private RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;
    private RoomEntitySessionBeanRemote roomEntitySessionBeanRemote;
    private ExceptionReportSessionBeanRemote exceptionReportSessionBean;

    // State for login
    private EmployeeEntity currentEmployee;

    public OperationManagerModule() {
    }

    public OperationManagerModule(RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote, RoomEntitySessionBeanRemote roomEntitySessionBeanRemote, ExceptionReportSessionBeanRemote exceptionReportSessionBean, EmployeeEntity currentEmployee) {
        this.roomTypeEntitySessionBeanRemote = roomTypeEntitySessionBeanRemote;
        this.roomEntitySessionBeanRemote = roomEntitySessionBeanRemote;
        this.exceptionReportSessionBean = exceptionReportSessionBean;
        this.currentEmployee = currentEmployee;
    }

    // insert constructor with the appropriate sessionbean
    public void menuOperationManager() throws InvalidAccessRightException {
        if (currentEmployee.getRole() != EmployeeRole.OPERATION_MANAGER) {
            throw new InvalidAccessRightException("You don't have OPERATIONS MANAGER rights to access the system administator module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** HORS System :: Operation Manager Module ***\n");
            System.out.println("1: Create New Room Type");
            System.out.println("2: View Room Type Details");
            System.out.println("3: Update Room Type Details");
            System.out.println("4: Delete Room Type");
            System.out.println("5: View all Room Types");
            System.out.println("---------------------------");
            System.out.println("6: Create new room");
            System.out.println("7: Update a room");
            System.out.println("8: Delete a room");
            System.out.println("9: View all rooms");
            System.out.println("---------------------------");
            System.out.println("10: View Room Allocation Report");
            System.out.println("---------------------------");
            System.out.println("11: Back\n");
            response = 0;

            while (response < 1 || response > 11) {

                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1) {
                    createNewRoomType();
                } else if (response == 2) {
                    viewRoomTypeDetails();
                } else if (response == 3) {
                    updateRoomTypeDetails();
                } else if (response == 4) {
                    System.out.println("Feature not implemented yet");
                } else if (response == 5) {
                    viewAllRoomTypes();
                } else if (response == 6) {
                    createNewRoom();
                } else if (response == 7) {
                    updateRoomDetails();
                } else if (response == 8) {
                    System.out.println("Feature not implemented yet");
                } else if (response == 9) {
                    viewAllRooms();
                } else if (response == 10) {
                    viewRoomAllocationExceptionReport();
                } else if (response == 11) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 11) {
                break;
            }
        }
    }

    // Use case 7
    private void createNewRoomType() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        System.out.println("*** HORS System :: Operation Manager :: Create New Room Type ***\n");
        System.out.println("-------------------------------------------------------------------");

        // Select the room type name
        RoomTypeName selectedRoomType = null;
        while (true) {
            response = 0;
            System.out.println("Please Select Room Type");
            int count = 0;
            for (RoomTypeName roomTypeName : RoomTypeName.values()) {
                count++;
                System.out.println(count + ". " + roomTypeName);
            }
            System.out.print("Enter room type number> ");
            response = scanner.nextInt();
            scanner.nextLine();

            if (response < 1 || response > RoomTypeName.values().length) {
                System.out.println("Invalid option. Please select a valid room type number.");
            } else {
                selectedRoomType = RoomTypeName.values()[response - 1];
                System.out.println("You have selected room type: " + selectedRoomType);
                break;
            }
        }

        // Select the next higher room type name
        RoomTypeName nextHigherRoomTypeName = null;
        System.out.println("\nDo you want to specify a next higher room type for this room type? (Y/N)");
        String reply = scanner.nextLine().trim();
        if (reply.equalsIgnoreCase("Y")) {
            while (true) {
                System.out.println("Please Select Next Higher Room Type");
                int count = 0;
                for (RoomTypeName roomTypeName : RoomTypeName.values()) {
                    count++;
                    System.out.println(count + ". " + roomTypeName);
                }
                System.out.print("Enter next higher room type number> ");
                response = scanner.nextInt();
                scanner.nextLine();

                if (response < 1 || response > RoomTypeName.values().length) {
                    System.out.println("Invalid option. Please select a valid room type number.");
                } else {
                    nextHigherRoomTypeName = RoomTypeName.values()[response - 1];
                    System.out.println("Next higher room type set to: " + nextHigherRoomTypeName);
                    break;
                }
            }
        }

        // Enter description
        System.out.print("Enter description > ");
        String description = scanner.nextLine();

        // Enter size
        Double size = null;
        while (size == null) {
            System.out.print("Enter size > ");
            if (scanner.hasNextDouble()) {
                size = scanner.nextDouble();
                scanner.nextLine();
            } else {
                System.out.println("Please input a valid size.");
                scanner.nextLine();
            }
        }

        // Enter bed type
        System.out.print("Enter bed type > ");
        String bed = scanner.nextLine();

        // Enter capacity
        Integer capacity = null;
        while (capacity == null) {
            System.out.print("Enter capacity > ");
            if (scanner.hasNextInt()) {
                capacity = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Please input a valid capacity.");
                scanner.nextLine();
            }
        }

        // Enter amenities
        List<String> amenities = new ArrayList<>();
        while (true) {
            System.out.print("Enter an amenity to add: ");
            String amenity = scanner.nextLine();
            amenities.add(amenity);

            System.out.print("Do you want to add another amenity? (Y/N): ");
            reply = scanner.nextLine().trim().toLowerCase();
            if (!reply.equalsIgnoreCase("y")) {
                break;
            }
        }

        // Instantiate the RoomTypeEntity with the new field
        RoomTypeEntity newRoomType = new RoomTypeEntity(selectedRoomType, nextHigherRoomTypeName, description, size, bed, capacity, amenities);

        // Call Session Bean to persist
        String newRoomTypeName = roomTypeEntitySessionBeanRemote.createNewRoomType(newRoomType);
        System.out.println("You have successfully created a new room type: " + newRoomTypeName);
    }

    // Use case 8
    private void viewRoomTypeDetails() {
        Scanner scanner = new Scanner(System.in);
        viewAllRoomTypes(); // Show the list of roomtypes
        Integer response = 0;
        System.out.print("Enter S/N of a displayed room type to view more details > ");
        response = scanner.nextInt();
        scanner.nextLine();
        RoomTypeName selectedRoomType = RoomTypeName.values()[response - 1];
        // Call session bean
        try {
            RoomTypeEntity roomType = roomTypeEntitySessionBeanRemote.getRoomTypeByName(selectedRoomType);
            System.out.printf("%-20s || %-20s || %-20s || %-20s || %-40s%n", "Description", "Size", "Bed", "Capacity", "Amenities");
            System.out.printf("%-20s || %-20.2f || %-20s || %-20d || %-40s%n", roomType.getDescription(), roomType.getSize(), roomType.getBed(), roomType.getCapacity(), roomType.getAmenities().toString());
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Invalid room type: " + ex.getMessage());
        }
    }

    // Use case 9
    private void updateRoomTypeDetails() {
        Scanner scanner = new Scanner(System.in);
        viewAllRoomTypes(); // Display all room types to the user

        System.out.print("Enter room type ID of the room type you wish to update > ");
        Long roomTypeId = scanner.nextLong();
        scanner.nextLine();

        String newDescription;
        System.out.print("Enter description > ");
        newDescription = scanner.nextLine();

        Double newSize = null;
        while (newSize == null) {
            System.out.print("Enter size > ");
            if (scanner.hasNextDouble()) {
                newSize = scanner.nextDouble();
                scanner.nextLine();
            } else {
                System.out.println("Please input a valid size!");
                scanner.nextLine(); // Consume invalid input
            }
        }

        System.out.print("Enter bed > ");
        String newBed = scanner.nextLine();

        Integer newCapacity = null;
        while (newCapacity == null) {
            System.out.print("Enter capacity > ");
            if (scanner.hasNextInt()) {
                newCapacity = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Please input a valid capacity!");
                scanner.nextLine(); // Consume invalid input
            }
        }

        List<String> newAmenities = new ArrayList<>();
        while (true) {
            System.out.print("Enter an amenity to add: ");
            String amenity = scanner.nextLine();
            newAmenities.add(amenity);

            System.out.print("Do you want to add another amenity? (Y/N): ");
            String reply = scanner.nextLine().trim().toLowerCase();
            if (!reply.equalsIgnoreCase("y")) {
                break;
            }
        }

        // Prompt for the next higher room type name
        RoomTypeName newNextHigherRoomTypeName = null;
        System.out.print("\nDo you want to specify a next higher room type? (Y/N): ");
        String reply = scanner.nextLine().trim();
        if (reply.equalsIgnoreCase("Y")) {
            System.out.println("Select Next Higher Room Type:");
            int count = 0;
            for (RoomTypeName roomTypeName : RoomTypeName.values()) {
                count++;
                System.out.println(count + ". " + roomTypeName);
            }
            System.out.print("Enter next higher room type number > ");
            int selectedType = scanner.nextInt();
            scanner.nextLine();

            if (selectedType >= 1 && selectedType <= RoomTypeName.values().length) {
                newNextHigherRoomTypeName = RoomTypeName.values()[selectedType - 1];
                System.out.println("Next higher room type set to: " + newNextHigherRoomTypeName);
            } else {
                System.out.println("Invalid selection. No next higher room type set.");
            }
        }

        // Call session bean to update room type
        try {
            RoomTypeEntity roomTypeUpdated = roomTypeEntitySessionBeanRemote.updateRoomType(
                    roomTypeId,
                    newDescription,
                    newSize,
                    newBed,
                    newCapacity,
                    newAmenities,
                    newNextHigherRoomTypeName);

            System.out.println("\n*** Updated details of room type: ***");
            System.out.printf("%-20s || %-20s || %-20s || %-20s || %-20s || %-20s%n",
                    "Description", "Size", "Bed", "Capacity", "Amenities", "Next Higher Room Type");
            System.out.printf("%-20s || %-20.2f || %-20s || %-20d || %-20s || %-20s%n",
                    roomTypeUpdated.getDescription(),
                    roomTypeUpdated.getSize(),
                    roomTypeUpdated.getBed(),
                    roomTypeUpdated.getCapacity(),
                    roomTypeUpdated.getAmenities().toString(),
                    roomTypeUpdated.getNextHigherRoomTypeName() != null ? roomTypeUpdated.getNextHigherRoomTypeName().toString() : "None");

        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Invalid Room Type Selected: " + ex.getMessage());
        }

    }

    private void viewRoomAllocationExceptionReport() {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.out.println("*** HORS System :: Operation Manager :: View Room Allocation Exception Report ***\n");

        LocalDate reportDate = null;

        // Prompt user to enter the date for which they want to view the exception report
        while (reportDate == null) {
            System.out.print("Enter the date for the report (yyyy-MM-dd): ");
            String inputDate = scanner.nextLine().trim();
            try {
                reportDate = LocalDate.parse(inputDate, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
            }
        }

        // Call the session bean to retrieve the exception reports for the specified date
        List<ExceptionReportEntity> exceptionReports = exceptionReportSessionBean.generateExceptionReport(reportDate);

        if (exceptionReports.isEmpty()) {
            System.out.println("No exception reports found for " + reportDate + ".");
        } else {
            System.out.println("\nException Reports for " + reportDate + ":");
            System.out.printf("%-10s | %-50s%n", "Report ID", "Exception Message");
            System.out.println("--------------------------------------------------------");

            // Display each exception report's details
            for (ExceptionReportEntity report : exceptionReports) {
                System.out.printf("%-10d | %-50s%n", report.getExceptionReportId(), report.getExceptionMessage());
            }
        }
    }
    // Use case 11
    private void viewAllRoomTypes() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HORS System :: Operations Manager :: View all Room Types");
        List<RoomTypeEntity> roomTypes = roomTypeEntitySessionBeanRemote.viewAllRoomTypes();
        Integer roomTypeCount = 0;
        System.out.printf("%-20s || %-20s || %-20s%n", "S/N", "Room Type Id", "Room Type Names");

        for (RoomTypeEntity roomType : roomTypes) {
            roomTypeCount++;
            System.out.printf("%-20d || %-20d || %-20s%n", roomTypeCount, roomType.getRoomTypeId(), roomType.getRoomTypeName().toString());
        }
    }

    // Use case 12
    private void createNewRoom() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HORS System :: Operation Manager :: Create New Room ***\n");
        System.out.println("-------------------------------------------------------------------");
        // Input Room Type
        viewAllRoomTypes();
        Integer response = 0;
        System.out.println("Enter the S/N of room type you which to create a new room type for> ");
        response = scanner.nextInt();
        scanner.nextLine();
        RoomTypeName selectedRoomType = RoomTypeName.values()[response - 1];
        // Input floor
        Integer floor = 0;
        while (true) {
            System.out.print("Enter Floor Number > ");
            if (scanner.hasNextInt()) {
                floor = scanner.nextInt();
                scanner.nextLine();
                break;
            } else {
                System.out.println("Please input a valid Floor Number");
            }
        }
        // Input sequence
        Integer sequence = 0;
        while (true) {
            System.out.print("Enter Room Sequence Number > ");
            if (scanner.hasNextInt()) {
                sequence = scanner.nextInt();
                scanner.nextLine();
                break;
            } else {
                System.out.println("Please input a valid Room Sequence Number");
            }
        }
        // Input Room Status
        while (true) {
            response = 0;
            System.out.println("Please Select Room Status");
            System.out.println("1: AVAILABLE");
            System.out.println("2: NOT_AVAILABLE");
            System.out.print("Enter room status number> ");
            response = scanner.nextInt();
            scanner.nextLine();

            if (response < 1 || response > 2) {
                System.out.println("Invalid option. Please select a number between 1 and 5.");
            } else {
                System.out.println("You have selected option " + response);
                break;
            }
        }
        RoomStatus selectedRoomStatus = RoomStatus.values()[response - 1];
        try {
            RoomEntity newRoom = roomEntitySessionBeanRemote.createNewRoom(selectedRoomType, floor, sequence, selectedRoomStatus);
            System.out.println("New room has been created with room type "
                    + newRoom.getRoomType().getRoomTypeName().toString()
                    + " and room number: " + newRoom.getRoomNumber());
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Invalid creation of new room: " + ex.getMessage());
        } catch (ExistingRoomException ex) {
            System.out.println("Invalid creation of new room: " + ex.getMessage());
        } catch (DisabledException ex) {
            System.out.println("Invalid creation of new room: " + ex.getMessage());
        }
    }

    // Use case 13
    private void updateRoomDetails() {
        Scanner scanner = new Scanner(System.in);
        viewAllRooms();
        Integer roomId = 0;
        System.out.print("Enter room Id of the room you wish to update > ");
        roomId = scanner.nextInt();
        scanner.nextLine();
        Integer response = 0;
        Integer roomTypeNum = 0;
        Integer roomTypeId = 0;
        // Get room type
        while (true) {
            // Print out all the roomType names --> CHANGE TO AVAILABLE ROOMTYPES
            int count = 1;
            System.out.println("Please Select Room Type");
            viewAllRoomTypes();
            System.out.print("Enter room type id> ");
            if (scanner.hasNextInt()) {
                roomTypeId = scanner.nextInt();
                scanner.nextLine();
                break;
            } else {
                System.out.println("Please select a valid room type!");
            }
        }
        // Get room number
        String newRoomNumber = "";
        while (true) {
            System.out.print("Enter room number > ");
            if (scanner.hasNextLine()) {
                newRoomNumber = scanner.nextLine();
                break;
            } else {
                System.out.println("Please input a valid room number");
            }
        }
        // Get room status
        while (true) {
            response = 0;
            System.out.println("Please Select Room Status");
            // Change to dynamic down the line
            System.out.println("1: AVAILABLE");
            System.out.println("2: NOT_AVAILABLE");
            System.out.print("Enter room status number> ");
            response = scanner.nextInt();
            scanner.nextLine();

            if (response < 1 || response > 2) {
                System.out.println("Invalid option. Please select a number between 1 and 5.");
            } else {
                System.out.println("You have selected option " + response);
                break;
            }
        }
        RoomStatus selectedRoomStatus = RoomStatus.values()[response - 1];
        try {
            roomEntitySessionBeanRemote.updateRoom(Long.valueOf(roomId), Long.valueOf(roomTypeId), newRoomNumber, selectedRoomStatus);
            System.out.println("You have updated the room details successfully");
        } catch (RoomNotFoundException ex) {
            System.out.println("Error updating room: " + ex.getMessage());
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Error updating room: " + ex.getMessage());
        }
    }

    // Use case 15
    private void viewAllRooms() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HORS System :: Operations Manager :: View all Room ");
        List<RoomEntity> rooms = roomEntitySessionBeanRemote.viewAllRooms();
        // Print rooms
        Integer roomTypeCount = 0;
        System.out.printf("%-20s || %-20s || %-20s || %-20s%n", "S/N", "Room Id", "Room Type", "Room Number");

        for (RoomEntity room : rooms) {
            roomTypeCount++;
            System.out.printf("%-20d || %-20d || %-20s || %-20s%n", roomTypeCount, room.getRoomId(), room.getRoomType().getRoomTypeName().toString(), room.getRoomNumber());
        }
    }

}
