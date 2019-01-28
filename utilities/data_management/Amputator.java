/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_management;

import essencials.ConfigurationParser;
import essencials.FileManager;
import java.io.IOException;
import weka.core.Debug;
import weka.core.Instances;

/**
 *
 * @author damares
 */
public class Amputator {
    public Instances amputeDatasetTimeSeries(Instances dataset, int mvRate) {
		Instances data = new Instances(dataset);
		
		int i, j;
	        int n = data.numAttributes()-1;
		
		int total = (int)(n*data.numInstances()*mvRate/100);
	        int nMissing = 0;
		
		while(nMissing < total) {
		    Debug.Random rd = new Debug.Random();
		    i = rd.nextInt(data.numInstances());
		    j = rd.nextInt(data.numAttributes()-1) + 1;
		    
		    if(!Double.isNaN(data.instance(i).value(j))) {
				data.setClassIndex(j);
				data.instance(i).setMissing(j);
				nMissing++;
		    }
		}
		
		data.setRelationName("amp_" + mvRate + "_" + data.relationName());
		return data;
    }
    
    public Instances amputeDatasetClassification(Instances dataset, int mvRate) {
		Instances data = new Instances(dataset);
		
		int i, j;
	        int n = data.numAttributes()-1;
		
		int total = (int)(n*data.numInstances()*mvRate/100);
	        int nMissing = 0;
		
		while(nMissing < total) {
		    Debug.Random rd = new Debug.Random();
		    i = rd.nextInt(data.numInstances());
		    j = rd.nextInt(data.numAttributes()-2);
		    
		    if(!Double.isNaN(data.instance(i).value(j))) {
				data.setClassIndex(j);
				data.instance(i).setMissing(j);
				nMissing++;
		    }
		}
		data.setRelationName("amp_" + mvRate + "_" + data.relationName());
		return data;
    }
    
    public void amputeListOfFiles (ConfigurationParser info) throws IOException {
		for(String file : info.getFileNames()) {
		    Instances data = FileManager.loadFile(info.getOriginalsDir() + file);
		    if(info.isClassification()) {
				Instances newData = amputeDatasetClassification(data, info.getMVRate());
				FileManager.saveDataset(newData, (info.getAmputationDir() + "amp_" + info.getMVRate() + "_" +  file + ".arff"));
				System.out.println("Amputed " + info.getMVRate() + "% of: "+ file + ".arff");
		    } else if(info.isTimeSeries()) {
				Instances newData = amputeDatasetTimeSeries(data, info.getMVRate());
				FileManager.saveDataset(newData, (info.getAmputationDir() + "amp_" + info.getMVRate() + "_" +  file + ".arff"));
				System.out.println("Amputed " + info.getMVRate() + "% of: "+ file + ".arff");
		    } else {
				System.err.println("Error! File type not identified. "
					+ "It must be classification or time series.");
				break;
		    }
		}
    }
}
