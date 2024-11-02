/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session;

import entity.EmployeeEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.EmployeeExistException;
import util.exception.InvalidPasswordException;
import util.exception.InvalidUsernameException;

/**
 *
 * @author shaokangseetoh
 */
@Remote
public interface EmployeeSessionBeanRemote {
    public void createNewEmployee(EmployeeEntity employee) throws EmployeeExistException;
    
    // Use JPQL
    public boolean employeeLogin(String username, String password) throws InvalidUsernameException, InvalidPasswordException;
}
