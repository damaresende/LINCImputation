/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_management;

import essencials.ConfigurationParser;
import essencials.FileManager;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.abs;
import java.text.DecimalFormat;
import weka.core.Instances;

/**
 *
 * @author damares
 */
public class AutocorrelationEvaluator {

    public String compareAutocorrelation(String fileName, ConfigurationParser config, String separator) throws IOException {
	
    	if(fileName.endsWith(".arff")) {
    		fileName = fileName.substring(0, fileName.indexOf(".arff"));
    	}
    	
    	File fTest = new File(config.getOriginalsDir() + fileName + ".arff");
		if(!fTest.exists())
		    return "@Original " + fileName + " NOT FOUND!";
		    
		Instances original = FileManager.loadFile(config.getOriginalsDir() + fileName + ".arff");
		
		String result = "";
		DecimalFormat dec = new DecimalFormat("0.000");
		
		
		for(int i = 0; i < config.getNumFolds(); i++) {
			String name = "imp_" + config.getMVRate() + "_" + fileName + "_" + i;
			
			result += name;
			for(String type : config.getImputeTypes()) {
			    fTest = new File(config.getImputationDir() + File.separator + name + "_" + type + ".arff");
			    
			    if(!fTest.exists()) {
			    	result += separator + Double.NaN;
			    	continue;
			    }
			    	
				Instances imputed = FileManager.loadFile(fTest.getAbsolutePath());				
				result += separator + dec.format(autocorrelationAbsDifference(imputed, original, config.getH()));
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
		    result += compareAutocorrelation(fileName, config, separator);
		}
		
		return result;
    }
    
    public double autocorrelationAbsDifference(Instances imputed, Instances original, int H) {
    	return abs(calculateAutocorrelation(imputed, H) - calculateAutocorrelation(original, H));
    }
    
    public double calculateAutocorrelation(Instances data, int H) {
		double sum = 0;
		for(int i = 0; i < data.numAttributes(); i++) {
		    sum += acor(data,i,avg(data,i),H,0,data.numInstances()-1);
		}
		return sum/data.numAttributes();
    }
    
    public static double avg (Instances data, int att) {
        double sum = 0;
        for(int i = 0; i < data.numInstances(); i++)
            sum += data.instance(i).value(att);
        
        return sum/data.numInstances();
    }
    
    public static double var (Instances data, int att, double avg) {
        double sum = 0;
        for(int i = 0; i < data.numInstances(); i++)
            sum += (data.instance(i).value(att) - avg)*(data.instance(i).value(att) - avg);
        
        return sum/(data.numInstances()-1);
    }
    
    public static double acor (Instances data, int att, double avg, int h, int n1, int n2) {
        return acov(data,att,avg,h,n1,n2)/acov(data,att,avg,0,0,n2);
    }
    
    public static double acov (Instances data, int att, double avg, int h, int n1, int n2) {
        int lh = abs(h);
        double sum = 0;
        for(int i = n1; i <= (n2 - lh); i++)
            sum += (data.instance(i+lh).value(att) - avg)*(data.instance(i).value(att) - avg);
        
        return sum/(n2 - n1 + 1);
    }
    
}
