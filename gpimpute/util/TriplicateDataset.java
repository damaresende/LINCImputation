/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import weka.core.Instances;

/**
 *
 * @author damares
 */
public class TriplicateDataset {
    
    int numOriginalAtts;
    String originalName;
    
    public TriplicateDataset(Instances dataset) {
		numOriginalAtts = dataset.numAttributes();
		originalName = dataset.relationName();
    }

    public void addOneShiftedAttribute(Instances dataset, int att, int shift, int label) {
		dataset.insertAttributeAt(dataset.attribute(att).copy(dataset.attribute(att).name() + label), dataset.numAttributes());
		
		dataset.setClassIndex(dataset.numAttributes()-1);
		
		for(int x = 0; x < shift; x++)
		    dataset.instance(x).setClassValue(dataset.instance(dataset.numInstances()-shift+x).value(att));
		
		for(int i = shift; i < dataset.numInstances();i++)
		    dataset.instance(i).setClassValue(dataset.instance(i-shift).value(att));
    }

    public void duplicateDataset(Instances dataset) {
		int label = 1;
		for(int i = 1; i < numOriginalAtts; i++)
		    addOneShiftedAttribute(dataset, i, 1, label);
	
    }

    public void triplicateDataset(Instances dataset) {
		duplicateDataset(dataset);
		int label = 2;
		
		for(int i = 1; i < numOriginalAtts; i++)
		    addOneShiftedAttribute(dataset, i, 1, label);
		
		dataset.setRelationName(dataset.relationName() + "-triplicated");
    }
    
    public void reduceData(Instances dataset){
		dataset.setClassIndex(0);
		int x = findNumberOfOriginalAttributes(dataset);
		for(int i = dataset.numAttributes()-1; i >= x; i--)
		    dataset.deleteAttributeAt(i);
		dataset.setRelationName(originalName);
    }
    
    public int findNumberOfOriginalAttributes(Instances triplicated) {
		if(triplicated.relationName().contains("-triplicated"))
		    return (triplicated.numAttributes()-1)/3 + 1;
		else
		    return triplicated.numAttributes();
    }
}
