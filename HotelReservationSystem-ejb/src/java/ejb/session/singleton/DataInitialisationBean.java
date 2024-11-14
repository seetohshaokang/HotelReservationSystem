/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.GuestEntitySessionBeanLocal;
import ejb.session.stateless.ReservationEntitySessionBeanLocal;
import ejb.session.stateless.RoomEntitySessionBeanLocal;
import ejb.session.stateless.RoomRateEntitySessionBeanLocal;
import entity.EmployeeEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.EmployeeRole;
import ejb.session.stateless.RoomReservationEntitySessionBeanLocal;
import ejb.session.stateless.RoomTypeEntitySessionBeanLocal;
import entity.ReservationEntity;
import entity.RoomTypeEntity;
import entity.VisitorEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import util.enumeration.RateType;
import util.enumeration.RoomStatus;
import util.enumeration.RoomTypeName;
import util.exception.DisabledException;
import util.exception.ExistingRoomException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author shaokangseetoh
 */
@Singleton
@LocalBean
@Startup

public class DataInitialisationBean {

    @EJB
    private ReservationEntitySessionBeanLocal reservationEntitySessionBean;

    @EJB
    private RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBeanLocal;

    @EJB
    private RoomRateEntitySessionBeanLocal roomRateEntitySessionBeanLocal;

    @EJB
    private RoomReservationEntitySessionBeanLocal roomReservationSessionBeanLocal;

    @EJB
    private RoomEntitySessionBeanLocal roomEntitySessionBeanLocal;

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
        if (roomTypeEntitySessionBeanLocal.viewAllRoomTypes().isEmpty()) {
            initialiseRoomTypeData();
        }
        if (roomRateEntitySessionBeanLocal.viewAllRoomRates().isEmpty()) {
            initialiseRoomRateData();
        }
        if (roomEntitySessionBeanLocal.viewAllRooms().isEmpty()) {
            initialiseRoomData();
        }
    }

    private void initialiseEmployeeData() {
        employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("sysadmin", "password", EmployeeRole.SYSTEM_ADMINISTRATOR));
        employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("opmanager", "password", EmployeeRole.OPERATION_MANAGER));
        employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("salesmanager", "password", EmployeeRole.SALES_MANAGER));
        employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("guestrelo", "password", EmployeeRole.GUEST_RELATION_OFFICER));

    }

    private void initialiseRoomTypeData() {
        try {
            // Deluxe Room, Premier Room
            List<String> deluxeAmenities = new ArrayList<>();
            deluxeAmenities.add("TV");
            deluxeAmenities.add("Mini Fridge");
            deluxeAmenities.add("Air Conditioning");
            RoomTypeEntity deluxeRoom = new RoomTypeEntity(
                    RoomTypeName.DELUXE,
                    RoomTypeName.PREMIER,
                    "Deluxe Room with essential amenities, suitable for budget travelers.",
                    30.0,
                    "Queen Bed",
                    2,
                    deluxeAmenities
            );
            roomTypeEntitySessionBeanLocal.createNewRoomType(deluxeRoom);

            // Premier Room, Family Room
            List<String> premierAmenities = new ArrayList<>();
            premierAmenities.add("TV");
            premierAmenities.add("Mini Fridge");
            premierAmenities.add("Air Conditioning");
            premierAmenities.add("Work Desk");
            RoomTypeEntity premierRoom = new RoomTypeEntity(
                    RoomTypeName.PREMIER,
                    RoomTypeName.FAMILY,
                    "Premier Room with enhanced amenities and larger space.",
                    40.0,
                    "King Bed",
                    2,
                    premierAmenities
            );
            roomTypeEntitySessionBeanLocal.createNewRoomType(premierRoom);

            // Family Room, Junior Suite
            List<String> familyAmenities = new ArrayList<>();
            familyAmenities.add("TV");
            familyAmenities.add("Mini Fridge");
            familyAmenities.add("Air Conditioning");
            familyAmenities.add("Living Area");
            RoomTypeEntity familyRoom = new RoomTypeEntity(
                    RoomTypeName.FAMILY,
                    RoomTypeName.JUNIOR,
                    "Family Room with spacious area, ideal for small families.",
                    50.0,
                    "Two Queen Beds",
                    4,
                    familyAmenities
            );
            roomTypeEntitySessionBeanLocal.createNewRoomType(familyRoom);

            // Junior Suite, Grand Suite
            List<String> juniorSuiteAmenities = new ArrayList<>();
            juniorSuiteAmenities.add("TV");
            juniorSuiteAmenities.add("Mini Fridge");
            juniorSuiteAmenities.add("Air Conditioning");
            juniorSuiteAmenities.add("Living Room");
            juniorSuiteAmenities.add("Coffee Maker");
            RoomTypeEntity juniorSuite = new RoomTypeEntity(
                    RoomTypeName.JUNIOR,
                    RoomTypeName.GRAND,
                    "Junior Suite with a luxurious setting and additional services.",
                    60.0,
                    "King Bed and Sofa Bed",
                    4,
                    juniorSuiteAmenities
            );
            roomTypeEntitySessionBeanLocal.createNewRoomType(juniorSuite);

            // Grand Suite, None (highest level)
            List<String> grandSuiteAmenities = new ArrayList<>();
            grandSuiteAmenities.add("TV");
            grandSuiteAmenities.add("Mini Fridge");
            grandSuiteAmenities.add("Air Conditioning");
            grandSuiteAmenities.add("Living Room");
            grandSuiteAmenities.add("Coffee Maker");
            grandSuiteAmenities.add("Private Balcony");
            RoomTypeEntity grandSuite = new RoomTypeEntity(
                    RoomTypeName.GRAND,
                    null,
                    "Grand Suite, our highest level suite offering top-tier luxury and space.",
                    80.0,
                    "King Bed and Sofa Bed",
                    4,
                    grandSuiteAmenities
            );
            roomTypeEntitySessionBeanLocal.createNewRoomType(grandSuite);

            System.out.println("Room type data initialized successfully.");

        } catch (Exception ex) {
            System.out.println("Failed to initialize room type data: " + ex.getMessage());
        }
    }

    private void initialiseRoomRateData() {
        //Name, RoomType, RateType, RatePerNight

        //Deluxe Room Published, Deluxe Room, Published, $100
        try {
            RoomTypeEntity roomType1 = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.DELUXE);
            roomRateEntitySessionBeanLocal.createNewRoomRate("Deluxe Room Published", roomType1.getRoomTypeId(), RateType.PUBLISHED, (double) 100, null, null);
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Deluxe Room Normal, Deluxe Room, Normal, $50
        try {
            RoomTypeEntity roomType2 = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.DELUXE);
            roomRateEntitySessionBeanLocal.createNewRoomRate("Deluxe Room Normal", roomType2.getRoomTypeId(), RateType.NORMAL, (double) 50, null, null);
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Premier Room Published, Premier Room, Published, $200
        try {
            RoomTypeEntity roomType3 = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.PREMIER);
            roomRateEntitySessionBeanLocal.createNewRoomRate("Premier Room Published", roomType3.getRoomTypeId(), RateType.PUBLISHED, (double) 200, null, null);
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Premier Room Normal, Premier Room, Normal, $100
        try {
            RoomTypeEntity roomType4 = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.PREMIER);
            roomRateEntitySessionBeanLocal.createNewRoomRate("Premier Room Normal", roomType4.getRoomTypeId(), RateType.NORMAL, (double) 100, null, null);
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Family Room Published, Family Room, Published, $300
        try {
            RoomTypeEntity roomType5 = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.FAMILY);
            roomRateEntitySessionBeanLocal.createNewRoomRate("Family Room Published", roomType5.getRoomTypeId(), RateType.PUBLISHED, (double) 300, null, null);
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Family Room Normal, Family Room, Normal, $150
        try {
            RoomTypeEntity roomType6 = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.FAMILY);
            roomRateEntitySessionBeanLocal.createNewRoomRate("Family Room Normal", roomType6.getRoomTypeId(), RateType.NORMAL, (double) 150, null, null);
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Junior Suite Published, Junior Suite, Published, $400
        try {
            RoomTypeEntity roomType7 = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.JUNIOR);
            roomRateEntitySessionBeanLocal.createNewRoomRate("Junior Suite Published", roomType7.getRoomTypeId(), RateType.PUBLISHED, (double) 400, null, null);
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Junior Suite Normal, Junior Suite, Normal, $200
        try {
            RoomTypeEntity roomType8 = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.JUNIOR);
            roomRateEntitySessionBeanLocal.createNewRoomRate("Junior Suite Normal", roomType8.getRoomTypeId(), RateType.NORMAL, (double) 200, null, null);
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Grand Suite Published, Grand Suite, Published, $500
        try {
            RoomTypeEntity roomType9 = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.GRAND);
            roomRateEntitySessionBeanLocal.createNewRoomRate("Grand Suite Published", roomType9.getRoomTypeId(), RateType.PUBLISHED, (double) 500, null, null);
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Grand Suite Normal, Grand Suite, Normal, $250
        try {
            RoomTypeEntity roomType10 = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.GRAND);
            roomRateEntitySessionBeanLocal.createNewRoomRate("Grand Suite Normal", roomType10.getRoomTypeId(), RateType.NORMAL, (double) 250, null, null);
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
    }

    private void initialiseRoomData() {
        //RoomType, RoomNumber, RoomStatus

        //Deluxe Room, 0101, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.DELUXE, 01, 01, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Deluxe Room, 0201, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.DELUXE, 02, 01, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Deluxe Room, 0301, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.DELUXE, 03, 01, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Deluxe Room, 0401, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.DELUXE, 04, 01, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Deluxe Room, 0501, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.DELUXE, 05, 01, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Premier Room, 0102, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.PREMIER, 01, 02, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Premier Room, 0202, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.PREMIER, 02, 02, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Premier Room, 0302, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.PREMIER, 03, 02, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Premier Room, 0402, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.PREMIER, 04, 02, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Premier Room, 0502, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.PREMIER, 05, 02, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Family Room, 0103, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.FAMILY, 01, 03, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Family Room, 0203, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.FAMILY, 02, 03, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Family Room, 0303, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.FAMILY, 03, 03, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Family Room, 0403, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.FAMILY, 04, 03, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Family Room, 0503, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.FAMILY, 05, 03, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Junior Suite, 0104, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.JUNIOR, 01, 04, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Junior Suite, 0204, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.JUNIOR, 02, 04, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Junior Suite, 0304, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.JUNIOR, 03, 04, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Junior Suite, 0404, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.JUNIOR, 04, 04, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Junior Suite, 0504, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.JUNIOR, 05, 04, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Grand Suite, 0105, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.GRAND, 01, 05, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Grand Suite, 0205, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.GRAND, 02, 05, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Grand Suite, 0305, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.GRAND, 03, 05, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Grand Suite, 0405, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.GRAND, 04, 05, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Grand Suite, 0505, Available
        try {
            roomEntitySessionBeanLocal.createNewRoom(RoomTypeName.GRAND, 05, 05, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
    }

}
