/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package horsmanagementclient;

import javax.ejb.EJB;
import ejb.session.EmployeeEntitySessionBeanRemote;

/**
 *
 * @author shaokangseetoh
 */
public class Main {

    @EJB
    private static EmployeeEntitySessionBeanRemote employeeSessionBean;
    
    
    public static void main(String[] args) {
        MainApp mainApp= new MainApp(employeeSessionBean);
        mainApp.runApp();
    }
    
}
