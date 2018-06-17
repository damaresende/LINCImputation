/**
* The class <code>FeatureERC</code> extends the class <code>ERC</code>
* and was coded by Tran Truong. It is responsible for generating a 
* terminal in an individual based on a value of an attribute in the dataset.
**/

package terminals;

import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.ERC;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import ec.util.Code;
import ec.util.DecodeReturn;
import evolution.DoubleData;
import evolution.OneImpute;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 *
 * @author truongtran
 */
public class FeatureERC extends ERC {
    /**
	 * Generates a terminal in an individual
	 */
	private static final long serialVersionUID = -9002043976494056693L;
	public int feature_idx;
    public static int index;
    // Koza claimed to be generating from [-1.0, 1.0] but he wasn't,
    // given the published simple-lisp code.  It was [-1.0, 1.0).  This is
    // pretty minor, but we're going to go with the code rather than the
    // published specs in the books.  If you want to go with [-1.0, 1.0],
    // just change nextDouble() to nextDouble(true, true)

    @Override
    public void resetNode(final EvolutionState state, final int thread) {
        //Create random feature index
        feature_idx = state.random[thread].nextInt(OneImpute.train.numAttributes());
        while (feature_idx == index)
            feature_idx = state.random[thread].nextInt(OneImpute.train.numAttributes());
    }

    @Override
    public int nodeHashCode() {
        // a reasonable hash code
        return this.getClass().hashCode() + Float.floatToIntBits((float)feature_idx);
    }

    @Override
    public int expectedChildren() { return 0; }

    @Override
    public boolean nodeEquals(final GPNode node) {
        // check first to see if we're the same kind of ERC --
        // won't work for subclasses; in that case you'll need
        // to change this to isAssignableTo(...)
        if (this.getClass() != node.getClass()) return false;
        // now check to see if the ERCs hold the same value
        return (((FeatureERC)node).feature_idx == feature_idx);
    }

    @Override
    public void readNode(final EvolutionState state, final DataInput dataInput) throws IOException {
        feature_idx = dataInput.readInt();
    }

    @Override
    public void writeNode(final EvolutionState state, final DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(feature_idx);
    }

    @Override
    public String encode() { return Code.encode(feature_idx); }

    @Override
    public boolean decode(DecodeReturn dret) {
        // store the position and the string in case they
        // get modified by Code.java
        int pos = dret.pos;
        String data = dret.data;

        // decode
        Code.decode(dret);

        if (dret.type != DecodeReturn.T_DOUBLE) {// uh oh!
            // restore the position and the string; it was an error
            dret.data = data;
            dret.pos = pos;
            return false;
        }

        // store the data
        feature_idx = (int)dret.d;
        return true;
    }

    @Override
    public String toStringForHumans() { return "X[" + feature_idx + "]"; }

    @Override
    public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem) {
        DoubleData rd = ((DoubleData)(input));
        rd.x = OneImpute.x[feature_idx];
    }
}
