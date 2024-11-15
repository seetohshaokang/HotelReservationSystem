package horsmanagementclient;

import entity.EmployeeEntity;
import java.util.Scanner;
import ejb.session.EmployeeEntitySessionBeanRemote;
import ejb.session.PartnerEntitySessionBeanRemote;
import ejb.session.ReservationEntitySessionBeanRemote;
import ejb.session.RoomEntitySessionBeanRemote;
import ejb.session.RoomRateEntitySessionBeanRemote;
import ejb.session.RoomTypeEntitySessionBeanRemote;
import ejb.session.stateless.helper.ExceptionReportSessionBeanRemote;
import ejb.session.stateless.helper.RoomCheckInOutSessionBeanRemote;
import ejb.session.stateless.helper.RoomReservationSessionBeanRemote;
import java.util.InputMismatchException;
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
    private PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote;
    private RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;
    private RoomEntitySessionBeanRemote roomEntitySessionBeanRemote;
    private RoomRateEntitySessionBeanRemote roomRateEntitySessionBeanRemote;
    private RoomReservationSessionBeanRemote roomReservationSessionBeanRemote;
    private ExceptionReportSessionBeanRemote exceptionReportSessionBean;
    private RoomCheckInOutSessionBeanRemote roomCheckInOutSessionBean;
    private ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote;

    // Modules for construction later
    private SystemAdministratorModule systemAdminModule;
    private OperationManagerModule operationManagerModule;
    private SalesManagerModule salesManagerModule;
    private GuestRelationOfficerModule guestRelationOfficerModule;

    // Employee States
    private EmployeeEntity currentEmployee;

    public MainApp() {
    }

    public MainApp(EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote, PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote, RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote, RoomEntitySessionBeanRemote roomEntitySessionBeanRemote, RoomRateEntitySessionBeanRemote roomRateEntitySessionBeanRemote, RoomReservationSessionBeanRemote roomReservationSessionBeanRemote, ExceptionReportSessionBeanRemote exceptionReportSessionBean, RoomCheckInOutSessionBeanRemote roomCheckInOutSessionBean, ReservationEntitySessionBeanRemote reservationEntitySessionBeanRemote) {
        this.employeeEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
        this.partnerEntitySessionBeanRemote = partnerEntitySessionBeanRemote;
        this.roomTypeEntitySessionBeanRemote = roomTypeEntitySessionBeanRemote;
        this.roomEntitySessionBeanRemote = roomEntitySessionBeanRemote;
        this.roomRateEntitySessionBeanRemote = roomRateEntitySessionBeanRemote;
        this.roomReservationSessionBeanRemote = roomReservationSessionBeanRemote;
        this.exceptionReportSessionBean = exceptionReportSessionBean;
        this.roomCheckInOutSessionBean = roomCheckInOutSessionBean;
        this.reservationEntitySessionBeanRemote = reservationEntitySessionBeanRemote;
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to HORS - Management System ***");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");

            while (true) {
                System.out.print("> ");
                try {
                    response = scanner.nextInt();

                    if (response == 1) {
                        try {
                            doLogin();
                            System.out.println("Login Successful");
                            systemAdminModule = new SystemAdministratorModule(employeeEntitySessionBeanRemote, partnerEntitySessionBeanRemote, currentEmployee);
                            operationManagerModule = new OperationManagerModule(roomTypeEntitySessionBeanRemote, roomEntitySessionBeanRemote, exceptionReportSessionBean, currentEmployee);
                            salesManagerModule = new SalesManagerModule(roomRateEntitySessionBeanRemote, roomTypeEntitySessionBeanRemote, currentEmployee);
                            guestRelationOfficerModule = new GuestRelationOfficerModule(roomReservationSessionBeanRemote, roomCheckInOutSessionBean, currentEmployee, reservationEntitySessionBeanRemote);
                            menuMain();
                        } catch (InvalidLoginCredentialException ex) {
                            System.out.println("Invalid Login Credentials: " + ex.getMessage() + "\n");
                        }
                        break;
                    } else if (response == 2) {
                        System.out.println("Exiting the system.");
                        return;
                    } else {
                        System.out.println("Invalid Option, please try again!\n");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input, please enter 1 or 2.\n");
                    scanner.next(); // Clear the invalid input
                }
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
            currentEmployee = employeeEntitySessionBeanRemote.employeeLogin(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    // COMPLETED
    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** HORS - Management System ***\n");
            System.out.println("You are logged in as " + currentEmployee.getUsername() + " with " + currentEmployee.getRole().toString() + " rights\n");

            System.out.println("1. System Administrator Operations");
            System.out.println("2. Operation Manager Operations");
            System.out.println("3. Sales Manager Operations");
            System.out.println("4. Guest Relation Officer Operations");
            System.out.println("5. Logout\n");
            System.out.print("> ");
            try {
                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        systemAdminModule.menuSystemAdministrator();
                    } catch (InvalidAccessRightException ex) {
                        System.out.println("Invalid access rights: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    try {
                        operationManagerModule.menuOperationManager();
                    } catch (InvalidAccessRightException ex) {
                        System.out.println("Invalid access rights: " + ex.getMessage() + "\n");
                    }
                } else if (response == 3) {
                    try {
                        salesManagerModule.menuSalesManager();
                    } catch (InvalidAccessRightException ex) {
                        System.out.println("Invalid access rights: " + ex.getMessage() + "\n");
                    }
                } else if (response == 4) {
                    try {
                        guestRelationOfficerModule.menuGuestRelationOfficer();
                    } catch (InvalidAccessRightException ex) {
                        System.out.println("Invalid access rights: " + ex.getMessage() + "\n");
                    }
                } else if (response == 5) {
                    System.out.println("Logging out...");
                    return; // Exit the method
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please enter a number between 1 and 5.\n");
                scanner.next(); // Clear the invalid input
            }
        }

    }

}
