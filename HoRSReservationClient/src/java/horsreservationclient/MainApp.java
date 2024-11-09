/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsreservationclient;

import entity.GuestEntity;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author shaokangseetoh
 */
public class MainApp {
    
    // Session Beans
    
    // Module -> No seperation of modules but have 2 "menus"
    
    private GuestEntity loggedInGuest;

    public MainApp() {
    }
    
    // Insert constructor with the session beans
    
    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("*** Welcome to HORS - Reservation System ***");
            System.out.println("1: Guest Login");
            System.out.println("2: Register as Guest");
            System.out.println("3: Search Hotel Room");
            System.out.println("---------------------------");
            System.out.println("4: Exit\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login Successful");
                        menuGuest();
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid Login Credentials: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    //
                } else if (response == 3) {
                    //
                } else if (response == 4) {
                    break; 
                } else {
                    System.out.println("Invalid Option, please try again!\n");
                }

            }
            if (response == 4) {
                break;
            }
        }
    }
    
    private void doLogin() throws InvalidLoginCredentialException {
        
    }
    
    private void registerVisitor() {
    }
    
    private void searchHotelRoom() {
    }
    
    private void menuGuest(){
        
    }
    
    private void reserveHotelRoom() {
    }
    
    private void viewMyReservationDetails() {
    }
    
    private void viewAllMyReservations() {
    }
    
}
