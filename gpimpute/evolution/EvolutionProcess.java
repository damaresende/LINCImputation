/**
* The class <code>EvolutionProcess</code> contains methods to run the genetic programming
* algorithm for imputing a .arff dataset. It requires the user to indicate a pre-imputation 
* process and a fold of the process. The fold is simply the index of the imputation to add 
* to the name of the resulted dataset.
*
* @author Oliveira de Resende, Damares
*   e-mail: d.oliveiraresende@gmail.com
*   Federal University of Para (UFPA)
*   Laboratory of Computational Intelligence and Operational Research (LINC)
**/
package evolution;

import ec.EvolutionState;
import ec.Evolve;
import ec.Individual;
import ec.gp.GPIndividual;
import ec.simple.SimpleStatistics;
import ec.util.ParameterDatabase;
import essencials.FileManager;
import util.TreeEvaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instances;

public class EvolutionProcess {
    
    /**
     * Start the genetic programming process to build a regression function and
     * apply it to estimate missing vales in a specific time series.
     * 
     * @param mvdataset dataset with missing values
     * @param preimputed pre imputed dataset
     * @param index index of the time series
     * @return string with the input directory 
     * @throws java.io.FileNotFoundException 
     */
    public static double evolveGP (Instances mvdataset, Instances preimputed, int index) throws FileNotFoundException, IOException {
        EvolutionState state;
        ParameterDatabase parameters;
        
        String args[] = {"-file", System.getProperty("user.dir") + "/files/params/gpimpute.params"};
        int job = 0;
        double fitness;
        
        parameters = Evolve.loadParameterDatabase(args);

         // Initialize the EvolutionState, then set its job variables
        state = Evolve.initialize(parameters, job);                  // pass in job# as the seed increment
        state.output.systemMessage("Job: " + job);
        state.job = new Object[1];                                  // make the job argument storage
        state.job[0] = job;                                        // stick the current job in our job storage
        state.runtimeArguments = args;        

        state.run(EvolutionState.C_STARTED_FRESH);
        
        Individual bestInd =  ((SimpleStatistics)state.statistics).best_of_run[0];
        
		//read result
		BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") 
	                + "/files/params/out.stat")); 
		String line, tree = "";
	
		while((line = reader.readLine()) != null) {
		    if(line.compareToIgnoreCase("Best Individual of Run:") == 0) 
		    	break;
		}

		//get final expression
		for(int j = 0; j < 4; j++) reader.readLine();
		while((line = reader.readLine()) != null) tree += line;
		reader.close();
		
		//impute
		mvdataset.setClassIndex(index);
		for(int i = 0; i < mvdataset.numInstances(); i++) {
		    if(Double.isNaN(mvdataset.instance(i).value(mvdataset.classIndex()))) {
			double x[] = new double[preimputed.numAttributes()];
			for(int k = 0; k < preimputed.numAttributes(); k++)
			    x[k] = preimputed.instance(i).value(k);
			
			double res = TreeEvaluation.prefixEvaluation(tree, x);
			if(Double.isNaN(res) || Double.isInfinite(res))
			    mvdataset.instance(i).setClassValue(preimputed.instance(i).value(index));
			else
			    mvdataset.instance(i).setClassValue(res);
		    }
		}
	
		System.out.println("Evolution process has finished!\nExpression for Att " 
	                + index + " is: " + ((GPIndividual)bestInd).trees[0].child.makeLispTree());
		fitness = ((GPIndividual)bestInd).fitness.fitness();
        
        //write tree in file
        try {
            
            File outFolder = new File(System.getProperty("user.dir") + "/files/results/");
            outFolder.mkdir();
            
            outFolder = new File(System.getProperty("user.dir") + "/files/results/trees/");
            outFolder.mkdir();
        
            FileManager.saveTextInfo(((GPIndividual)bestInd).trees[0].child.makeLispTree() + "\n\n\n\nArvore: \n" 
                    + ((GPIndividual)bestInd).trees[0].child.makeLatexTree(), outFolder.getAbsolutePath() 
                            + "/tree" + mvdataset.relationName().substring(3) + "_att_" + index + ".txt");
        } catch (IOException ex) {
            Logger.getLogger(EvolutionProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Evolve.cleanup(state);  // flush and close various streams, print out parameters if necessary
        
        return fitness;
    }
}
