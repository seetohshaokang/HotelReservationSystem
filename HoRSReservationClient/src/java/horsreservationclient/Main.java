/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package horsreservationclient;

import ejb.session.GuestEntitySessionBeanRemote;
import ejb.session.RoomEntitySessionBeanRemote;
import ejb.session.stateless.helper.RoomReservationSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author shaokangseetoh
 */
public class Main {

    @EJB
    private static RoomReservationSessionBeanRemote roomReservationSessionBean;

    

    @EJB
    private static RoomEntitySessionBeanRemote roomEntitySessionBean;

    @EJB
    private static GuestEntitySessionBeanRemote guestEntitySessionBean;
    
    
    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(guestEntitySessionBean, roomEntitySessionBean, roomReservationSessionBean);
        mainApp.runApp();
    }
    
}
