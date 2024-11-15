/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless.helper;

import entity.ExceptionReportEntity;
import entity.RoomEntity;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author shaokangseetoh
 */
@Stateless
public class ExceptionReportSessionBean implements ExceptionReportSessionBeanRemote, ExceptionReportSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createTypeOneException(RoomEntity oldRoom, RoomEntity upgradedRoom) {
        // Find not needed as within same PC
        ExceptionReportEntity newReport = new ExceptionReportEntity("Room number: " + oldRoom.getRoomNumber() + " with room type "
                + oldRoom.getRoomType().getRoomTypeName() + " cannot be allocated "
                + "New room number: " + upgradedRoom.getRoomNumber() + " of the next highest room type "
                + oldRoom.getRoomType().getNextHigherRoomTypeName() + " is automatically allocated");
        em.persist(newReport);
        em.flush();
        return newReport.getExceptionReportId();
    }

    @Override
    public Long createTypeTwoException(RoomEntity oldRoom) {

        ExceptionReportEntity newReport = new ExceptionReportEntity("Room number: " + oldRoom.getRoomNumber() + " with room type "
                + oldRoom.getRoomType().getRoomTypeName() + " cannot be allocated " + " no available rooms in next higher room type "
                + oldRoom.getRoomType().getNextHigherRoomTypeName() + " please proceed with manual allocation");

        em.persist(newReport);
        em.flush();
        return newReport.getExceptionReportId();
    }

    @Override
    public List<ExceptionReportEntity> generateExceptionReport(LocalDate date) {
        // Create a JPQL query to fetch exception reports with a matching date
        Query query = em.createQuery(
                "SELECT e FROM ExceptionReportEntity e WHERE e.dateCreated = :date",
                ExceptionReportEntity.class
        );
        query.setParameter("date", date);

        // Execute the query and return the result list
        return query.getResultList();
    }
}
