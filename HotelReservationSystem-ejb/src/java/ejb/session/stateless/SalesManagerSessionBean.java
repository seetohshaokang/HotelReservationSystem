/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import ejb.session.SalesManagerSessionBeanRemote;
import entity.RoomRateEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.RoomRateNotFoundException;

/**
 *
 * @author bryan
 */
@Stateless
public class SalesManagerSessionBean implements SalesManagerSessionBeanRemote, SalesManagerSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewRoomRate(RoomRateEntity roomRate) {
        em.persist(roomRate);
        em.flush();
        return roomRate.getRoomRateId();
    }

    @Override
    public RoomRateEntity updateRoomRate(Long id, String newName, Double newRatePerNight, Date newStartDate, Date newEndDate) throws RoomRateNotFoundException {
        RoomRateEntity roomRate = em.find(RoomRateEntity.class, id); // Step 1: Retrieve the existing Room Rate

        if (roomRate != null) {
            // Step 2: Modify the fields
            roomRate.setName(newName);
            roomRate.setRatePerNight(newRatePerNight);
            roomRate.setStartDate(newStartDate);
            roomRate.setEndDate(newEndDate);

            // Step 3: Persist the changes
            roomRate = em.merge(roomRate);
        } else {
            throw new RoomRateNotFoundException("Room Rate Id " + id + " does not exist!");
        }

        return roomRate; // Return the updated RoomRate, or null if not found
    }

    @Override
    public void deleteRoomRateEntity(Long id) throws RoomRateNotFoundException {
        RoomRateEntity roomRate = em.find(RoomRateEntity.class, id); // Step 1: Retrieve the existing Room Rate

        if (roomRate != null) {
            em.remove(roomRate);
        } else {
            throw new RoomRateNotFoundException("Room Rate Id " + id + " does not exist!");
        }
    }

    @Override
    public List<RoomRateEntity> viewAllRoomRates() {
        Query query = em.createQuery("SELECT r FROM RoomRateEntity r ORDER BY r.id ASC");
        return query.getResultList();
    }

    @Override
    public RoomRateEntity getRoomRateById(Long id) throws RoomRateNotFoundException {
        RoomRateEntity roomRate = em.find(RoomRateEntity.class, id);
        if (roomRate != null) {
            return roomRate;
        } else {
            throw new RoomRateNotFoundException("Room Rate Id " + id + " does not exist!");
        }
    }
}
