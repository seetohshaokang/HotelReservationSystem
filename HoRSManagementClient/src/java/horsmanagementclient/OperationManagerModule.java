package horsmanagementclient;

import entity.EmployeeEntity;
import java.util.Scanner;
import util.enumeration.EmployeeRole;
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
    private EmployeeEntity currentEmployee;
    
    
    public OperationManagerModule() {
    }
    
    // insert constructor with the appropriate sessionbean
    
    
    
    public void menuOperationManager() throws InvalidAccessRightException {
        if(currentEmployee.getRole() != EmployeeRole.OPERATIONS_MANAGER) {
            throw new InvalidAccessRightException("You don't have OPERATIONS MANAGER rights to access the system administator module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
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
            
            while(response < 1 || response > 11) {
            
                System.out.print("> ");
                response = scanner.nextInt();
                
                if(response == 1){
                    System.out.println("Feature not implemented yet");
                    // create new room type
                } else if (response == 2){
                    System.out.println("Feature not implemented yet");
                } else if (response == 3){
                    System.out.println("Feature not implemented yet");
                } else if(response == 4){
                    System.out.println("Feature not implemented yet");
                } else if(response == 5){
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
    
}
