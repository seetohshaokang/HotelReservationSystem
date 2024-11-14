/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import ejb.session.RoomEntitySessionBeanRemote;
import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RoomStatus;
import util.enumeration.RoomTypeName;
import util.exception.DisabledException;
import util.exception.ExistingRoomException;
import util.exception.NoExistingRoomException;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author shaokangseetoh
 */
@Stateless
public class RoomEntitySessionBean implements RoomEntitySessionBeanRemote, RoomEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @EJB
    private RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBean;

    // Might need to include validation checks to see if room already exists
    @Override
    public RoomEntity createNewRoom(RoomTypeName rtName, Integer floor, Integer sequence, RoomStatus roomStatus) throws RoomTypeNotFoundException, ExistingRoomException, DisabledException {
        try {

            // There is existing room
            String roomNumber = String.format("%02d%02d", floor, sequence);
            RoomEntity existingRoom = searchRoomByRoomNumber(roomNumber);
            if (existingRoom != null) {
                throw new ExistingRoomException("There is an existing room with room number: " + roomNumber);
            }
            return null; // This is to remove compilation warning, but actually not needed.
        } catch (NoExistingRoomException ex) {
            // No existing room
            // Find roomType
            RoomTypeEntity rt = roomTypeEntitySessionBean.getRoomTypeByName(rtName);

            // Check if room type is disabled
            if (rt.getIsDisabled() == true) {
                throw new DisabledException("Room Type is current disabled!");
            } else {
                // Instantiate room entity
                RoomEntity newRoom = new RoomEntity(rt, floor, sequence, roomStatus);
                // Add room to room type
                rt.getRooms().add(newRoom);
                // persist room
                em.persist(newRoom);
                em.flush();
                // Lazy fetching of roomtype
                newRoom.getRoomType();
                return newRoom;
            }
        }
    }

    @Override
    public RoomEntity searchRoomByRoomNumber(String roomNumber) throws NoExistingRoomException {
        Query query = em.createQuery("SELECT r FROM RoomEntity r WHERE r.roomNumber = :inRoomNumber", RoomEntity.class);
        query.setParameter("inRoomNumber", roomNumber);
        try {
            return (RoomEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new NoExistingRoomException("Room with room number " + roomNumber + " does not exist!");
        }
    }

    @Override
    public RoomEntity updateRoom(Long roomId, Long roomTypeId, String newRoomNumber, RoomStatus newStatus) throws RoomNotFoundException, RoomTypeNotFoundException {
        // Retrieve room 
        RoomEntity retrievedRoom = em.find(RoomEntity.class, roomId);
        // Retrieve new room type
        RoomTypeEntity newRoomType = em.find(RoomTypeEntity.class, roomTypeId);

        if (retrievedRoom == null) {
            throw new RoomNotFoundException("Room with id: " + roomId + " does not exist!");
        } else if (newRoomType == null) {
            throw new RoomTypeNotFoundException("Room type with id: " + roomTypeId + " does not exist!");
        } else {
            // Retrieve old roomtype
            RoomTypeEntity oldRoomType = retrievedRoom.getRoomType();
            // Remove existing room
            oldRoomType.getRooms().remove(retrievedRoom);
            // Set new attributes
            retrievedRoom.setRoomNumber(newRoomNumber);
            retrievedRoom.setRoomType(newRoomType);
            retrievedRoom.setStatus(newStatus);
            // Add room to new room type
            newRoomType.getRooms().add(retrievedRoom);
            em.flush();
            return retrievedRoom;
        }
    }

    @Override
    public List<RoomEntity> viewAllRooms() {
        Query query = em.createQuery("SELECT r FROM RoomEntity r");
        return query.getResultList();
    }


    public boolean isRoomInUse(Long roomId) throws RoomNotFoundException {
        RoomEntity room = em.find(RoomEntity.class, roomId);
        if (room == null) {
            throw new RoomNotFoundException("Room ID " + roomId + " does not exist!");
        }

        // Check if room has active reservations or is currently occupied
        if (room.getStatus() == RoomStatus.OCCUPIED || !room.getRoomReservations().isEmpty()) {
            return true;
        }

        return false;
    }

    @Override
    public void disableRoom(Long roomId) throws RoomNotFoundException {
        RoomEntity room = em.find(RoomEntity.class, roomId);
        if (room == null) {
            throw new RoomNotFoundException("Room ID " + roomId + " does not exist!");
        }

        room.setStatus(RoomStatus.DISABLED); // Assuming DISABLED status exists in RoomStatus enum
        em.merge(room);
        em.flush();
    }

    @Override
    public void deleteRoom(Long roomId) throws RoomNotFoundException {
        RoomEntity room = em.find(RoomEntity.class, roomId);
        if (room == null) {
            throw new RoomNotFoundException("Room ID " + roomId + " does not exist!");
        }

        // Check if room is not in use before deleting
        if (isRoomInUse(roomId)) {
            throw new IllegalStateException("Room is currently in use and cannot be deleted.");
        }

        em.remove(room);
        em.flush();
    }

    @Override
    public List<ReservationEntity> retrieveOverlappingReservations(LocalDate checkInDate, LocalDate checkOutDate, RoomTypeName roomTypeName) {
        // Query the list of overlappingreservations
        Query query = em.createQuery(
                "SELECT r FROM ReservationEntity r "
                + "WHERE r.roomType.roomTypeName = :roomTypeName "
                + "AND r.checkInDate < :checkOutDate "
                + "AND r.checkOutDate > :checkInDate", ReservationEntity.class);
        query.setParameter("checkInDate", checkInDate);
        query.setParameter("checkOutDate", checkOutDate);
        query.setParameter("roomTypeName", roomTypeName);

        // Retrieve list of reservations that overlap with the specified timeframe
        List<ReservationEntity> overlappingReservations = query.getResultList();
        return overlappingReservations;
    }

    @Override
    public List<RoomEntity> retrieveAvailableRooms(RoomTypeName roomTypeName) {
        Query query = em.createQuery("SELECT r FROM RoomEntity r WHERE r.roomType.roomTypeName = :roomTypeName AND r.status = :status");
        query.setParameter("roomTypeName", roomTypeName);
        query.setParameter("status", RoomStatus.AVAILABLE);

        return query.getResultList();
    }
}
