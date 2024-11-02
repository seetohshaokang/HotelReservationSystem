/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session;

import entity.EmployeeEntity;
import javax.ejb.Local;
import java.util.*;
import util.exception.EmployeeExistException;
import util.exception.InvalidPasswordException;
import util.exception.InvalidUsernameException;

/**
 *
 * @author shaokangseetoh
 */
@Local
public interface EmployeeSessionBeanLocal {
    
    public void createNewEmployee(EmployeeEntity employee) throws EmployeeExistException;
    // Use JPQL
    public boolean employeeLogin(String username, String password) throws InvalidUsernameException, InvalidPasswordException;
    
}
