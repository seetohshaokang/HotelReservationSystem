/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.GuestEntity;
import entity.ReservationEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.GuestNotFoundException;
import util.exception.InvalidInputException;
import util.exception.InvalidLoginCredentialException;
import util.exception.VisitorFoundException;

/**
 *
 * @author shaokangseetoh
 */
@Local
public interface GuestEntitySessionBeanLocal {
    
    public Long createNewGuest(String name, String email, String password)throws InvalidInputException, VisitorFoundException;
    
    public GuestEntity guestLogin(String email, String password) throws InvalidLoginCredentialException, VisitorFoundException;
    
    public GuestEntity retrieveGuestByEmail(String email) throws GuestNotFoundException, VisitorFoundException;
    
    // New method to retrieve reservation only if it belongs to the specified guest
    public ReservationEntity retrieveGuestReservationById(Long reservationId, Long guestId);
    
    public List<ReservationEntity> retrieveAllReservations(GuestEntity guest);

}
