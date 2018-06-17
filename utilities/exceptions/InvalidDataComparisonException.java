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
public class InvalidDataComparisonException extends RuntimeException {
    
    public InvalidDataComparisonException(String msg){
	super(msg);
    }
}
