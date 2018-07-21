/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_management;

import essencials.ConfigurationParser;
import essencials.FileManager;
import exceptions.InvalidMVRateException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author damares
 */
public class Main {
    
    public static void main(String args[]) {

		ConfigurationParser config = new ConfigurationParser();
		if(args.length == 1)
			config = new ConfigurationParser(args[0]);
			    
		try {
		    if(config.isToAmpute()) {
				System.out.println("Applying amputation...\n");
				Amputator amp = new Amputator();
				amp.amputeListOfFiles(config);
		    } else if(config.isToEvaluateRMSE()) {
				System.out.println("Applying RMSE evaluation...\n");
				RMSEEvaluator eval = new RMSEEvaluator();
				String result = eval.compareListOfDatasets(config);
				FileManager.saveTextInfo(result,config.getOutputDir() + "rmse.csv");
				System.out.println("Results saved to " + config.getOutputDir() + "rmse.csv");
		    } else if(config.isToEvaluateNRMSE()) {
				System.out.println("Applying NRMSE evaluation...\n");
				RMSEEvaluator eval = new RMSEEvaluator();
				String result = eval.compareListOfDatasetsNormalized(config);
				FileManager.saveTextInfo(result,config.getOutputDir() + "nrmse.csv");
				System.out.println("Results saved to " + config.getOutputDir() + "nrmse.csv");
		    } else if(config.isToEvaluateAutocorrelation()) {
				System.out.println("Applying autocorrelation evaluation...");
				AutocorrelationEvaluator acor = new AutocorrelationEvaluator();
				String result = acor.compareListOfDatasets(config);
				FileManager.saveTextInfo(result,config.getOutputDir() + "autocorrelation.csv");
				System.out.println("Results saved to " + config.getOutputDir() + "autocorrelation.csv");
		    } else if(config.isSummary()) {
				System.out.println("Building summary...");
				String result = DataSummary.statisticalSummary(config, ";");
				FileManager.saveTextInfo(result,config.getOutputDir() + "summary.csv");
				System.out.println("Summary saved to " + config.getOutputDir() + "summary.csv");
		    } else {
				System.err.println("ERROR! Invalid operation. "
					+ "It must be of type INFO, AMP, RMSE, NRMSE or ACOR.");
		    }
		} catch (IOException | InvalidMVRateException ex) {
		    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
    }
}
