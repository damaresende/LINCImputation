/**
* The class <code>OneImpute</code> implements the interface 
* <code>SimpleProblemForm</code> and has methods to evaluate 
* each individual in the genetic programming process. It uses
* Koza Fitness and the class <code>Fitnesss</code> to compute 
* the fitness of each individual.
* 
* @author Oliveira de Resende, Damares
*   e-mail: d.oliveiraresende@gmail.com
*   Federal University of Para (UFPA)
*   Laboratory of Computational Intelligence and Operational Research (LINC)
**/
package evolution;

import ec.util.*;
import terminals.FeatureERC;
import ec.*;
import ec.gp.*;
import ec.gp.koza.*;
import ec.simple.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instances;

public class OneImpute extends GPProblem implements SimpleProblemForm {
    /**
	 * Evaluates each individual in the genetic programming process
	 */
	private static final long serialVersionUID = -4571485292066826765L;
	public static double x[]; //values of each attribute
    public static Instances train; //imputed dataset
    public static Instances test; //reference dataset
    public static Instances mvdataset; //dataset with mapped missing values
    public static int eval_feature_idx; //attribute/time series to be evaluated
    
    /**
     * Constructor.
     */
    public OneImpute () {
        FeatureERC.index = eval_feature_idx;
        x = new double[train.numAttributes()];
    }
    
    @Override
    public void setup(final EvolutionState state, final Parameter base) {
        // very important, remember this
        super.setup(state,base);

        // verify our input is the right class (or subclasses from it)
        if (!(input instanceof DoubleData))
            state.output.fatal("GPData class must be subclass from " + DoubleData.class,
                base.push(P_DATA), null);
    }
    
    @Override
    public void evaluate(final EvolutionState state, final Individual ind, final int subpopulation, final int threadnum) {
        if (!ind.evaluated) {  // don't bother reevaluating
            DoubleData ipt = (DoubleData)(this.input);
            int hits = 0;
            double ft = Double.MAX_VALUE;
            
		    train.setClassIndex(eval_feature_idx);
	
		    //creates the imputed time series
		    for(int k = 0; k < train.numInstances(); k++) {
				for(int f = 0; f < x.length; f++)
				    x[f] = train.instance(k).value(f);
		
				((GPIndividual)ind).trees[0].child.eval(state,threadnum,ipt,stack,((GPIndividual)ind),this);
		
				if(Double.isNaN(mvdataset.instance(k).value(eval_feature_idx)))
					train.instance(k).setClassValue(ipt.x);
		    }

	    //calculates fitness
	    try {
			double[] lts = new double[train.numInstances()];
			double[] ats = new double[test.numInstances()];
	
			for(int k = 0; k < train.numInstances(); k++)
			    lts[k] = train.instance(k).value(eval_feature_idx);
	
			for(int k = 0; k < test.numInstances(); k++)
			    ats[k] = test.instance(k).value(eval_feature_idx);

			ft = Fitness.fitness(lts, ats, 7);
			if(Double.isNaN(ft) || Double.isInfinite(ft))
				ft = Double.MAX_VALUE;
	
		    } catch (Exception ex) {
		    	Logger.getLogger(OneImpute.class.getName()).log(Level.SEVERE, null, ex);
		    }
	    
            // the fitness better be KozaFitness!
            KozaFitness f = ((KozaFitness)ind.fitness);
            f.setStandardizedFitness(state, ft);
            f.hits = hits;
            ind.evaluated = true;
        }
    }
}
