/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.singleton;

import ejb.session.EmployeeEntitySessionBeanLocal;
import entity.EmployeeEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.enumeration.EmployeeRole;
import util.exception.EmployeeNotFoundException;

/**
 *
 * @author shaokangseetoh
 */
@Singleton
@LocalBean
@Startup

public class DataInitialisationBean {

    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBean;

    public DataInitialisationBean() {
    }

    @PostConstruct
    public void postContstruct() {
        
        // Initialise data if there are no employees.
        if(employeeEntitySessionBean.retrieveAllEmployees().isEmpty()) {
            initialiseData();
        }
    }
    
    private void initialiseData() {
        employeeEntitySessionBean.createNewEmployee(new EmployeeEntity("manager", "password", EmployeeRole.SYSTEM_ADMINISTRATOR));
    }
}
