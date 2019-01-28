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

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;

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
     * categorical attributes with missing values, number of numeric attributes with missing values
     * and number of instances with missing values.
     * 
     * @param config configuration
     * @return string with the summary
     * @throws java.io.IOException
     */
    public static String statisticalSummary(ConfigurationParser config, String separator) throws IOException {
        String fullInfo = getHeader(separator) + "\n";
       
        File folder = new File(config.getInputDir());
        File[] listOfFiles = folder.listFiles();
        
		for(String fileName : config.getFileNames()) {
			for(File file : listOfFiles) {
				if(file.getName().contains(fileName)) {
				    Instances data = FileManager.loadFile(file.getAbsolutePath());
				    String info = datasetStatisticalSummary(data, separator);
			
				    fullInfo += info + "\n";
		        }
			}
		}
		    
		return fullInfo;
    }
    
    /**
     * Gets the header with the name of the columns that in the summary table
     * 
     * @param separator column separator
     * @return string with the header
     * @throws java.io.IOException
     */
    public static String getHeader(String separator) {
        return MessageFormat.format("DatasetName{0}#Atts{0}#Instances{0}#CategoricAtts{0}" +
        		"#NumericAtts{0}#AttsWithMVs{0}#CategoricalAttsWithMVs{0}#NumericAttsWithMVs{0}" + 
        		"#IntancesWithMVs{0}%InstancesWithMVs{0}#MVs{0}%MVs", separator);
    }
    
    /**
     * Gets the information of a dataset and stores it in a string where each column
     * is separated by the indicated separator. The string includes the following data:
     * dataset name, number of attributes, number of instances, number of categorical attributes,
     * number of numerical attributes, number of attributes with missing values, number of categorical
     * attributes with missing values, number of numerical attributes with missing values, number of
     * instances with missing values, percentage of instances with missing values, number of missing values,
     * percentage of missing values
     * 
     * @param separator column separator
     * @return string with the information about the dataset.
     * @throws java.io.IOException
     */
    public static String datasetStatisticalSummary(Instances data, String separator) {
    	String info = "";
    	data.setClassIndex(data.numAttributes() - 1);
	    int num = numInstancesWithMVs(data);
	    
	    info += data.relationName() +  separator + data.numAttributes()
	    + separator + data.numInstances() + separator + numCategoricalAtts(data)
	    + separator + numNumericAtts(data) + separator + numAttsWithMVs(data)
	    + separator + numCategoricalAttsWithMVs(data) + separator
	    + numNumericAttsWithMVs(data) + separator + num + separator 
	    + round(num*100.0/data.numInstances(), 3) + separator 
	    + numMissingValues(data);

	    info += separator + round(percentageOfMVs(data),3);
	    
    	return info;
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
    
    /**
     * Rounds double values by setting it to a specific number of decimal places
     * 
     * @param value value
     * @param places number of decimal places
     * @return 
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    /**
     * Calculates the percentage of missing values
     * 
     * @param data input dataset
     * @return 
     */
    public static double percentageOfMVs(Instances data) {
        return numMissingValues(data)*100.0/(data.numAttributes()*data.numInstances());
    }
}
