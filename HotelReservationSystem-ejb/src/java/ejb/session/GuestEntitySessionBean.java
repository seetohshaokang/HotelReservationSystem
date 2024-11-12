/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session;

import entity.GuestEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.GuestNotFoundException;
import util.exception.InvalidInputException;
import util.exception.InvalidLoginCredentialException;

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

    @Override
    public GuestEntity guestLogin(String email, String password) throws InvalidLoginCredentialException {
        try {
            GuestEntity guestEntity = retrieveGuestByEmail(email);
            if (guestEntity.getPassword().equals(password)) {
                return guestEntity;
            } else {
                throw new InvalidLoginCredentialException("Password is invalid!");
            }
        } catch (GuestNotFoundException ex) {
            throw new InvalidLoginCredentialException("" + ex.getMessage());
        }
    }

    @Override
    public GuestEntity retrieveGuestByEmail(String email) throws GuestNotFoundException {

        Query query = em.createQuery("SELECT g FROM GuestEntity g WHERE g.email = :inEmail", GuestEntity.class);
        query.setParameter("inEmail", email);

        try {
            return (GuestEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new GuestNotFoundException("Guest with email " + email + "does not exist!");
        }

    }

}
