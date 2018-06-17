/**
* The class <code>ConstERC</code> extends the class <code>ERC</code>
* and is responsible for generating a random constant to be considered
* as a terminal in the tree of an individual.
*
* @author Oliveira de Resende, Damares
*   e-mail: d.oliveiraresende@gmail.com
*   Federal University of Para (UFPA)
*   Laboratory of Computational Intelligence and Operational Research (LINC)
**/
package terminals;

import ec.EvolutionState;
import ec.Problem;
import ec.gp.*;
import ec.util.Code;
import ec.util.DecodeReturn;
import evolution.DoubleData;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ConstERC extends ERC {
    /**
	 * Generates a random constant
	 */
	private static final long serialVersionUID = 7814709336367732416L;
	public double value;
    
    @Override
    public String toStringForHumans() { return "" + value; }
    
    @Override
    public String encode()  { return Code.encode(value); }
    
    @Override
    public boolean decode(DecodeReturn dret) {
        int pos = dret.pos;
        String data = dret.data;
        Code.decode(dret);
        if (dret.type != DecodeReturn.T_DOUBLE)  // uh oh!  Restore and signal error.
             { dret.data = data; dret.pos = pos; return false; }
        value = dret.d;
        return true;
    }
    
    @Override
    public boolean nodeEquals(GPNode node) { 
    	return (node.getClass() == this.getClass() && ((ConstERC)node).value == value);
    }
    
    @Override
    public void readNode(EvolutionState state, DataInput input) throws IOException {
    	value = input.readDouble();
    }
    
    @Override
    public void writeNode(EvolutionState state, DataOutput output) throws IOException {
    	output.writeDouble(value);
    }
    
    @Override
    public void resetNode(EvolutionState state, int thread) {
    	value = state.random[thread].nextDouble();
    }
    
    public void mutateNode(EvolutionState state, int thread) {
        double v;
        do v = value + state.random[thread].nextGaussian() * 0.01;
        while( v < 0.0 || v >= 1.0 );
        value = v;
    }

    @Override
    public void eval(EvolutionState state, int thread, GPData input, ADFStack stack, GPIndividual individual, Problem Problem) { 
    	((DoubleData)input).x = value;
    }
}
