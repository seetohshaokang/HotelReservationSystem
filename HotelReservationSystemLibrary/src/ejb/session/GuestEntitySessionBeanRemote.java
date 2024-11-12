/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session;

import javax.ejb.Remote;
import util.exception.InvalidInputException;

/**
 *
 * @author shaokangseetoh
 */
@Remote
public interface GuestEntitySessionBeanRemote {

    public Long createNewGuest(String name, String email, String password) throws InvalidInputException;

}
