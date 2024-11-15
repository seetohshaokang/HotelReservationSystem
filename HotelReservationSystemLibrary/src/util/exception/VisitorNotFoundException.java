/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author bryan
 */
public class VisitorNotFoundException extends Exception{

    /**
     * Creates a new instance of <code>VisitorNotFoundException</code> without
     * detail message.
     */
    public VisitorNotFoundException() {
    }

    /**
     * Constructs an instance of <code>VisitorNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public VisitorNotFoundException(String msg) {
        super(msg);
    }
}
