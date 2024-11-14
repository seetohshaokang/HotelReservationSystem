/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless.helper;

import entity.ExceptionReportEntity;
import entity.RoomEntity;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author shaokangseetoh
 */
@Remote
public interface ExceptionReportSessionBeanRemote {
    
    public Long createTypeOneException(RoomEntity oldRoom , RoomEntity upgradedRoom);
    
    public Long createTypeTwoException(RoomEntity oldRoom);
    
    public List<ExceptionReportEntity> generateExceptionReport(LocalDate date);
}
