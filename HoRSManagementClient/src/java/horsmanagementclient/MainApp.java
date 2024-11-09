package horsmanagementclient;

import entity.EmployeeEntity;
import java.util.Scanner;
import ejb.session.EmployeeEntitySessionBeanRemote;
import ejb.session.PartnerEntitySessionBeanRemote;
import ejb.session.RoomTypeEntitySessionBeanRemote;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidLoginCredentialException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author shaokangseetoh
 */
public class MainApp {

    // Session Beans
    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    private PartnerEntitySessionBeanRemote partnerEntitySessionBean;
    private RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;

    // Modules for construction later
    private SystemAdministratorModule systemAdminModule;
    private OperationManagerModule operationManagerModule;

    // Employee States
    private EmployeeEntity currentEmployee;

    public MainApp() {
    }

    public MainApp(EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote, PartnerEntitySessionBeanRemote partnerEntitySessionBean, RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote) {
        this.employeeEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
        this.partnerEntitySessionBean = partnerEntitySessionBean;
        this.roomTypeEntitySessionBeanRemote = roomTypeEntitySessionBeanRemote;
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to HORS - Management System ***");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        if (this.employeeEntitySessionBeanRemote == null) {
                            System.out.print("Bean in missing in RUN APP");
                        }
                        doLogin();
                        System.out.println("Login Successful");
                        systemAdminModule = new SystemAdministratorModule(employeeEntitySessionBeanRemote, partnerEntitySessionBean, currentEmployee);
                        // Create operation manager module
                        // Create sales manager module
                        // Create guest relation officer module
                        menuMain();
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid Login Credentials: " + ex.getMessage() + "\n");

                    }
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid Option, please try again!\n");
                }

            }
            if (response == 2) {
                break;
            }
        }
    }

    private void doLogin() throws InvalidLoginCredentialException { // throws missing credentials and username does not exist
        Scanner scanner = new Scanner(System.in);
        String username;
        String password = "";

        System.out.println("*** Management System :: Login ***\n");
        System.out.print("Enter username > ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password > ");
        password = scanner.nextLine().trim();
        System.out.println("DEBUG");
        if (this.employeeEntitySessionBeanRemote == null) {
            System.out.print("Bean in missing in DO LOGIN");
        }

        if (username.length() > 0 && password.length() > 0) {
            checkEmployeeSessionBeanInDoLogin(employeeEntitySessionBeanRemote);
            currentEmployee = employeeEntitySessionBeanRemote.employeeLogin(username, password);
            System.out.println("DEBUG");
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** HORS - Management System ***\n");
            System.out.println("You are login as " + currentEmployee.getUsername() + " with " + currentEmployee.getRole().toString() + " rights\n");

            System.out.println("1. System Administrator Operations");
            System.out.println("2. Operation Manager Operations");
            System.out.println("3. Sales Manager Operations");
            System.out.println("4. Guest Relation Officer Operations");
            System.out.println("5. Logout\n");
            response = 0;

            while (response < 1 || response > 5) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {

                    try {
                        systemAdminModule.menuSystemAdministrator();
                    } catch (InvalidAccessRightException ex) {
                        System.out.println("Invalid option, please try again!:" + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    // invoke module.method() in try catch
                    System.out.println("Operation Manager Module is not implemented yet!");
                    System.out.println(" ");
                } else if (response == 3) {
                    // invoke module.method() in try catch
                    System.out.println("Sales Manager Module is not implemented yet!");
                    System.out.println(" ");
                } else if (response == 4) {
                    // invoke module.method() in try catch
                    System.out.println("Guest Relation Officer Module is not implemented yet!");
                    System.out.println(" ");
                } else if (response == 5) {
                    break; // Break inner while loop
                } else {
                    System.out.println("Invalid option, please try again!\n");
                    System.out.println(" ");
                }
            }

            if (response == 5) {
                break; // Break outer while loop
            }
        }
    }

    private void checkEmployeeSessionBeanInDoLogin(EmployeeEntitySessionBeanRemote test) {
        if (test == null) {
            System.out.println("Dependency injection failed for employeeEntitySessionBeanRemote INSIDE DO LOGIN.");
        } else {
            System.out.println("Dependency injection successful for employeeEntitySessionBeanRemote INSIDE DO LOGIN.");
        }

    }

}
