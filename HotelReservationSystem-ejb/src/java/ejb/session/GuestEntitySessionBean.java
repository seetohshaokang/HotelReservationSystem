/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session;

import entity.GuestEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.InvalidInputException;

/**
 *
 * @author shaokangseetoh
 */
@Stateless
public class GuestEntitySessionBean implements GuestEntitySessionBeanRemote, GuestEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewGuest(String name, String email, String password) throws InvalidInputException {
        if (name.isEmpty()) {
            throw new InvalidInputException("Name cannot be empty!");
        } else if (email.isEmpty()) {
            throw new InvalidInputException("Email cannot be empty!");
        } else if (password.isEmpty()) {
            throw new InvalidInputException("Password cannot be empty");
        } else {
            GuestEntity newGuest = new GuestEntity(name, email, password);
            em.persist(newGuest);
            em.flush();
            return newGuest.getGuestId();
        }
    }

}
