/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session;

import entity.RoomRateEntity;
import entity.RoomTypeEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RateType;
import util.enumeration.RoomTypeName;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author shaokangseetoh
 */
@Stateless
public class RoomRateEntitySessionBean implements RoomRateEntitySessionBeanRemote, RoomRateEntitySessionBeanLocal {

    private RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public String createNewRoomRate(String name, RoomTypeName selectedRoomType, RateType selectedRateType, Double ratePerNight, Date start, Date end) throws RoomTypeNotFoundException {
        RoomTypeEntity roomType = null;
        try {
            roomType = roomTypeEntitySessionBeanLocal.getRoomTypeByName(selectedRoomType);
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RoomTypeNotFoundException("Room Type does not exist!");

        }
        RoomRateEntity newRoomRate = new RoomRateEntity();
        newRoomRate.setName(name);
        newRoomRate.setRoomType(roomType);
        newRoomRate.setRateType(selectedRateType);
        newRoomRate.setRatePerNight(ratePerNight);
        newRoomRate.setStartDate(start);
        newRoomRate.setEndDate(end);
        em.persist(newRoomRate);
        roomType.getRoomRates().add(newRoomRate);
        em.flush();
        
        return name;
    }

    @Override
    public RoomRateEntity getRoomRateById(Long id) throws RoomRateNotFoundException {
                Query query = em.createQuery("SELECT r FROM RoomRateEntity r WHERE r.roomRateId = :inId", RoomRateEntity.class);
        query.setParameter("inId", id);
        try {
            return (RoomRateEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RoomRateNotFoundException ("Room rate with room rate id " + id + " does not exist!");
        }
    }
    
    @Override
    public List<RoomRateEntity> viewAllRoomRates() {
        Query query = em.createQuery("SELECT r FROM RoomRateEntity r");
        return query.getResultList();
    }
}
