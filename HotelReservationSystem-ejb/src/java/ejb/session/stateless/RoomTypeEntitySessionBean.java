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
        return newRoomType.getRoomTypeName().toString();
    }

    // JPQL Query
    @Override
    public RoomTypeEntity getRoomTypeByName(RoomTypeName name) throws RoomTypeNotFoundException {
        Query query = em.createQuery("SELECT rt FROM RoomTypeEntity rt WHERE rt.roomTypeName = :rtName", RoomTypeEntity.class);
        query.setParameter("rtName", name);

        try {
            return (RoomTypeEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RoomTypeNotFoundException("Room Type: " + name.toString() + " does not exist");
        }
    }

    public RoomTypeEntity updateRoomType(Long roomTypeId, String newDescription, Double newSize, String newBed, Integer newCapacity, List<String> newAmenities,
            RoomTypeName newNextHigherRoomTypeName) 
            throws RoomTypeNotFoundException {
        RoomTypeEntity retrievedRoomType = em.find(RoomTypeEntity.class, roomTypeId); // returns null if not found
        if (retrievedRoomType == null) {
            throw new RoomTypeNotFoundException("Room Type with ID " + roomTypeId + " not found.");
        } else {
            // Update fields
            retrievedRoomType.setDescription(newDescription);
            retrievedRoomType.setSize(newSize);
            retrievedRoomType.setBed(newBed);
            retrievedRoomType.setCapacity(newCapacity);
            retrievedRoomType.setAmenities(newAmenities);

            // Update next higher room type if specified
            if (newNextHigherRoomTypeName != null) {
                try {
                    RoomTypeEntity nextHigherRoomType = getRoomTypeByName(newNextHigherRoomTypeName);
                    retrievedRoomType.setNextHigherRoomTypeName(nextHigherRoomType.getRoomTypeName());
                } catch (RoomTypeNotFoundException e) {
                    throw new RoomTypeNotFoundException("Next higher room type with name " + newNextHigherRoomTypeName + " not found.");
                }
            } else {
                retrievedRoomType.setNextHigherRoomTypeName(null); // Set to null if no higher room type is specified
            }

            em.flush();
            return retrievedRoomType;
        }
    }

    @Override
    public List<RoomTypeEntity> viewAllRoomTypes() {
        Query query = em.createQuery("SELECT rt FROM RoomTypeEntity rt");
        return query.getResultList();
    }
    
    // Method to check if the room type is in use
    public boolean isRoomTypeInUse(Long roomTypeId) {
        Query query = em.createQuery("SELECT COUNT(r) FROM RoomEntity r WHERE r.roomType.roomTypeId = :roomTypeId");
        query.setParameter("roomTypeId", roomTypeId);
        Long count = (Long) query.getSingleResult();
        return count > 0;
    }

    // Method to delete or disable a room type based on its usage
    public void deleteRoomType(Long roomTypeId) throws RoomTypeNotFoundException {
        RoomTypeEntity roomType = em.find(RoomTypeEntity.class, roomTypeId);
        if (roomType == null) {
            throw new RoomTypeNotFoundException("Room type ID " + roomTypeId + " does not exist.");
        }

        if (isRoomTypeInUse(roomTypeId)) {
            // Mark room type as disabled if it is in use
            roomType.setIsDisabled(true); // Assuming RoomTypeEntity has an 'isDisabled' field
            em.merge(roomType);
            System.out.println("Room type " + roomType.getRoomTypeName() + " is in use and has been disabled.");
        } else {
            // Delete the room type if it is not in use
            em.remove(roomType);
            System.out.println("Room type " + roomType.getRoomTypeName() + " has been deleted.");
        }
        em.flush();
    }

}
