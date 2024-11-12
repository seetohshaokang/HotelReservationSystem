/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import ejb.session.RoomTypeEntitySessionBeanRemote;
import entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RoomTypeName;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author shaokangseetoh
 */
@Stateless
public class RoomTypeEntitySessionBean implements RoomTypeEntitySessionBeanRemote, RoomTypeEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public String createNewRoomType(RoomTypeEntity newRoomType) {
        em.persist(newRoomType);
        em.flush();
        return newRoomType.getName().toString();
    }

    // JPQL Query
    @Override
    public RoomTypeEntity getRoomTypeByName(RoomTypeName name) throws RoomTypeNotFoundException {
        Query query = em.createQuery("SELECT rt FROM RoomTypeEntity rt WHERE rt.name = :rtName", RoomTypeEntity.class);
        query.setParameter("rtName", name);

        try {
            return (RoomTypeEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RoomTypeNotFoundException("Room Type: " + name.toString() + " does not exist");
        }
    }

    @Override
    public RoomTypeEntity updateRoomType(Long roomTypeId, String newDescription, Double newSize, String newBed, Integer newCapacity, List<String> newAmenities) throws RoomTypeNotFoundException {
       RoomTypeEntity retrievedRoomType = em.find(RoomTypeEntity.class, roomTypeId); // returns null if not found
        if(retrievedRoomType == null) {
            throw new RoomTypeNotFoundException("Room Type is not found");
        } else {
            retrievedRoomType.setDescription(newDescription);
            retrievedRoomType.setSize(newSize);
            retrievedRoomType.setBed(newBed);
            retrievedRoomType.setCapacity(newCapacity);
            retrievedRoomType.setAmenities(newAmenities);
            em.flush();
            return retrievedRoomType;
        }
    }

    @Override
    public List<RoomTypeEntity> viewAllRoomTypes() {
        Query query = em.createQuery("SELECT rt FROM RoomTypeEntity rt");
        return query.getResultList();
    }

}
