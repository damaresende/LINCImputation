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

    public String compareImputation(String fileName, ConfigurationParser config, String separator) throws IOException {
    	
    	if(fileName.endsWith(".arff")) {
    		fileName = fileName.substring(0, fileName.indexOf(".arff"));
    	}
    	
    	File fTest = new File(config.getOriginalsDir() + fileName + ".arff");
		if(!fTest.exists())
		    return "@Original " + fileName + " NOT FOUND!";
		
		fTest = new File(config.getAmputationDir() + "amp_" + config.getMVRate() + "_" + fileName + ".arff");
		if(!fTest.exists())
		    return "@Amputed amp_" + config.getMVRate() + "_" + fileName + ".arff NOT FOUND!";
		    
		Instances original = FileManager.loadFile(config.getOriginalsDir() + fileName + ".arff");
		Instances amputed = FileManager.loadFile(config.getAmputationDir() + "amp_" + config.getMVRate() + "_" + fileName + ".arff");
		
		String result = "";
		DecimalFormat dec = new DecimalFormat("0.000");
		
		
		for(int i = 0; i < config.getNumFolds(); i++) {
			String name = "imp_" + config.getMVRate() + "_" + fileName + "_" + i;
			
			result += name;
			for(String type : config.getImputeTypes()) {
			    fTest = new File(config.getImputationDir() + "/" + name + "_" + type + ".arff");
			    
			    if(!fTest.exists()) {
			    	result += separator + Double.NaN;
			    	continue;
			    }
			    	
				Instances imputed = FileManager.loadFile(fTest.getAbsolutePath());				
				result += separator + dec.format(calcRMSE(original, imputed, amputed));
			}
			result += "\n";
			
		}
		return result;
    }

    public String compareListOfDatasets(ConfigurationParser config, String separator) throws IOException {
		String result = "Dataset";
		for(String type : config.getImputeTypes())
		    result += separator + type;
		
		result += "\n";
		for(String fileName : config.getFileNames()) {
		    result += compareImputation(fileName, config, separator);
		}
		
		return result;
    }
    
    public String compareListOfDatasetsNormalized(ConfigurationParser config, String separator) throws IOException {
		String result = "Dataset";
		for(String type : config.getImputeTypes())
		    result += separator + type;
		
		result += "\n";
		for(String fileName : config.getFileNames()) {
		    result += compareImputationNormalized(fileName, config, separator);
		}
		
		return result;
    }

    public String compareImputationNormalized(String fileName, ConfigurationParser config, String separator) throws IOException {
		String result = "";
		
		for (String fold : compareImputation(fileName, config, separator).split("\n")) {
			if(fold.startsWith("@")) 
				continue;
			
			String parts[] = fold.split(separator);
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
			result += parts[0];
			
			for(int i = 0; i < values.length; i++) {
			    if(Double.isNaN(values[i])) 
			    	result += separator + Double.NaN;
			    else
			    	result += separator + dec.format(1-(values[i] - min)/(max-min));
			}	
			result += "\n";
	    }  
		return result;
    }
}
