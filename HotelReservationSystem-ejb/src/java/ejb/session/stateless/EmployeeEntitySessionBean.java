/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import ejb.session.EmployeeEntitySessionBeanRemote;
import entity.EmployeeEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.EmployeeExistException;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author shaokangseetoh
 */
@Stateless
public class EmployeeEntitySessionBean implements EmployeeEntitySessionBeanRemote, EmployeeEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewEmployee(EmployeeEntity newEmployee) throws EmployeeExistException {
        EmployeeEntity e = null;
        try {
            e = retrieveEmployeeByUsername(newEmployee.getUsername());
        } catch (EmployeeNotFoundException ex) {

        }
        if (e == null) {
            em.persist(newEmployee);
            em.flush();
        } else {
            throw new EmployeeExistException("Username already exists");
        }

        return newEmployee.getEmployeeId();
    }

    public EmployeeEntity employeeLogin(String username, String password) throws InvalidLoginCredentialException {

        try {

            EmployeeEntity employeeEntity = retrieveEmployeeByUsername(username);

            if (employeeEntity.getPassword().equals(password)) {
                return employeeEntity;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }

        } catch (EmployeeNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password");
        }

    }

    // JPQL Query
    @Override
    public EmployeeEntity retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException {

        Query query = em.createQuery("SELECT e FROM EmployeeEntity e WHERE e.username = :username", EmployeeEntity.class);
        query.setParameter("username", username);

        try {
            return (EmployeeEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new EmployeeNotFoundException("Employee Username " + username + " does not exits!");
        }

    }

    // em.find()
    @Override
    public EmployeeEntity retrieveEmployeeByEmployeeId(Long employeeId) throws EmployeeNotFoundException {

        EmployeeEntity employeeEntity = em.find(EmployeeEntity.class, employeeId);
        if (employeeEntity != null) {
            return employeeEntity;
        } else {
            throw new EmployeeNotFoundException("Employee ID " + employeeId + " does not exist!");
        }

    }

    @Override
    public List<EmployeeEntity> retrieveAllEmployees() {

        Query query = em.createQuery("SELECT e FROM EmployeeEntity e");

        return query.getResultList();

    }

    // Update and delete staff operations are not supported
}
