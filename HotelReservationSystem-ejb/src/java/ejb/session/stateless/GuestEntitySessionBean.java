/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import ejb.session.GuestEntitySessionBeanRemote;
import entity.GuestEntity;
import entity.ReservationEntity;
import entity.VisitorEntity;
import java.util.List;
import java.util.regex.Pattern;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.GuestNotFoundException;
import util.exception.InvalidInputException;
import util.exception.InvalidLoginCredentialException;
import util.exception.VisitorFoundException;

/**
 *
 * @author shaokangseetoh
 */
@Stateless
public class GuestEntitySessionBean implements GuestEntitySessionBeanRemote, GuestEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public Long createNewGuest(String name, String email, String password) throws InvalidInputException {
        // Validate name: non-empty, only letters and spaces, reasonable length
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Name cannot be empty!");
        } else if (!name.matches("^[a-zA-Z\\s]+$")) {
            throw new InvalidInputException("Name can only contain letters and spaces.");
        } else if (name.length() < 2 || name.length() > 50) {
            throw new InvalidInputException("Name must be between 2 and 50 characters.");
        }

        // Validate email: non-empty, valid email format
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidInputException("Email cannot be empty!");
        } else {
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            Pattern pattern = Pattern.compile(emailRegex);
            if (!pattern.matcher(email).matches()) {
                throw new InvalidInputException("Invalid email format.");
            }
        }

        // Validate password: non-empty, minimum length, and complexity (e.g., at least one letter and one number)
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidInputException("Password cannot be empty.");
        }
        // Check if guest already exist
        GuestEntity newGuest;
        try {
            retrieveGuestByEmail(email);
            throw new InvalidInputException("A guest with this email already exists.");
        } catch (GuestNotFoundException ex) {
            // Create and persist the new guest entity
            newGuest = new GuestEntity(name, email, password);
            em.persist(newGuest);
            em.flush();
            return newGuest.getGuestId();
        }
    }

    @Override
    public GuestEntity guestLogin(String email, String password) throws InvalidLoginCredentialException {
        // Input validation for email and password
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidLoginCredentialException("Email cannot be empty.");
        }

        // Basic email format validation (regex)
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(emailRegex)) {
            throw new InvalidLoginCredentialException("Invalid email format.");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new InvalidLoginCredentialException("Password cannot be empty.");
        }

        try {
            GuestEntity guestEntity = retrieveGuestByEmail(email);

            // Password verification
            if (guestEntity.getPassword().equals(password)) {
                return guestEntity;
            } else {
                throw new InvalidLoginCredentialException("Password is invalid.");
            }
        } catch (GuestNotFoundException ex) {
            throw new InvalidLoginCredentialException("Login failed: " + ex.getMessage());
        }
    }

    public GuestEntity retrieveGuestByEmail(String email) throws GuestNotFoundException, VisitorFoundException {
        // Query for VisitorEntity by email
        Query visitorQuery = em.createQuery("SELECT v FROM VisitorEntity v WHERE v.email = :inEmail", VisitorEntity.class);
        visitorQuery.setParameter("inEmail", email);

        try {
            VisitorEntity visitor = (VisitorEntity) visitorQuery.getSingleResult();

            // Check if the visitor is an instance of GuestEntity
            if (visitor instanceof GuestEntity) {
                return (GuestEntity) visitor; // Cast and return if it is a GuestEntity
            } else {
                throw new VisitorFoundException("The email " + email + " is associated with a visitor but not a guest.");
            }

        } catch (NoResultException ex) {
            // Throw GuestNotFoundException if no visitor is found with the given email
            throw new GuestNotFoundException("Guest with email " + email + " does not exist!");
        } catch (NonUniqueResultException ex) {
            // Handle cases where multiple visitors have the same email (should not happen if email is unique)
            throw new GuestNotFoundException("Multiple visitors with the email " + email + " found. Please contact support.");
        }
    }

    @Override
    public List<ReservationEntity> retrieveAllReservations(GuestEntity guest) {
        Long guestId = guest.getGuestId();

        GuestEntity retrievedGuest = em.find(GuestEntity.class, guestId);

        // Initialize the reservations list to avoid LazyInitializationException
        retrievedGuest.getReservations().size();
        return retrievedGuest.getReservations();
    }

    // New method to retrieve reservation only if it belongs to the specified guest
    public ReservationEntity retrieveGuestReservationById(Long reservationId, Long guestId) {
        ReservationEntity reservation = em.find(ReservationEntity.class,
                reservationId);
        if (reservation != null && reservation.getGuest() != null && reservation.getGuest().getGuestId().equals(guestId)) {
            return reservation; // Return reservation if it belongs to the guest
        }
        return null; // Return null if reservation not found or doesn't belong to guest
    }
}
