/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_management;

import essencials.ConfigurationParser;
import essencials.FileManager;
import exceptions.InvalidDataComparisonException;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import weka.core.Instances;

/**
 *
 * @author damares
 */
public class RMSEEvaluator {

    public double calcRMSE(Instances imputed, Instances original, Instances map) {
		if(imputed.numAttributes() != original.numAttributes())
		    throw new InvalidDataComparisonException("Number of attributes are different!");
		
		if(imputed.numInstances() != original.numInstances())
		    throw new InvalidDataComparisonException("Number of instances are different!");
		
		int mvCount = 1;
		double sum = 0;
		
		for(int i = 0; i < imputed.numAttributes(); i++) {
		    for(int j = 0; j < imputed.numInstances(); j++) {
				if(map.instance(j).isMissing(i)) {
				    mvCount++;
				    sum += (imputed.instance(j).value(i) - original.instance(j).value(i)) * 
					    (imputed.instance(j).value(i) - original.instance(j).value(i));
				}
		    }
		}
	
		return sum/mvCount;
    }

    public String compareImputation(String fileName, ConfigurationParser config) throws IOException {
	
		File fTest = new File(config.getOriginalsDir() + fileName + ".arff");
		if(!fTest.exists())
		    return "@Original " + fileName + " NOT FOUND!";
		
		fTest = new File(config.getAmputationDir() + "amp_" + config.getMVRate() + "_" + fileName + ".arff");
		if(!fTest.exists())
		    return "@Amputed amp_" + config.getMVRate() + "_" + fileName + ".arff NOT FOUND!";
		    
		Instances original = FileManager.loadFile(config.getOriginalsDir() + fileName + ".arff");
		Instances amputed = FileManager.loadFile(config.getAmputationDir() + "amp_" + config.getMVRate() + "_" + fileName + ".arff");
		
		String result = fileName;
		DecimalFormat dec = new DecimalFormat("0.000");
		
		for(String type : config.getImputeTypes()) {
		    fTest = new File(config.getImputationDir() + "imp_" + config.getMVRate() + "_" + fileName + "-" + type + ".arff");
		    if(!fTest.exists())
		    	result += "," + Double.NaN;
		    else {
				Instances imputed = FileManager.loadFile(config.getImputationDir() + "imp_" 
					+ config.getMVRate() + "_" + fileName + "-" + type + ".arff");
		
				result += "," + dec.format(calcRMSE(original, imputed, amputed));
		    }
		}
	
		return result;
    }

    public String compareListOfDatasets(ConfigurationParser config) throws IOException {
		String result = "Dataset";
		for(String type : config.getImputeTypes())
		    result += "," + type;
		
		for(String fileName : config.getFileNames()) {
		    result += "\n" + compareImputation(fileName, config);
		}
		
		return result;
    }
    
    public String compareListOfDatasetsNormalized(ConfigurationParser config) throws IOException {
		String result = "Dataset";
		for(String type : config.getImputeTypes())
		    result += "," + type;
		
		for(String fileName : config.getFileNames()) {
		    result += "\n" + compareImputationNormalized(fileName, config);
		}
		
		return result;
    }

    public String compareImputationNormalized(String fileName, ConfigurationParser config) throws IOException {
		String result = compareImputation(fileName, config);
		
		if(result.startsWith("@")) 
		    return result;
		
		String parts[] = result.split(",");
		double values[] = new double[parts.length-1];
		double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
		
		for(int i = 1; i < parts.length; i++) {
		    values[i-1] = Double.parseDouble(parts[i]);
		    if(values[i-1] < min)
			min = values[i-1];
		    if(values[i-1] > max)
			max = values[i-1];
		}
	
		DecimalFormat dec = new DecimalFormat("0.000");
		result = fileName;
		for(int i = 0; i < values.length; i++) {
		    if(Double.isNaN(values[i])) 
			result += "," + Double.NaN;
		    else
			result += "," + dec.format(1-(values[i] - min)/(max-min));
		}
		return result;
    }  
}
