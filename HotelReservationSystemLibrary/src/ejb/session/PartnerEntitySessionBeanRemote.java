/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session;

import entity.PartnerEntity;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author shaokangseetoh
 */
@Remote
public interface PartnerEntitySessionBeanRemote {
    // System Administrator
    public Long createNewPartner(PartnerEntity partnerEntity);
    // System Administrator
    public List<PartnerEntity> viewAllPartners();
}
