/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
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
import entity.RoomTypeEntity;
import entity.VisitorEntity;
import java.util.ArrayList;
import java.util.List;
import util.enumeration.RateType;
import util.enumeration.RoomStatus;
import util.enumeration.RoomTypeName;
import util.exception.DisabledException;
import util.exception.EmployeeExistException;
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

        /*List<VisitorEntity> visitors = em.createQuery("SELECT v FROM VisitorEntity v").getResultList();
        if (visitors.isEmpty()) {
            initialiseVisitors();
        }
        if (reservationEntitySessionBean.getAllReservations().isEmpty()) {
            initialiseReservations();

        }*/
    }

    private void initialiseEmployeeData() {
        try {
            employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("sysadmin", "password", EmployeeRole.SYSTEM_ADMINISTRATOR));
            employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("opmanager", "password", EmployeeRole.OPERATION_MANAGER));
            employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("salesmanager", "password", EmployeeRole.SALES_MANAGER));
            employeeEntitySessionBeanLocal.createNewEmployee(new EmployeeEntity("guestrelo", "password", EmployeeRole.GUEST_RELATION_OFFICER));
        } catch (EmployeeExistException ex) {
            
        }

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
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.DELUXE);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 01, 01, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Deluxe Room, 0201, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.DELUXE);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 02, 01, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Deluxe Room, 0301, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.DELUXE);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 03, 01, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Deluxe Room, 0401, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.DELUXE);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 04, 01, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Deluxe Room, 0501, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.DELUXE);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 05, 01, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Premier Room, 0102, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.PREMIER);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 01, 02, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Premier Room, 0202, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.PREMIER);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 02, 02, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Premier Room, 0302, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.PREMIER);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 03, 02, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Premier Room, 0402, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.PREMIER);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 04, 02, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Premier Room, 0502, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.PREMIER);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 05, 02, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Family Room, 0103, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.FAMILY);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 01, 03, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Family Room, 0203, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.FAMILY);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 02, 03, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Family Room, 0303, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.FAMILY);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 03, 03, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Family Room, 0403, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.FAMILY);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 04, 03, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Family Room, 0503, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.FAMILY);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 05, 03, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Junior Suite, 0104, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.JUNIOR);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 01, 04, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Junior Suite, 0204, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.JUNIOR);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 02, 04, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Junior Suite, 0304, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.JUNIOR);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 03, 04, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Junior Suite, 0404, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.JUNIOR);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 04, 04, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Junior Suite, 0504, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.JUNIOR);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 05, 04, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Grand Suite, 0105, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.GRAND);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 01, 05, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Grand Suite, 0205, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.GRAND);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 02, 05, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Grand Suite, 0305, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.GRAND);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 03, 05, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Grand Suite, 0405, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.GRAND);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 04, 05, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
        //Grand Suite, 0505, Available
        try {
            RoomTypeEntity roomtype = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.GRAND);
            roomEntitySessionBeanLocal.createNewRoom(roomtype.getRoomTypeId(), 05, 05, RoomStatus.AVAILABLE);
        } catch (ExistingRoomException | RoomTypeNotFoundException | DisabledException ex) {
            System.out.println("Failed to create new room rate: " + ex.getMessage());
        }
    }

    /*private void initialiseVisitors() {
        VisitorEntity visitor1 = new VisitorEntity("Alice Tan", "alice@example.com");

        em.persist(visitor1);

        System.out.println("Sample visitor initialized.");
    }

    private void initialiseReservations() {
        // Retrieve an existing visitor from the database
        VisitorEntity visitor = em.createQuery("SELECT v FROM VisitorEntity v WHERE v.email = :email", VisitorEntity.class)
                .setParameter("email", "alice@example.com").getSingleResult();

        // Sample room type data (Assuming some RoomTypeEntity instances exist in the database)
        RoomTypeEntity roomType = null;
        try {
            roomType = roomTypeEntitySessionBeanLocal.getRoomTypeByName(RoomTypeName.FAMILY);
        } catch (RoomTypeNotFoundException e) {
            System.out.println("Room Type Not Found!");
        }
        // Create a reservation with the specified visitor, room type, and room details
        ReservationEntity reservation = new ReservationEntity(visitor, roomType, LocalDate.now(), LocalDate.now().plusDays(3), 450.0, 2);

        em.persist(reservation);

        System.out.println("Sample reservation initialized with visitor and room type.");
    }*/
}
