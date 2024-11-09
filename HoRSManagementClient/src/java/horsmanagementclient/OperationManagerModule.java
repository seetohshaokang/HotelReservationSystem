package horsmanagementclient;

import ejb.session.RoomTypeEntitySessionBeanRemote;
import entity.EmployeeEntity;
import entity.RoomTypeEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeRole;
import util.enumeration.RoomTypeName;
import util.exception.InvalidAccessRightException;

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
    
    // State for login
    private EmployeeEntity currentEmployee;

    public OperationManagerModule() {
    }

    public OperationManagerModule(RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote) {
        this.roomTypeEntitySessionBeanRemote = roomTypeEntitySessionBeanRemote;
    }
   

    // insert constructor with the appropriate sessionbean
    public void menuOperationManager() throws InvalidAccessRightException {
        if (currentEmployee.getRole() != EmployeeRole.OPERATIONS_MANAGER) {
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

            while (response < 1 || response > 11) {

                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1) {
                    System.out.println("Feature not implemented yet");
                    createNewRoomType();
                } else if (response == 2) {
                    System.out.println("Feature not implemented yet");
                } else if (response == 3) {
                    System.out.println("Feature not implemented yet");
                } else if (response == 4) {
                    System.out.println("Feature not implemented yet");
                } else if (response == 5) {
                    System.out.println("Feature not implemented yet");
                } else if (response == 6) {
                    System.out.println("Feature not implemented yet");
                } else if (response == 7) {
                    System.out.println("Feature not implemented yet");
                } else if (response == 8) {
                    System.out.println("Feature not implemented yet");
                } else if (response == 9) {
                    System.out.println("Feature not implemented yet");
                } else if (response == 10) {
                    System.out.println("Feature not implemented yet");
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

    private void createNewRoomType() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        System.out.println("*** HORS System :: Operation Manager :: Create New Room Type ***\n");
        System.out.println("-------------------------------------------------------------------");

        while (true) {
            response = 0;
            System.out.println("Please Select Room Type");
            System.out.println("1: DELUXE");
            System.out.println("2: PREMIER");
            System.out.println("3: FAMILY");
            System.out.println("4: JUNIOR");
            System.out.println("5: GRAND");
            System.out.print("Enter room type number> ");
            response = scanner.nextInt();
            scanner.nextLine();

            if (response < 1 || response > 5) {
                System.out.println("Invalid option. Please select a number between 1 and 5.");
            } else {
                System.out.println("You have selected option " + response);
                break;
            }
        }
        RoomTypeName selectedRoomType = RoomTypeName.values()[response - 1];

        String description = "";
        while (true) {
            System.out.print("Enter description > ");
            if (scanner.hasNextLine()) {
                description = scanner.nextLine();
                break;
            } else {
                System.out.println("Please input a valid description");
            }
        }
        Double size = 0.0;
        while (true) {
            System.out.print("Enter size > ");
            if (scanner.hasNextDouble()) {
                size = scanner.nextDouble();
                scanner.nextLine();
                break;
            } else {
                System.out.println("Please input a valid size");
            }
        }
        String bed = "";
        while (true) {
            System.out.print("Enter bed > ");
            if (scanner.hasNextLine()) {
                bed = scanner.nextLine();
                break;
            } else {
                System.out.println("Please input a valid bed");
            }
        }
        Integer capacity = 0;
        while (true) {
            System.out.print("Enter capacity > ");
            if (scanner.hasNextInt()) {
                capacity = scanner.nextInt();
                scanner.nextLine();
                break;
            } else {
                System.out.println("Please input a valid capacity");
            }
        }
        List<String> amenities = new ArrayList<>();
        while(true) {
            System.out.print("Enter an amenity to add: ");
            String amenity = scanner.nextLine();
            amenities.add(amenity);
            
            System.out.print("Do you want to add another amenity? (Y/N): ");
            String reply = scanner.nextLine().trim().toLowerCase();
            
            if(!response.equals("y")) {
                break;
            } 
        }
        
        RoomTypeEntity newRoomType = new RoomTypeEntity(selectedRoomType, description, size, bed, capacity, amenities);
        
        String newRoomTypeName = roomTypeEntitySessionBeanRemote.createNewRoomType(newRoomType);
        System.out.println("You have created a new room type: " + newRoomTypeName);
    }

}
