/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsmanagementclient;

import entity.EmployeeEntity;
import java.util.Scanner;
import util.enumeration.EmployeeRole;
import util.exception.InvalidAccessRightException;

/**
 *
 * @author shaokangseetoh
 */
public class GuestRelationOfficerModule {

    // Insert relevant sessionbeans;
    private EmployeeEntity currentEmployee;
    
    public GuestRelationOfficerModule() {
    }
    
    // Insert constructor with appropriate sessionbean
    
    public void menuGuestRelationOfficer() throws InvalidAccessRightException {
        
        if(currentEmployee.getRole() != EmployeeRole.GUEST_RELATION_OFFICER) {
            throw new InvalidAccessRightException("You dont' have GUEST RELATION OFFICER rights to access the guest relation officer module.");
           
        }
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** HORS System :: Guest Relation Officer Module ***\n");
            System.out.println("1: Walk-in Search Room");
            System.out.println("2: Walk-in Reserve Room");
            System.out.println("3: Check-in Guest");
            System.out.println("4: Check-out Guest");
            System.out.println("---------------------------");
            System.out.println("5: Back\n");
            
            while(response < 1 || response > 5) {
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
    
}
