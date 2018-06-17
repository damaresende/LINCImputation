/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author damares
 */
public class InvalidMVRateException extends RuntimeException {
    
    public InvalidMVRateException(String msg){
	super(msg);
    }
}
