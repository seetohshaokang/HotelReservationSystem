/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import ejb.session.PartnerEntitySessionBeanRemote;
import entity.EmployeeEntity;
import entity.PartnerEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EmployeeExistException;
import util.exception.EmployeeNotFoundException;
import util.exception.PartnerExistException;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author shaokangseetoh
 */
@Stateless
public class PartnerEntitySessionBean implements PartnerEntitySessionBeanRemote, PartnerEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewPartner(PartnerEntity newPartner) throws PartnerExistException {
        PartnerEntity p = null;
        try {
            p = retrievePartnerByUsername(newPartner.getUsername());
        } catch (PartnerNotFoundException e) {

        }
        if (p == null) {
            em.persist(newPartner);
            em.flush();
        } else {
            throw new PartnerExistException("Username already exists.");
        }

        return newPartner.getPartnerId();
    }

    @Override
    public PartnerEntity retrievePartnerByUsername(String username) throws PartnerNotFoundException {

        Query query = em.createQuery("SELECT e FROM PartnerEntity e WHERE e.username = :username", PartnerEntity.class);
        query.setParameter("username", username);

        try {
            return (PartnerEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new PartnerNotFoundException("Partner Username " + username + " does not exits!");
        }

    }

    @Override
    public List<PartnerEntity> viewAllPartners() {
        Query query = em.createQuery("SELECT p FROM PartnerEntity p ");
        return query.getResultList();
    }

}
