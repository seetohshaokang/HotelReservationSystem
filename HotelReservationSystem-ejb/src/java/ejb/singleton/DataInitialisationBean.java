/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.singleton;

import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.RoomEntitySessionBeanLocal;
import entity.EmployeeEntity;
import entity.GuestEntity;
import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomReservationEntity;
import java.time.LocalDate;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.EmployeeRole;
import util.enumeration.ReservationStatus;
import util.exception.EmployeeNotFoundException;
import ejb.session.stateless.RoomReservationEntitySessionBeanLocal;

/**
 *
 * @author shaokangseetoh
 */
@Singleton
@LocalBean
@Startup

public class DataInitialisationBean {

    @EJB
    private RoomReservationEntitySessionBeanLocal roomReservationSessionBean;

    
    
    @EJB
    private RoomEntitySessionBeanLocal roomEntitySessionBean;

    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBeanLocal;
    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    
    
    public DataInitialisationBean() {
    }

    @PostConstruct
    public void postContstruct() {

        // Initialise data if there are no employees.
        if (employeeEntitySessionBeanLocal.retrieveAllEmployees().isEmpty()) {
            initialiseData();
        }
    }

    private void initialiseData() {
        employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("sa", "password", EmployeeRole.SYSTEM_ADMINISTRATOR));
        employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("om", "password", EmployeeRole.OPERATION_MANAGER));
        employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("sm", "password", EmployeeRole.SALES_MANAGER));
        employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("gro", "password", EmployeeRole.GUEST_RELATION_OFFICER));

    }
}
