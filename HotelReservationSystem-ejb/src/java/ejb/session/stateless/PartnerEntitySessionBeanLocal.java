/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author shaokangseetoh
 */
@Local
public interface PartnerEntitySessionBeanLocal {
    
    // System Administrator
    public Long createNewPartner(PartnerEntity partnerEntity);
    // System Administrator
    public List<PartnerEntity> viewAllPartners();
    
    // Method to update partner reservations;
    
}
