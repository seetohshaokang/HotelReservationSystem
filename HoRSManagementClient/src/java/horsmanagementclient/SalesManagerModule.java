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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.text.ParseException;
import util.enumeration.EmployeeRole;
import util.enumeration.RateType;
import util.enumeration.RoomTypeName;
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
        Integer response = 0;

        while (true) {
            System.out.println("*** HORS System :: Sales Manager Module ***\n");
            System.out.println("1: Create New Room Rate");
            System.out.println("2: View Room Rate Details");
            System.out.println("3: Update Room Rate");
            System.out.println("4: Delete Room Rate");
            System.out.println("5: View all Room Rates");
            System.out.println("---------------------------");
            System.out.println("6: Back\n");

            while (response < 1 || response > 6) {

                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1) {
                    createNewRoomRate();
                } else if (response == 2) {
                    viewRoomRateDetails();
                } else if (response == 3) {
                    //updateRoomRateDetails();
                } else if (response == 4) {
                    //deleteRoomRate();
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
        Integer response = 0;
        System.out.println("*** HORS System :: Sales Manager :: Create New Room Rate ***\n");
        System.out.println("-------------------------------------------------------------------");
        // Input name
        String name = "";
        while (true) {
            System.out.print("Enter name > ");
            if (scanner.hasNextLine()) {
                name = scanner.nextLine();
                break;
            } else {
                System.out.println("Please input a valid name!");
            }
        }
        // Input Room Type
        viewAllRoomTypes();
        Integer roomType = 0;
        System.out.println("Enter the S/N of room type you wish to create a new room rate for> ");
        roomType = scanner.nextInt();
        scanner.nextLine();
        RoomTypeName selectedRoomType = RoomTypeName.values()[response - 1];

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
            if (scanner.hasNextInt()) {
                ratePerNight = scanner.nextInt();
                scanner.nextLine();
                break;
            } else {
                System.out.println("Please input a valid Rate per night!");
            }
        }
        Date startDate = null;
        Date endDate = null;
        // Input start date and end date (For peak and promotion rate types only)
        if (response == 3 || response == 4) {
            while (true) {
                System.out.print("Enter start date (YYYY-MM-DD) > ");
                String input = scanner.nextLine();
                try {
                    startDate = parseDate(input);
                    break;
                } catch (ParseException e) {
                    System.out.println("Please input a valid start date in the format YYYY-MM-DD!");
                }
            }
            while (true) {
                System.out.print("Enter end date (YYYY-MM-DD) > ");
                String input2 = scanner.nextLine();
                try {
                    endDate = parseDate(input2);
                    break;
                } catch (ParseException e) {
                    System.out.println("Please input a valid end date in the format YYYY-MM-DD!");
                }
            }

            try {
                String newRateName = roomRateEntitySessionBeanRemote.createNewRoomRate(name, selectedRoomType, selectedRateType, ratePerNight, startDate, endDate);
                System.out.println("New room rate has been created: " + newRateName);
            } catch (RoomTypeNotFoundException ex) {
                System.out.println("Invalid creation of new room rate: "+ ex.getMessage());
            }
        }
    }

    // Use case 18
    private void viewRoomRateDetails() {
        Scanner scanner = new Scanner(System.in);
        viewAllRoomRates(); // Show the list of roomrates
        Integer response = 0;
        System.out.print("Enter ID of a displayed room rate to view more details > ");
        Long roomRateId = scanner.nextLong();
        scanner.nextLine();
        // Call session bean
        try {
            RoomRateEntity roomRate = roomRateEntitySessionBeanRemote.getRoomRateById(roomRateId);
            System.out.printf("%s || %s || %s || %s || %s || %s || %s%n", "Room Rate Id", "Room Rate Name", "Room Type", "Rate Type", "Rate per night", "Start Date", "End Date");
            System.out.printf("%d || %s || %s || %s || %f || %s || %s%n", roomRate.getRoomRateId(), roomRate.getName(), roomRate.getRoomType().toString(), roomRate.getRateType().toString(), roomRate.getRatePerNight(), roomRate.getStartDate().toString(), roomRate.getEndDate().toString());
        } catch (RoomRateNotFoundException ex) {
            System.out.println("Invalid room type: " + ex.getMessage());
        }
    }

    private void updateRoomRateDetails() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void deleteRoomRate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Use case 21
    private void viewAllRoomRates() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HORS System :: Sales Manager :: View all Room Rates ");
        List<RoomRateEntity> roomRates = roomRateEntitySessionBeanRemote.viewAllRoomRates();
        Integer roomRateCount = 0;

        System.out.printf("%s || %s || %s || %s || %s || %s || %s || %s%n", "S/N", "Room Rate Id", "Room Rate Name", "Room Type", "Rate Type", "Rate per night", "Start Date", "End Date");

        for (RoomRateEntity roomRate : roomRates) {
            roomRateCount++;
            System.out.printf("%d || %d || %s || %s || %s || %f || %s || %s%n", roomRateCount, roomRate.getRoomRateId(), roomRate.getName(), roomRate.getRoomType().toString(), roomRate.getRateType().toString(), roomRate.getRatePerNight(), roomRate.getStartDate().toString(), roomRate.getEndDate().toString());
        }
    }

    // Used in createNewRoomRate()
    private void viewAllRoomTypes() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HORS System :: Operations Manager :: View all Room Types");
        List<RoomTypeEntity> roomTypes = roomTypeEntitySessionBeanRemote.viewAllRoomTypes();
        Integer roomTypeCount = 0;

        System.out.printf("%s || %s|| %s%n", "S/N", "Room Type Id", "Room Type Names");

        for (RoomTypeEntity roomType : roomTypes) {
            roomTypeCount++;
            System.out.printf("%d || %d || %s%n", roomTypeCount, roomType.getRoomTypeId(), roomType.getName().toString());
        }
    }

    // Used in createNewRoomRate()
    public static Date parseDate(String dateStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);  // Ensures strict parsing
        return dateFormat.parse(dateStr);
    }
}
