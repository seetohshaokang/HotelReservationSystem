/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author shaokangseetoh
 */
public class InvalidInputException extends Exception {

    /**
     * Creates a new instance of <code>InvalidInputException</code> without
     * detail message.
     */
    public InvalidInputException() {
    }

    /**
     * Constructs an instance of <code>InvalidInputException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidInputException(String msg) {
        super(msg);
    }
}
