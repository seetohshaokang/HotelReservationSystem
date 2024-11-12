/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session;

import javax.ejb.Local;
import util.exception.InvalidInputException;

/**
 *
 * @author shaokangseetoh
 */
@Local
public interface GuestEntitySessionBeanLocal {
    
    public Long createNewGuest(String name, String email, String password)throws InvalidInputException;
    
    
}
