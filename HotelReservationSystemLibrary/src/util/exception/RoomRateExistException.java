/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author bryan
 */
public class RoomRateExistException extends Exception{

    /**
     * Creates a new instance of <code>RoomRateExistException</code> without
     * detail message.
     */
    public RoomRateExistException() {
    }

    /**
     * Constructs an instance of <code>RoomRateExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomRateExistException(String msg) {
        super(msg);
    }
}
