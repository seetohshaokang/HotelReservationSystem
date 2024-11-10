/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author shaokangseetoh
 */
public class ExistingRoomException extends Exception {

    /**
     * Creates a new instance of <code>ExistingRoomException</code> without
     * detail message.
     */
    public ExistingRoomException() {
    }

    /**
     * Constructs an instance of <code>ExistingRoomException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ExistingRoomException(String msg) {
        super(msg);
    }
}
