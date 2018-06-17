/**
* The class <code>DoubleData</code>  represents the data used in the
* genetic programming individuals terminals.
*
* @author Oliveira de Resende, Damares
*   e-mail: d.oliveiraresende@gmail.com
*   Federal University of Para (UFPA)
*   Laboratory of Computational Intelligence and Operational Research (LINC)
**/
package evolution;

import ec.gp.*;

public class DoubleData extends GPData {
    /**
	 * Handles the data of double type
	 */
	private static final long serialVersionUID = 251612020161226682L;
	public double x;    // return value

    @Override
    public void copyTo(final GPData gpd) {  // copy my stuff to another DoubleData
    	((DoubleData)gpd).x = x;
    }
}
