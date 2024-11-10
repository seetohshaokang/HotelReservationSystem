/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session;

import entity.RoomEntity;
import entity.RoomTypeEntity;
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
import util.exception.ExistingRoomException;
import util.exception.NoExistingRoomException;
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
    public RoomEntity createNewRoom(RoomTypeName rtName, Integer floor, Integer sequence, RoomStatus roomStatus) throws RoomTypeNotFoundException, ExistingRoomException {
        try {
            // There is existing room
            String roomNumber = String.format("%02d%02d", floor, sequence);
            RoomEntity existingRoom = searchRoomByRoomNumber(roomNumber);
            if(existingRoom != null) {
                throw new ExistingRoomException("There is an existing room with room number: " + roomNumber);
            }
            return null; // This is to remove compilation warning, but actually not needed.
        } catch (NoExistingRoomException ex) {
            // No existing room
            // Find roomType
            RoomTypeEntity rt = roomTypeEntitySessionBean.getRoomTypeByName(rtName);
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

    @Override
    public RoomEntity searchRoomByRoomNumber(String roomNumber) throws NoExistingRoomException {
        Query query = em.createQuery("SELECT r FROM RoomEntity r WHERE r.roomNumber = :inRoomNumber", RoomEntity.class);
        query.setParameter("inRoomNumber", roomNumber);
        try {
            return (RoomEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new NoExistingRoomException ("Room with room number " + roomNumber + " does not exist!");
        }
    }

    @Override
    public List<RoomEntity> viewAllRooms() {
        Query query = em.createQuery("SELECT r FROM RoomEntity r");
        
        return query.getResultList();
    }
}
