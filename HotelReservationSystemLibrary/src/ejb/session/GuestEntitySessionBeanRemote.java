/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session;

import entity.GuestEntity;
import entity.ReservationEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.GuestNotFoundException;
import util.exception.InvalidInputException;
import util.exception.InvalidLoginCredentialException;
import util.exception.VisitorFoundException;

/**
 *
 * @author shaokangseetoh
 */
@Remote
public interface GuestEntitySessionBeanRemote {

    public Long createNewGuest(String name, String email, String password)throws InvalidInputException;
    
    public GuestEntity guestLogin(String email, String password) throws InvalidLoginCredentialException;

public GuestEntity retrieveGuestByEmail(String email) throws GuestNotFoundException, VisitorFoundException;    
    // New method to retrieve reservation only if it belongs to the specified guest
    public ReservationEntity retrieveGuestReservationById(Long reservationId, Long guestId);
    
    public List<ReservationEntity> retrieveAllReservations(GuestEntity guest);

}
