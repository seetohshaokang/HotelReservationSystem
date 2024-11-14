/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session;

import entity.EmployeeEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.EmployeeExistException;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author shaokangseetoh
 */
@Remote
public interface EmployeeEntitySessionBeanRemote {
    // Use case: System Administrator :: create new employee

    public Long createNewEmployee(EmployeeEntity newEmployee) throws EmployeeExistException;

    // Use case: System Administrator :: retrieve all employees
    public List<EmployeeEntity> retrieveAllEmployees();

    // Use case: Employee -> Login
    public EmployeeEntity employeeLogin(String username, String password) throws InvalidLoginCredentialException;

    // Search methods
    public EmployeeEntity retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;

    public EmployeeEntity retrieveEmployeeByEmployeeId(Long employeeId) throws EmployeeNotFoundException;

    // Update and delete operations not required for employee.
}
