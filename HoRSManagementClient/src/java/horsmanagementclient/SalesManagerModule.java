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
 * @author bryan
 */
public class SalesManagerModule {

    // Insert relevant sessionbeans
    private EmployeeEntity currentEmployee;
    
    public SalesManagerModule() {
    }
    
    // Insert constructor with appropriate sessionbean
    
    public void menuSalesManager() throws InvalidAccessRightException {
        
        if(currentEmployee.getRole() != EmployeeRole.SALES_MANAGER) {
            throw new InvalidAccessRightException("You don't have SALES MANAGER rights to access the sales manager module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** HORS System :: Sales Manager Module ***\n");
            System.out.println("1: Create New Room Rate");
            System.out.println("2: View Room Rate Details");
            System.out.println("3: Update Room Rate");
            System.out.println("4: Delete Room Rate");
            System.out.println("5: View all Room Rates");
            System.out.println("---------------------------");
            System.out.println("6: Back\n");
            
            while(response < 1 || response > 6) {
            
                System.out.print("> ");
                response = scanner.nextInt();
                
                if(response == 1){
                    System.out.println("Feature not implemented yet");
                    // create new room rate
                } else if (response == 2){
                    System.out.println("Feature not implemented yet");
                } else if (response == 3){
                    System.out.println("Feature not implemented yet");
                } else if(response == 4){
                    System.out.println("Feature not implemented yet");
                } else if(response == 5){
                    System.out.println("Feature not implemented yet");
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
    
    
    
}
