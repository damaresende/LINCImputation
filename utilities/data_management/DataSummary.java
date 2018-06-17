/**
* The class <code>DataSummary</code> has methods that makes a summary of the
* specified data.
*
* @author Oliveira de Resende, Damares
*   e-mail: d.oliveiraresende@gmail.com
*   Federal University of Para (UFPA)
*   Laboratory of Computational Intelligence and Operational Research (LINC)
**/

package data_management;

import essencials.ConfigurationParser;
import essencials.FileManager;
import java.io.IOException;
import weka.core.AttributeStats;
import weka.core.Instances;

/**
 *
 * @author damaresresende
 */
public class DataSummary {
    
    /**
     * Analyze a list of datasets and give a summary of the relevant information: 
     * name, number of attributes, number of instances, number of categorical attributes,
     * number of numeric attributes, number of attributes with missing values, number of
     * categoric attributes with missing values, number of numeric attributes with missing values
     * and number of instances with missing values.
     * 
     * @param config
     * @return 
     * @throws java.io.IOException
     */
    public String statisticalSummary(ConfigurationParser config) throws IOException {
        String fullInfo = "DatasetName,#Atts,#Instances,#CategoricAtts,#NumericAtts"
                        + ",#AttsWithMVs,#CategoricalAttsWithMVs,#NumericAttsWithMVs,"
                        + "#IntancesWithMVs,%InstancesWithMVs,#MVs, %MVs\n";
       
		for(String file : config.getFileNames()) {
		    String info = "";
		    Instances data = FileManager.loadFile(config.getInputDir() + file);
		    data.setClassIndex(data.numAttributes() - 1);
	
		    int num = numInstancesWithMVs(data);
	                
		    info += file +  ", " + data.numAttributes()
			    + ", " + data.numInstances() + ", " + numCategoricalAtts(data)
			    + ", " + numNumericAtts(data) + ", " + numAttsWithMVs(data)
			    + ", " + numCategoricalAttsWithMVs(data) + ", "
			    + numNumericAttsWithMVs(data) + ", " + num + ", " 
			    + num*100.0/data.numInstances() + "%," + numMissingValues(data);
	
		    fullInfo += info + "," + numMissingValues(data)*100.0/(data.numAttributes()*data.numInstances()) + "\n";
	        }
		    
		return fullInfo;
    }
    
    /**
     * Calculates the number of instances with missing values
     * 
     * @param data input dataset
     * @return 
     */
    public static int numInstancesWithMVs (Instances data) {
        int count = 0;
       
        for(int i = 0; i < data.numInstances(); i++) {
            if(data.instance(i).hasMissingValue())
                count++;
        }
        return count;
    }
    
    /**
     * Calculates the number of numeric attributes with missing values
     * 
     * @param data input dataset
     * @return 
     */
    public static int numNumericAttsWithMVs (Instances data) {
        int count = 0;
       
        for(int i = 0; i < data.numAttributes(); i++) {
            AttributeStats stats = data.attributeStats(i);
            if(data.attribute(i).isNumeric() && (stats.missingCount > 0))
                count++;
        }
        return count;
    }
    
    /**
     * Calculates the total number of missing values in the dataset
     * 
     * @param data input dataset
     * @return 
     */
    public static int numMissingValues(Instances data) {
        int num = 0;
        for(int i = 0; i < data.numAttributes(); i++) {
            for(int j = 0; j < data.numInstances(); j++) {
                if(Double.isNaN(data.instance(j).value(i)))
                    num++;
            }
        }
        return num;
    }
    
    /**
     * Calculates the number of categorical attributes with missing values
     * 
     * @param data input dataset
     * @return 
     */
    public static int numCategoricalAttsWithMVs (Instances data) {
        int count = 0;
       
        for(int i = 0; i < data.numAttributes(); i++) {
            AttributeStats stats = data.attributeStats(i);
            if(data.attribute(i).isNominal() && (stats.missingCount > 0))
                count++;
        }
        return count;
    }
    
    /**
     * Calculates the number of attributes with missing values
     * 
     * @param data input dataset
     * @return 
     */
    public static int numAttsWithMVs (Instances data) {
        int count = 0;
       
        for(int i = 0; i < data.numAttributes(); i++) {
            AttributeStats stats = data.attributeStats(i);
            if(stats.missingCount > 0)
                count++;
        }
        return count;
    }
    
    /**
     * Calculates the number of numeric attributes
     * 
     * @param data input dataset
     * @return 
     */
    public static int numNumericAtts (Instances data) {
        int count = 0;
        for(int i = 0; i < data.numAttributes(); i++) {
            if(data.attribute(i).isNumeric())
                count++;
        }
        return count;
    }
    
    /**
     * Calculates the number of categorical attributes
     * 
     * @param data input dataset
     * @return 
     */
    public static int numCategoricalAtts (Instances data) {
        int count = 0;
        for(int i = 0; i < data.numAttributes(); i++) {
            if(data.attribute(i).isNominal())
                count++;
        }
        return count;
    }
}
