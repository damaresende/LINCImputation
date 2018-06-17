/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imputation;

import java.io.FileNotFoundException;
import java.io.IOException;
import weka.core.Instances;

/**
 *
 * @author damares
 */
public interface Imputation {
    
    Instances runGP(Instances dataset, boolean saveFitness, int fold);
    Instances runLGP(Instances dataset, boolean saveFitness, int fold);
    void saveFitnessStatus(String datasetName, int i, int fold, String flag) throws FileNotFoundException, IOException;
    Instances removeInstancesWithMV(Instances dataset, int att);
    void saveResult(Instances dataset, int fold, String flag) throws FileNotFoundException, IOException;
    
}
