/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.RoomEntitySessionBeanLocal;
import ejb.session.stateless.RoomRateEntitySessionBeanLocal;
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
import ejb.session.stateless.RoomTypeEntitySessionBeanLocal;

/**
 *
 * @author shaokangseetoh
 */
@Singleton
@LocalBean
@Startup

public class DataInitialisationBean {

    @EJB
    private RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBean;

    @EJB
    private RoomRateEntitySessionBeanLocal roomRateEntitySessionBean;

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
            initialiseEmployeeData();
        }
        //initialiseRoomTypeData();
            //initialiseRoomRateData();
            //initialiseRoomData();
    }

    private void initialiseEmployeeData() {
        employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("sysadmin", "password", EmployeeRole.SYSTEM_ADMINISTRATOR));
        employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("opmanager", "password", EmployeeRole.OPERATION_MANAGER));
        employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("salesmanager", "password", EmployeeRole.SALES_MANAGER));
        employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("guestrelo", "password", EmployeeRole.GUEST_RELATION_OFFICER));

    }

    private void initialiseRoomTypeData() {
        /*Name, NextHigherRoomType
        ------------------------
        Deluxe Room, Premier Room
        Premier Room, Family Room
        Family Room, Junior Suite
        Junior Suite, Grand Suite
        Grand Suite, None (i.e., Grand Suite is the current highest room type)*/
    }

    private void initialiseRoomRateData() {
        /*Name, RoomType, RateType, RatePerNight
        --------------------------------------
        Deluxe Room Published, Deluxe Room, Published, $100
        Deluxe Room Normal, Deluxe Room, Normal, $50
        Premier Room Published, Premier Room, Published, $200
        Premier Room Normal, Premier Room, Normal, $100
        Family Room Published, Family Room, Published, $300
        Family Room Normal, Family Room, Normal, $150
        Junior Suite Published, Junior Suite, Published, $400
        Junior Suite Normal, Junior Suite, Normal, $200
        Grand Suite Published, Grand Suite, Published, $500
        Grand Suite Normal, Grand Suite, Normal, $250*/


    }

    private void initialiseRoomData() {
        /*RoomType, RoomNumber, RoomStatus
--------------------------------
Deluxe Room, 0101, Available
Deluxe Room, 0201, Available
Deluxe Room, 0301, Available
Deluxe Room, 0401, Available
Deluxe Room, 0501, Available
Premier Room, 0102, Available
Premier Room, 0202, Available
Premier Room, 0302, Available
Premier Room, 0402, Available
Premier Room, 0502, Available
Family Room, 0103, Available
Family Room, 0203, Available
Family Room, 0303, Available
Family Room, 0403, Available
Family Room, 0503, Available
Junior Suite, 0104, Available
Junior Suite, 0204, Available
Junior Suite, 0304, Available
Junior Suite, 0404, Available
Junior Suite, 0504, Available
Grand Suite, 0105, Available
Grand Suite, 0205, Available
Grand Suite, 0305, Available
Grand Suite, 0405, Available
Grand Suite, 0505, Available*/

    }
}
