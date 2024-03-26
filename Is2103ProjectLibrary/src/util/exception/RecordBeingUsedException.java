/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.exception;

/**
 *
 * @author mw
 */

public class RecordBeingUsedException extends Exception {

    public RecordBeingUsedException() {
    }

    public RecordBeingUsedException(String msg) {
        super(msg);
    }
}
