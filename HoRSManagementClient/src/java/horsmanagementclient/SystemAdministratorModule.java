/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsmanagementclient;

import ejb.session.EmployeeEntitySessionBeanRemote;
import ejb.session.PartnerEntitySessionBeanRemote;
import entity.EmployeeEntity;
import entity.PartnerEntity;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeRole;
import util.exception.EmployeeExistException;
import util.exception.InvalidAccessRightException;
import util.exception.PartnerExistException;

/**
 *
 * @author shaokangseetoh
 */
public class SystemAdministratorModule {

    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    private PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote;
    private EmployeeEntity currentEmployee;

    public SystemAdministratorModule() {

    }

    // Constructor 
    public SystemAdministratorModule(
            EmployeeEntitySessionBeanRemote employeeEntitySessionBean,
            PartnerEntitySessionBeanRemote partnerEntitySessionBean,
            EmployeeEntity currentEmployee) {
        this.employeeEntitySessionBeanRemote = employeeEntitySessionBean;
        this.partnerEntitySessionBeanRemote = partnerEntitySessionBean;
        this.currentEmployee = currentEmployee;
    }

    public void menuSystemAdministrator() throws InvalidAccessRightException {

        if (currentEmployee.getRole() != EmployeeRole.SYSTEM_ADMINISTRATOR) {
            throw new InvalidAccessRightException(" You don't have SYSTEM ADMINISTRATOR rights to access the system administrator module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** HORS System :: System Administrator Module ***\n");
            System.out.println("1: Create new employee");
            System.out.println("2: View all employees");
            System.out.println("-------------------------");
            System.out.println("3: Create new partner");
            System.out.println("4: View all partners");
            System.out.println("-------------------------");
            System.out.println("5: Back\n");

            System.out.print("> ");
            try {
                response = scanner.nextInt();

                if (response == 1) {
                    doCreateEmployee();
                } else if (response == 2) {
                    doViewAllEmployees();
                } else if (response == 3) {
                    doCreateNewPartner();
                } else if (response == 4) {
                    doViewAllPartners();
                } else if (response == 5) {
                    System.out.println("Returning to main menu...");
                    return; // Exit the method
                } else {
                    System.out.println("Invalid option, please enter a number between 1 and 5.\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please enter a number between 1 and 5.\n");
                scanner.next(); // Clear the invalid input
            }
        }

    }

    private void doCreateEmployee() {
        Scanner scanner = new Scanner(System.in);
        EmployeeEntity newEmployee = new EmployeeEntity();

        System.out.println("*** HORS System :: System Administrator :: Create New Employee");

        System.out.print("Enter username > ");
        newEmployee.setUsername(scanner.nextLine().trim());

        System.out.print("Enter password > ");
        newEmployee.setPassword(scanner.nextLine().trim());

        while (true) {
            System.out.println("Select Access Right (1: System Administrator, 2: Operations Manager, 3: Sales Manager, 4: Guest Relations Officer");
            Integer accessRightInt = scanner.nextInt();

            if (accessRightInt >= 1 && accessRightInt <= 4) {

                newEmployee.setRole(EmployeeRole.values()[accessRightInt - 1]);
                break;
            } else {

                System.out.println("Invalid option, please try again!\n");
            }
        }

        scanner.nextLine();

        // System.out.print("Enter username > ");
        // newEmployee.setUsername(scanner.nextLine().trim());
        // System.out.print("Enter password > ");
        // newEmployee.setPassword(scanner.nextLine().trim());
        try {
        Long newEmployeeId = employeeEntitySessionBeanRemote.createNewEmployee(newEmployee);
        System.out.println("New employee created successfully!: " + newEmployee.getUsername().toString() + "\n");
        } catch (EmployeeExistException e) {
            System.out.println("Error creating employee: " + e.getMessage());
        }
    }

    private void doViewAllEmployees() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HORS System :: System Administrator :: View All Employees");
        List<EmployeeEntity> employeeEntities = employeeEntitySessionBeanRemote.retrieveAllEmployees();

        System.out.printf("%10s%30s\n", "Username", "Role");

        for (EmployeeEntity employeeEntity : employeeEntities) {
            System.out.printf("%10s%30s\n", employeeEntity.getUsername(), employeeEntity.getRole().toString());
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doCreateNewPartner() {

        Scanner scanner = new Scanner(System.in);
        PartnerEntity newPartner = new PartnerEntity();

        System.out.println("*** HORS System :: System Administrator :: Create new partner");

        System.out.print("Enter username > ");
        newPartner.setUsername(scanner.nextLine().trim());

        System.out.print("Enter password > ");
        newPartner.setPassword(scanner.nextLine().trim());

        try { Long newPartnerId = partnerEntitySessionBeanRemote.createNewPartner(newPartner);
        System.out.println("New partner created successfully!: " + newPartner.getUsername().toString() + "\n");
        } catch (PartnerExistException ex) {
            System.out.println("Error creating partner: " + ex.getMessage());
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doViewAllPartners() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HORS System :: System Administrator :: View all Partners");
        List<PartnerEntity> partnerEntities = partnerEntitySessionBeanRemote.viewAllPartners();

        if (!partnerEntities.isEmpty()) {
            System.out.printf("%10s\n", "Username");
            for (PartnerEntity partnerEntity : partnerEntities) {
                System.out.printf("%10s\n", partnerEntity.getUsername());
            }
        } else {
            System.out.println("No partners created yet.");
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
}
