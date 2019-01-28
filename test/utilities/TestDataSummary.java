package utilities;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import data_management.DataSummary;
import essencials.ConfigurationParser;
import essencials.FileManager;
import weka.core.Instances;

class TestDataSummary {

	static ConfigurationParser config;
	
	@BeforeClass
	static void setUpBeforeClass() throws Exception {
		config = new ConfigurationParser(System.getProperty("user.dir") + "/mockFiles/config/config.txt");
	}

	@Test
	void testNumericDataset() throws IOException {
		Instances data = FileManager.loadFile(config.getInputDir() + "amp_05_AAL_RSS_1-user-movement.arff");
		assertEquals(0, DataSummary.numCategoricalAtts(data));
		assertEquals(5, DataSummary.numNumericAtts(data));
	}
	
	@Test
	void testNumericDatasetWithMVs() throws IOException {
		Instances data = FileManager.loadFile(config.getInputDir() + "amp_05_AAL_RSS_1-user-movement.arff");
		assertEquals(0, DataSummary.numCategoricalAttsWithMVs(data));
		assertEquals(3, DataSummary.numNumericAttsWithMVs(data));
	}
	
	@Test
	void testNumberOfMVs() throws IOException {
		Instances data = FileManager.loadFile(config.getInputDir() + "amp_05_AAL_RSS_1-user-movement.arff");
		assertEquals(6, DataSummary.numMissingValues(data));
	}
	
	@Test
	void testNumberOfInstancesWithMVs() throws IOException {
		Instances data = FileManager.loadFile(config.getInputDir() + "amp_05_AAL_RSS_1-user-movement.arff");
		assertEquals(6, DataSummary.numInstancesWithMVs(data));
	}
	
	@Test
	void testStatisticalSummary() throws IOException {
		Instances data = FileManager.loadFile(config.getInputDir() + "amp_05_AAL_RSS_1-user-movement.arff");
		assertEquals("amp_05_AAL-movement;5;27;0;5;3;0;3;5;18.519%,6;4.444", DataSummary.datasetStatisticalSummary(data,";"));
	}

}
