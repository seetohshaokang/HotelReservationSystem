/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.GuestEntity;
import javax.ejb.Local;
import util.exception.GuestNotFoundException;
import util.exception.InvalidInputException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author shaokangseetoh
 */
@Local
public interface GuestEntitySessionBeanLocal {
    
    public Long createNewGuest(String name, String email, String password)throws InvalidInputException;
    
    public GuestEntity guestLogin(String email, String password) throws InvalidLoginCredentialException;

    public GuestEntity retrieveGuestByEmail(String email) throws GuestNotFoundException;

}
