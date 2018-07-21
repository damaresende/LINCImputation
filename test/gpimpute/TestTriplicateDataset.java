package gpimpute;

import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Test;

import essencials.ConfigurationParser;
import essencials.FileManager;
import weka.core.Instances;
import util.TriplicateDataset;

/**
 *
 * @author damares
 */
public class TestTriplicateDataset {
    Instances dataset;
    TriplicateDataset td;
    static ConfigurationParser config;
    
    @Before
    public void setUp() throws IOException {
    	config = new ConfigurationParser(System.getProperty("user.dir") + "/mockFiles/config/config.txt");
		
		dataset = FileManager.loadFile(config.getInputDir() + "amp_05_AAL_RSS_1-user-movement.arff");
		td = new TriplicateDataset(dataset);
    }
    
    @After
    public void cleanData() {
    	dataset.clear();
    }
    
    @Test
    public void loadDatasetSuccessfully() throws IOException {
    	assertEquals(dataset.relationName(), "amp_05_AAL-movement");
    }
    
    @Test
    public void addOneAttribute() {
		int att = 1;
		int shift = 0;
		int label = 1;
		Instances newData = new Instances(dataset);
		td.addOneShiftedAttribute(newData, att, shift, label);
		assertEquals(newData.attribute(dataset.numAttributes()).name(), 
			dataset.attribute(att).name() + att);
    }
    
    @Test
    public void shiftOneValue() {
		int att = 1;
		int shift = 1;
		int label = 1;
		Instances newData = new Instances(dataset);
		td.addOneShiftedAttribute(newData, att, shift, label);
		assertEquals(dataset.instance(0).value(att), 
			newData.instance(shift).value(dataset.numAttributes()),0.001);
		assertEquals(dataset.instance(dataset.numInstances()-1-shift).value(att), 
			newData.instance(newData.numInstances()-1).value(dataset.numAttributes()),0.001);
    }
    
    @Test
    public void shiftNValues() {
		int att = 0;
		int shift = 10;
		int label = 1;
		Instances newData = new Instances(dataset);
		td.addOneShiftedAttribute(newData, att, shift, label);
		assertEquals(dataset.instance(0).value(att), 
			newData.instance(shift).value(dataset.numAttributes()),0.001);
		assertEquals(dataset.instance(dataset.numInstances()-1-shift).value(att), 
			newData.instance(newData.numInstances()-1).value(dataset.numAttributes()),0.001);
    }
    
    @Test
    public void duplicateDataset(){
		Instances newData = new Instances(dataset);
		td.duplicateDataset(newData);
		assertEquals(dataset.numAttributes()*2-1, newData.numAttributes());
		for(int i = 0; i < dataset.numAttributes()-2;i++)
		    assertEquals(newData.attribute(dataset.numAttributes()+i).name(),dataset.attribute(i+1).name() + "1");
    }
    
    @Test
    public void triplicateDataset(){
		Instances newData = new Instances(dataset);
		td.triplicateDataset(newData);
		assertEquals(dataset.numAttributes()*3-2, newData.numAttributes());
		
		for(int i = 0; i < dataset.numAttributes()-2;i++)
		    assertEquals(newData.attribute(dataset.numAttributes()+i).name(),dataset.attribute(i+1).name() + "1");
		
		for(int i = 0; i < dataset.numAttributes()-2;i++)
		    assertEquals(newData.attribute(dataset.numAttributes()*2-1+i).name(),dataset.attribute(i+1).name() + "2");
		
		assertEquals(dataset.relationName() + "-triplicated", newData.relationName());
    }
    
    @Test
    public void findNumberOfOriginalAttsInTriplicatedData() {
		Instances triplicated = new Instances(dataset); 
		td.triplicateDataset(triplicated);
		int x = td.findNumberOfOriginalAttributes(triplicated);
		assertEquals(dataset.numAttributes(), x);
    }
    
    @Test
    public void findNumberOfOriginalAttsInNormalData() {
		int x = td.findNumberOfOriginalAttributes(dataset);
		assertEquals(dataset.numAttributes(), x);
    }
    
    @Test
    public void reduceData() {
		Instances triplicated = new Instances(dataset); 
		td.triplicateDataset(triplicated);
		td.reduceData(triplicated);
		assertEquals(dataset.numAttributes(), triplicated.numAttributes());
		assertEquals(dataset.relationName(), triplicated.relationName());
    }
}
