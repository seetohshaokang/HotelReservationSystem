/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsmanagementclient;

import ejb.session.EmployeeEntitySessionBeanRemote;
import entity.EmployeeEntity;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeRole;
import util.exception.InvalidAccessRightException;

/**
 *
 * @author shaokangseetoh
 */
public class SystemAdministratorModule {
    
    private EmployeeEntitySessionBeanRemote employeeEntitySessionBean;
    
    // Insert PartnerEntitySessionBean Here
    
    private EmployeeEntity currentEmployee;
    
    public SystemAdministratorModule(){
        
    }
    
    // Constructor without partner operations
    public SystemAdministratorModule(EmployeeEntitySessionBeanRemote employeeEntitySessionBean, EmployeeEntity currentEmployee) {
        this.employeeEntitySessionBean = employeeEntitySessionBean;
        this.currentEmployee = currentEmployee;
    }
    
    public void menuSystemAdministrator() throws InvalidAccessRightException {
        
        if(currentEmployee.getRole() != EmployeeRole.SYSTEM_ADMINISTRATOR) {
            throw new InvalidAccessRightException (" You don't have SYSTEM ADMINISTRATOR rights to access the system administrator module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** HORS System :: System Administrator Module ***\n");
            System.out.println("1: Create a new employee");
            System.out.println("2: View all employees");
            System.out.println("-------------------------");
            System.out.println("3: Back\n");
            response = 0;
        
            while(response < 1 || response > 3) {
                
                System.out.print("> ");
                response = scanner.nextInt();
                
                if(response == 1){
                    doCreateEmployee();
                } else if(response == 2) {
                    doViewAllEmployees();
                } else if(response == 3){
                    break;
                } else {
                    System.out.println("Invalid Option, please try again!\n");
                }
            }
            
            if(response == 3){
                break;
            }
        }
    }
    
    private void doCreateEmployee() {
        Scanner scanner = new Scanner(System.in);
        EmployeeEntity newEmployeeEntity = new EmployeeEntity();
        
        System.out.println("*** HORS System :: System Administrator :: Create New Employee");
        
        System.out.print("Enter username > ");
        newEmployeeEntity.setUsername(scanner.nextLine().trim());
        
        System.out.print("Enter password > ");
        newEmployeeEntity.setPassword(scanner.nextLine().trim());
        
        
        while(true) {
            System.out.println("Select Access Right (1: System Administrator, 2: Operations Manager, 3: Sales Manager, 4: Guest Relations Officer");
            Integer accessRightInt = scanner.nextInt();
            
            if(accessRightInt >= 1 && accessRightInt <= 4) {
            
                newEmployeeEntity.setRole(EmployeeRole.values()[accessRightInt - 1]);
                break;
            } else {
            
                System.out.println("Invalid option, please try again!\n");
            }
        }
        
        scanner.nextLine();
        
        // System.out.print("Enter username > ");
        // newEmployeeEntity.setUsername(scanner.nextLine().trim());
        // System.out.print("Enter password > ");
        // newEmployeeEntity.setPassword(scanner.nextLine().trim());
        
        Long newEmployeeId = employeeEntitySessionBean.createNewEmployee(newEmployeeEntity);
        System.out.println("New employee created successfully!: " + newEmployeeId + "\n");
    }
    
    private void doViewAllEmployees() {
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HORS System :: System Administratro :: View All Employees");
        List<EmployeeEntity> employeeEntities = employeeEntitySessionBean.retrieveAllEmployees();
        
        System.out.printf("%10s%20s\n", "Username", "Role");
        
        for(EmployeeEntity employeeEntity: employeeEntities) {
        
            System.out.printf("%10s%20s\n", employeeEntity.getUsername(), employeeEntity.getRole().toString());
            
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
}
