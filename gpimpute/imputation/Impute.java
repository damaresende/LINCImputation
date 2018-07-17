/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imputation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

import essencials.FileManager;
import evolution.EvolutionProcess;
import evolution.OneImpute;
import preimputation.Interpolation;
import preimputation.ValidNeighbor;
import util.TriplicateDataset;
import weka.core.AttributeStats;
import weka.core.Instances;

/**
 *
 * @author damares
 */
public class Impute implements Imputation {

    @Override
    public Instances runGP(Instances dataset, boolean saveFitness, int fold, String resultPath) {
		Instances result = new Instances(dataset);
		
		OneImpute.mvdataset = result;
		Instances tmpData = new Instances(dataset);
		ValidNeighbor.fillDataWithNeighbor(tmpData);
		
		for(int i = 1; i < dataset.numAttributes(); i++) {
		    Instances imputed = new Instances(result);
		    AttributeStats stats = result.attributeStats(i);
		    
		    if(stats.missingCount > 0) {
                OneImpute.train = new Instances(imputed);
                OneImpute.test = removeInstancesWithMV(imputed, i);
			
                // set evaluated feature
                OneImpute.eval_feature_idx = i;
			
				try {
				    EvolutionProcess.evolveGP(result,tmpData, i);
				    if(saveFitness) saveFitnessStatus(dataset.relationName(),i,fold,"-NGP");
				} catch (IOException ex) {
				    Logger.getLogger(Impute.class.getName()).log(Level.SEVERE, null, ex);
				}
		    }
		}
		try {
			File statusPath = new File(resultPath + "/status/");
			statusPath.mkdir();
		    
			FileManager.copyFile(System.getProperty("user.dir") + "/files/params/out.stat", 
			statusPath.getAbsolutePath() + "/output_" + result.relationName() + "_fold_" + fold + ".stat");
			
			File file = new File(System.getProperty("user.dir") + "/files/params/out.stat");
		    Files.deleteIfExists(file.toPath());
	    
	    } catch (IOException ex) {
		    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	    
		return result;
    }
    
    @Override
    public Instances removeInstancesWithMV(Instances dataset, int att) {
		Instances ignoreMissingData = new Instances(dataset);
		for(int i = 0; i < ignoreMissingData.numInstances(); i++) {
		    if(ignoreMissingData.instance(i).isMissing(att))
		    	ignoreMissingData.delete(i--);
		}
		return ignoreMissingData;
    }

    @Override
    public Instances runLGP(Instances dataset, boolean saveFitness, int fold, String resultPath) {
		Instances result = new Instances(dataset);
		TriplicateDataset td = new TriplicateDataset(result);
		td.triplicateDataset(result);
		
		OneImpute.mvdataset = result;
		Instances tmpData = new Instances(dataset);
		td.triplicateDataset(tmpData);
		Interpolation.findBasePoints(tmpData);
		
		for(int i = 1; i < dataset.numAttributes(); i++) {
		    Instances imputed = new Instances(result);
		    AttributeStats stats = result.attributeStats(i);
		    
		    if(stats.missingCount > 0) {
                OneImpute.train = new Instances(imputed);
                OneImpute.test = removeInstancesWithMV(imputed, i);
			
                // set evaluated feature
                OneImpute.eval_feature_idx = i;
			
				try {
				    EvolutionProcess.evolveGP(result,tmpData, i);
				    if(saveFitness) saveFitnessStatus(dataset.relationName(),i,fold,"-LGP");
				} catch (IOException ex) {
				    Logger.getLogger(Impute.class.getName()).log(Level.SEVERE, null, ex);
				}
		    }
		}
		td.reduceData(result);
		
		try {
			File statusPath = new File(resultPath + "/status/");
			statusPath.mkdir();
		    
			FileManager.copyFile(System.getProperty("user.dir") + "/files/params/out.stat", 
			statusPath.getAbsolutePath() + "/output_" + result.relationName() + "_fold_" + fold + ".stat");
			
			File file = new File(System.getProperty("user.dir") + "/files/params/out.stat");
		    Files.deleteIfExists(file.toPath());
	    
	    } catch (IOException ex) {
		    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return result;
    }
    
    @Override
    public void saveFitnessStatus(String datasetName, int i, int fold, String flag) throws FileNotFoundException, IOException {
    	String fitness = "";
        BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/files/params/out.stat"));
                    
        String line;
        while((line = reader.readLine()) != null) {
            if(line.startsWith("Fitness")) {
                String parts[] = line.split(" ");
                String num = "";
                for(int d = 9; d < parts[2].length(); d++)
                    num += parts[2].charAt(d);

                fitness += num + "\n";
            }
        }
        
        try {
            File outFolder = new File(System.getProperty("user.dir") + "/files/results/");
            outFolder.mkdir();
            
            outFolder = new File(System.getProperty("user.dir") + "/files/results/fitness/");
            outFolder.mkdir();
        
            FileManager.saveTextInfo(fitness, outFolder.getAbsolutePath() + "/ft" + 
                    datasetName.substring(3) + "_att_" + i + "_fold_" + fold + flag + ".txt");

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        reader.close();
    }

    @Override
    public void saveResult(Instances dataset, int fold, String flag) throws FileNotFoundException, IOException{
        FileManager.saveDataset(dataset, System.getProperty("user.dir") + "/files/results/imp_" + 
		dataset.relationName() + "-" + (fold-1) + flag + ".arff");
    }
}
