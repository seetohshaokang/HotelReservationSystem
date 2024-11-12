/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author shaokangseetoh
 */
public class DisabledException extends Exception{

    /**
     * Creates a new instance of <code>DisabledException</code> without detail
     * message.
     */
    public DisabledException() {
    }

    /**
     * Constructs an instance of <code>DisabledException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DisabledException(String msg) {
        super(msg);
    }
}
