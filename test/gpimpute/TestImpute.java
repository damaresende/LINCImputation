package gpimpute;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import weka.core.AttributeStats;
import weka.core.Instances;
import imputation.Imputation;
import imputation.Impute;
import util.ConfigurationParser;
import util.FileManager;

/**
 *
 * @author damares
 */
public class TestImpute {
    
    Instances dataset;
    Imputation ip;
    
    @Before
    public void setUp() throws IOException {
    	dataset = FileManager.loadFile(System.getProperty("user.dir") + "/test/gpimpute/mockFiles/datasets/amp_05_AAL_RSS_1-user-movement.arff");
    }
    
    @After
    public void cleanData() {
    	dataset.clear();
    }

    @Test
    public void ignoreMissing(){
		ip = new Impute();
		for(int i = 0; i < dataset.numAttributes(); i++) {
		    Instances ignoreMissingData = ip.removeInstancesWithMV(dataset, i);
		    AttributeStats stats = ignoreMissingData.attributeStats(i);
		    assertEquals(0, stats.missingCount);
		}
    }
    
    @Test
    public void imputeByGP(){
		// ip = new Impute(); //use this if you want to test the real class
		ip = new MockImpute(); //faster test but it is just a mock test
		Instances imputedData = ip.runGP(dataset, false, 1);
		assertNotNull(imputedData);
		
		for(int i = 0; i < imputedData.numAttributes(); i++) {
		    AttributeStats stats = imputedData.attributeStats(i);
		    assertEquals(0, stats.missingCount);
		}
    }
    
    @Test
    public void imputeByLGP(){
		// ip = new Impute(); //use this if you want to test the real class
		ip = new MockImpute(); //faster test but it is just a mock test
		Instances imputedData = ip.runLGP(dataset, false, 1);
		assertNotNull(imputedData);
		
		for(int i = 0; i < imputedData.numAttributes(); i++) {
		    AttributeStats stats = imputedData.attributeStats(i);
		    assertEquals(0, stats.missingCount);
		}
    }
    
    @Test
    public void saveFitnessStatus(){
		/* uncomment the following lines if you want to test the real class
		  BE CAREFUL, it will delete all file in /files/results/fitness/ */
		
		//ip = new Impute(); 
		//File dir = new File(System.getProperty("user.dir") + "/files/results/fitness/");
		
		ip = new MockImpute(); //faster test but it is just a mock test
		File dir = new File(System.getProperty("user.dir") + "/test/gpimpute/mockFiles/results/fitness/");
		
		for(File file: dir.listFiles()) 
		    if (!file.isDirectory()) 
			file.delete();
		assertEquals(0,dir.listFiles().length);
		
		int numAttsWithMV = getNumAttsWithMVs(dataset);
		
		ip.runLGP(dataset,true,1);
		assertEquals(numAttsWithMV,dir.listFiles().length);
    }
    
    @Test
    public void saveResult() throws FileNotFoundException, IOException{
		ConfigurationParser config = new ConfigurationParser(System.getProperty("user.dir") + "/test/gpimpute/mockFiles/config/config.txt");
		config.readConfiguration();
		
		//ip = new Impute(); //use this if you want to test the real class
		ip = new MockImpute(); //faster test but it is just a mock test
		for (int fold = 1; fold <= config.getNumFolds(); fold++ ) {
			ip.saveResult(ip.runLGP(dataset, true, fold), fold, "-LGP");
		}
		File dir = new File(System.getProperty("user.dir") + "/test/gpimpute/mockFiles/results/");
		assertEquals(config.getNumFolds(),countArff(dir));
    }
    
    public int getNumAttsWithMVs(Instances dataset){
		int count = 0;
		for(int i = 0; i < dataset.numAttributes(); i++) 
	        {
		    AttributeStats stats = dataset.attributeStats(i);
		    if(stats.missingCount > 0) 
			count++;
		}
		return count;
    }
    
    public int countArff(File dir) {
		int count = 0;
		for(File f : dir.listFiles()) {
			System.out.println(f);
		    if(f.getName().endsWith(".arff"))
		    	count++;
		}
		
		return count;
    }
}
