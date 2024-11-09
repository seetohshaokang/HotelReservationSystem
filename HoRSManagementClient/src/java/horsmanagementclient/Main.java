/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package horsmanagementclient;

import javax.ejb.EJB;
import ejb.session.EmployeeEntitySessionBeanRemote;
import ejb.session.PartnerEntitySessionBeanRemote;
import ejb.session.RoomTypeEntitySessionBeanRemote;

/**
 *
 * @author shaokangseetoh
 */
public class Main {

    @EJB
    private static RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;

    @EJB
    private static PartnerEntitySessionBeanRemote partnerEntitySessionBeanRemote;

    @EJB
    private static EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;

    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(employeeEntitySessionBeanRemote, partnerEntitySessionBeanRemote, roomTypeEntitySessionBeanRemote);
        mainApp.runApp();
    }

}
