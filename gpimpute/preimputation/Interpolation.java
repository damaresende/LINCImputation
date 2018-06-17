/**
* The class <code>Interpolation</code> has methods to find the base points
* of a gap in a time series in order to interpolate that gap.
*
* @author Oliveira de Resende, Damares
*   e-mail: d.oliveiraresende@gmail.com
*   Federal University of Para (UFPA)
*   Laboratory of Computational Intelligence and Operational Research (LINC)
**/
package preimputation;

import java.util.ArrayList;
import weka.core.Instances;

public class Interpolation {
    
    /**
     * Find the base points of a gap in a time series and interpolate every gap
     * of every attribute except attribute 0 and attribute z.
     * 
     * @param data dataset of the pre-imputation
     * @param z attribute to be ignored
     */
    public static void findBasePoints (Instances data, int z) {
		for(int i = 1; i < data.numAttributes(); i++) {
		    if(i != z) {
			for(int j = 0; j < data.numInstances(); j++) {
			    if(Double.isNaN(data.instance(j).value(i))) {
					int start, end;
					ArrayList<Integer> points = new ArrayList<>();
					if(j == 0) {
					    start = 0;
					    while(Double.isNaN(data.instance(start).value(i)))
					    	start++;
					    end = start + 1;    
					    while(Double.isNaN(data.instance(end).value(i)))
					    	end++;
					    points.add(start);
					    points.add(end);
					    start = -1;
					} else {
					    start = j - 1;
					    end = j + 1;
					    while(end < data.numInstances() && Double.isNaN(data.instance(end).value(i)))
					    	end++;
		
					    int diff = end - start - 1;
		
					    if(start - diff > -1 && start - diff < data.numInstances()-1 
						    && !Double.isNaN(data.instance(start - diff).value(i)))
						points.add(start - diff); //start extreme point (start - diff)
					    if(start > -1 && start < data.numInstances()-1 
						    && !Double.isNaN(data.instance(start).value(i)))
						points.add(start);
					    if(end > -1 && end < data.numInstances()-1 
						    && !Double.isNaN(data.instance(end).value(i)))
						points.add(end);
					    if(end + diff > -1 && end + diff < data.numInstances()-1
						    && !Double.isNaN(data.instance(end+diff).value(i)))
					       points.add(end + diff); //end extreme point (end + diff)
					}
		
					double x[] = new double[points.size()];
					double fx[] = new double[points.size()];
		
					for(int k = 0; k < points.size(); k++) {
					    x[k] = data.instance(points.get(k)).value(0);
					    fx[k] = data.instance(points.get(k)).value(i);
					}
		
					// Interpolation
					Lagrange.interpolate(data, start+1, end-1, i, x, fx);
				    }
				}
		    }
		}
    }
    
    /**
     * Find the base points of a gap in a time series and interpolate every gap
     * of every attribute except attribute 0.
     * 
     * @param data dataset of the pre-imputation
     */
    public static void findBasePoints (Instances data)
    {
		for(int i = 1; i < data.numAttributes(); i++) {
		    for(int j = 0; j < data.numInstances(); j++) {
				if(Double.isNaN(data.instance(j).value(i))) {
				    int start, end;
				    ArrayList<Integer> points = new ArrayList<>();
				    if(j == 0) {
						start = 0;
						while(Double.isNaN(data.instance(start).value(i)))
							start++;
						end = start + 1;    
						while(Double.isNaN(data.instance(end).value(i)))
							end++;
						points.add(start);
						points.add(end);
						start = -1;
				    } else {
						start = j - 1;
						end = j + 1;
						while(end < data.numInstances() && Double.isNaN(data.instance(end).value(i)))
							end++;
			
						int diff = end - start - 1;
			
						if(start - diff > -1 && start - diff < data.numInstances()-1 && !Double.isNaN(data.instance(start - diff).value(i)))
						    points.add(start - diff); //start extreme point (start - diff)
						if(start > -1 && start < data.numInstances()-1 && !Double.isNaN(data.instance(start).value(i)))
						    points.add(start);
						if(end > -1 && end < data.numInstances()-1 && !Double.isNaN(data.instance(end).value(i)))
						    points.add(end);
						if(end + diff > -1 && end + diff < data.numInstances()-1 && !Double.isNaN(data.instance(end+diff).value(i)))
						   points.add(end + diff); //end extreme point (end + diff)
				    }
		
				    double x[] = new double[points.size()];
				    double fx[] = new double[points.size()];
		
				    for(int k = 0; k < points.size(); k++)  {
						x[k] = data.instance(points.get(k)).value(0);
						fx[k] = data.instance(points.get(k)).value(i);
				    }
		
				    // Interpolation
				    Lagrange.interpolate(data, start+1, end-1, i, x, fx);
				}
		    }
		}
    }
}
