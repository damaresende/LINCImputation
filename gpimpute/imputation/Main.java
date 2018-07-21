/**
* The class <code>Main</code> runs the GPImpute and LGPImpute algorithms. 
* These algorithms build regression functions for time series data by making
* use of genetic programming, and use those functions to estimate 
* missing values.
*
* @author Oliveira de Resende, Damares
*   e-mail: d.oliveiraresende@gmail.com
*   Federal University of Para (UFPA)
*   Laboratory of Computational Intelligence and Operational Research (LINC)
**/
package imputation;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import essencials.ConfigurationParser;
import essencials.FileManager;
import weka.core.Instances;

public class Main {

    /**
     * Main method, initializes the imputation by genetic programming.
     * 
     * @param args
     */
    public static void main(String[] args)  {
		ConfigurationParser config = new ConfigurationParser(System.getProperty("user.dir") 
			+ "/files/config/config.txt");
		
		try {
		    Impute imp = new Impute();
		    
		    File resultPath = new File(config.getOutputDir());
		    resultPath.mkdir();
		    
		    File imputedPath = new File(config.getOutputDir() + "/imputed/");
		    imputedPath.mkdir();
		    
		    for(String file : config.getFileNames()) {
		    	Instances data = FileManager.loadFile(config.getInputDir() + file);
				
		    	for(int i = 0; i < config.getNumFolds(); i++) {
				    if(config.isLGPImpute()) {
						System.out.println("Running LGPImpute " + config.getNumFolds() + " times.");
						FileManager.saveTextInfo(imp.runLGP(data, config.saveFitness(), i, resultPath.getAbsolutePath()).toString(), 
							config.getOutputDir() + "/imputed/i" + file.substring(1) + "-" + i + "-LGP.arff");
				    }
				    else if(config.isNGPImpute()) {
						System.out.println("Running GPImpute " + config.getNumFolds() + " times.");
						FileManager.saveTextInfo(imp.runGP(data, config.saveFitness(), i, resultPath.getAbsolutePath()).toString(), 
							config.getOutputDir() + "/imputed/i" + file.substring(1) + "-" + i + "-NGP.arff");
				    }
				    else System.out.println("Invalid operation. You may choose NGP or LGP");
				}
		    }
		} catch (IOException ex) {
		    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
    }
}