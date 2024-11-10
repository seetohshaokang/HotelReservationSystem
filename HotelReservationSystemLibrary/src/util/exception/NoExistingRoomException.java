/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author shaokangseetoh
 */
public class NoExistingRoomException extends Exception {

    /**
     * Creates a new instance of <code>NoExistingRoomException</code> without
     * detail message.
     */
    public NoExistingRoomException() {
    }

    /**
     * Constructs an instance of <code>NoExistingRoomException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoExistingRoomException(String msg) {
        super(msg);
    }
}
