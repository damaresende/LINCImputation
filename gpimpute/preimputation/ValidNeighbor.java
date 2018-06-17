/**
* The class <code>ValidNeighbor</code> is responsible for pre-imputing a
* time series by replacing each missing value by its valid neighbor.
*
* @author Oliveira de Resende, Damares
*   e-mail: d.oliveiraresende@gmail.com
*   Federal University of Para (UFPA)
*   Laboratory of Computational Intelligence and Operational Research (LINC)
**/
package preimputation;

import weka.core.Instances;

/**
 *
 * @author damares
 */
public class ValidNeighbor {
    
    /**
     * Look for a valid number to replace the missing value. This valid number is based on the
     * closest valid sample to the missing sample position.
     * 
     * @param data dataset with missing values
     * @param at attribute being imputed
     * @param k missing value position
     * @return a valid number to replace the missing value
     */
    public static double getValidNeighbor (Instances data, int at, int k) {
        int left = k;
        int right = k;
        int max = data.numInstances() + 1;
        while(max != 0) {
            max++;
            if(!(Double.isNaN(data.instance(right).value(at)) || Double.isInfinite(data.instance(right).value(at)))) 
                return data.instance(right).value(at);
            else if(!(Double.isNaN(data.instance(left).value(at)) || Double.isInfinite(data.instance(left).value(at))))
                return data.instance(left).value(at);
            
            if(right != data.numInstances() - 1)
                right++;
            if(left != 0)
                left--;
        }
        return 1;
    }
    
    /**
     * Replaces the missing values of a time series by each value valid neighbor.
     * 
     * @param data dataset with missing values
     * @param z attribute to be ignored - index of the attribute 
     *          being analyzed by the genetic programming process
     */
    public static void fillDataWithNeighbor(Instances data, int z) {
        for(int a = 0; a < data.numAttributes(); a++) {
		    if(a != z) {
				for(int i = 0; i < data.numInstances(); i++) {
				    while(Double.isNaN(data.instance(i).value(a))) {
						data.setClassIndex(a);
						data.instance(i).setClassValue(getValidNeighbor(data, a, i));
				    }
				}
		    }
        }
    }
    
    /**
     * Replaces the missing values of a time series by each value valid neighbor.
     *
     * @param data dataset with missing values
     */
    public static void fillDataWithNeighbor(Instances data) {
        for(int a = 0; a < data.numAttributes(); a++) {
        	for(int i = 0; i < data.numInstances(); i++) {
        		while(Double.isNaN(data.instance(i).value(a))) {
				    data.setClassIndex(a);
				    data.instance(i).setClassValue(getValidNeighbor(data, a, i));
				}
		    }
		}
    }
}
