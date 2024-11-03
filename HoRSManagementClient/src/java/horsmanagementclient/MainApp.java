package horsmanagementclient;

import entity.EmployeeEntity;
import java.util.Scanner;
import ejb.session.EmployeeEntitySessionBeanRemote;
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
    private EmployeeEntitySessionBeanRemote employeeEntitySessionBean;

    // Modules
    private SystemAdministratorModule systemAdminModule;

    // Employee States
    private EmployeeEntity currentEmployee;

    public MainApp() {
    }

    public MainApp(EmployeeEntitySessionBeanRemote employeeSessionBean) {
        this.employeeEntitySessionBean = employeeSessionBean;
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
                        doLogin();
                        System.out.println("Login Successful");
                        systemAdminModule = new SystemAdministratorModule(employeeEntitySessionBean, currentEmployee);
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

        if (username.length() > 0 && password.length() > 0) {
            currentEmployee = employeeEntitySessionBean.employeeLogin(username, password);
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
                    System.out.println("Operation Manager Module is not implemented yet");
                } else if (response == 3) {
                    // invoke module.method() in try catch
                    System.out.println("Sales Manager Module is not implemented yet");
                } else if (response == 4) {
                    // invoke module.method() in try catch
                    System.out.println("Guest Relation Officer Module is not implemented yet");
                } else if (response == 5) {
                    break; // Break inner while loop
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 5) {
                break; // Break outer while loop
            }
        }
    }

}
