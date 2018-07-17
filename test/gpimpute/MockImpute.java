package gpimpute;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import essencials.FileManager;
import imputation.Imputation;
import imputation.Impute;
import imputation.Main;
import weka.core.AttributeStats;
import weka.core.Instances;
import preimputation.Interpolation;
import preimputation.ValidNeighbor;

/**
 *
 * @author damares
 */
public class MockImpute implements Imputation {

	private String resultPath = System.getProperty("user.dir");
	
    @Override
    public Instances runGP(Instances dataset, boolean saveFitness, int fold, String resultPath) {
    	this.resultPath = resultPath;
		Instances result = new Instances(dataset);
	
		for(int i = 1; i < dataset.numAttributes(); i++) {
		    AttributeStats stats = result.attributeStats(i);
		    if(stats.missingCount > 0) {
				try {
				    if(saveFitness) saveFitnessStatus(dataset.relationName(),i,fold,"-GP");
				} catch (IOException ex) {
				    Logger.getLogger(Impute.class.getName()).log(Level.SEVERE, null, ex);
				}
		    }
		}
		ValidNeighbor.fillDataWithNeighbor(result);
		return result;
    }

    @Override
    public Instances runLGP(Instances dataset, boolean saveFitness, int fold, String resultPath) {
    	this.resultPath = resultPath;
		Instances result = new Instances(dataset);
		
		for(int i = 1; i < dataset.numAttributes(); i++) {
		    AttributeStats stats = result.attributeStats(i);
		    if(stats.missingCount > 0) {
				try {
				    if(saveFitness)
				    	saveFitnessStatus(dataset.relationName(),i,fold,"-LGP");
				} catch (IOException ex) {
				    Logger.getLogger(Impute.class.getName()).log(Level.SEVERE, null, ex);
				}
		    }
		}
		
		Interpolation.findBasePoints(result);
		return result;
    }
    
    @Override
    public void saveFitnessStatus(String datasetName, int i, int fold, String flag) throws FileNotFoundException, IOException {
		String fitness = "mock mock mock";   
		
		try {
            File outFolder = new File(resultPath);
            outFolder.mkdir();
            
            outFolder = new File(resultPath + "/fitness/");
            outFolder.mkdir();
        
            FileManager.saveTextInfo(fitness, outFolder.getAbsolutePath() + "/ft" + 
                    datasetName.substring(3) + "_att_" + i + "_fold_" + fold + flag + ".txt");

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public Instances removeInstancesWithMV(Instances dataset, int att) {
		Instances ignoreMissingData = new Instances(dataset);
		for(int i = 0; i < ignoreMissingData.numInstances(); i++) {
		    if(ignoreMissingData.instance(i).isMissing(att)) {
		    	ignoreMissingData.delete(i--);
		    }
		}
		return ignoreMissingData;
    }

    @Override
    public void saveResult(Instances dataset, int fold, String flag) throws IOException {
    	try {
            File outFolder = new File(resultPath);
            outFolder.mkdir();
        
            FileManager.saveDataset(dataset, outFolder.getAbsolutePath() + "/imp_" + 
            		dataset.relationName() + "-" + (fold-1) + flag + ".arff");

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
