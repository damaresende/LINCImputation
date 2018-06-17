/**
* The class <code>Mul</code> extends the class <code>GPNode</code> and
* creates a function node that takes the product of a two values in 
* the tree that represents an individual.
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

/**
 *
 * @author damaresresende
 */
public class Mul extends GPNode{
    
    /**
	 * Node that represents multiplication operation
	 */
	private static final long serialVersionUID = 897326719057191139L;

	@Override
    public String toString() {return "*"; }
    
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
        rd.x = result * rd.x;
    }
}