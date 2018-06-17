/**
* The class <code>Div</code> extends the class <code>GPNode</code> and
* creates a function node that divide two values in the tree that 
* represents an individual.
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

public class Div extends GPNode{
    
    /**
	 * Node that represents division operation
	 */
	private static final long serialVersionUID = -8928107793433075736L;

	@Override
    public String toString() {return "//"; }
    
    @Override
    public void checkConstraints(final EvolutionState state, final int tree, final GPIndividual typicalIndividual, final Parameter individualBase) {
	    super.checkConstraints(state,tree,typicalIndividual,individualBase);
	    if (children.length!=2)
	        state.output.error("Incorrect number of children for node " + toStringForError() + " at " + individualBase);
    }

    @Override
    public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem) {
        double result;
        DoubleData rd = ((DoubleData)(input));
        
        children[0].eval(state,thread,input,stack,individual,problem);
        result = rd.x;
        
        children[1].eval(state,thread,input,stack,individual,problem);
        if(rd.x == 0) rd.x = 1;
        else rd.x = result / rd.x;
    }
}
