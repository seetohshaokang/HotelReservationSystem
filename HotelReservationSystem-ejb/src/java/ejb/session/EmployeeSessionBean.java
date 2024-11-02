/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session;

import entity.EmployeeEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.EmployeeExistException;
import util.exception.InvalidPasswordException;
import util.exception.InvalidUsernameException;

/**
 *
 * @author shaokangseetoh
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public void createNewEmployee(EmployeeEntity employee) throws EmployeeExistException {

        try {
            em.persist(employee);
            em.flush();
        } catch (EntityExistsException ex) {
            throw new EmployeeExistException("Employee with the same username already exists!");
        }

    }

    public boolean employeeLogin(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        
        try {
            
            Query query = em.createQuery("SELECT DISTINCT e FROM EmployeeEntity e WHERE e.username = :username", EmployeeEntity.class);
            query.setParameter("username", username);
            EmployeeEntity employee = (EmployeeEntity) query.getSingleResult();
            
            if (password.equals(employee.getPassword())) {
                return true;
            } else {
                return false; // Throw invalid username exception in client
            }
            
        } catch (NoResultException ex) {
            throw new InvalidUsernameException("Employee with provided username does not exist!");
        } 
        
    }
}
