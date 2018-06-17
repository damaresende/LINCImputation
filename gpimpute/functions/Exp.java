/**
* The class <code>Exp</code> extends the class <code>GPNode</code> and
* creates a function node that takes the exponential value of a value 
* in the tree that represents an individual.
*
* @author Oliveira de Resende, Damares
*   e-mail: d.oliveiraresende@gmail.com
*   Federal University of Para (UFPA)
*   Laboratory of Computational Intelligence and Operational Research (LINC)
**/
package functions;

import ec.*;
import ec.gp.*;
import ec.util.*;
import evolution.DoubleData;

import static java.lang.Math.exp;

public class Exp extends GPNode{
    
    /**
	 * Node that represents exponential operation
	 */
	private static final long serialVersionUID = -6802505188323408743L;

	@Override
    public String toString() {return "exp"; }
    
    @Override
    public void checkConstraints(final EvolutionState state, final int tree, final GPIndividual typicalIndividual, final Parameter individualBase) {
        super.checkConstraints(state,tree,typicalIndividual,individualBase);
        if (children.length!=1)
            state.output.error("Incorrect number of children for node " + toStringForError() + " at " + individualBase);
    }

    @Override
    public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem) {
        DoubleData rd = ((DoubleData)(input));
        
        children[0].eval(state,thread,input,stack,individual,problem);
        rd.x = exp(rd.x);
    }
}
