
package cs475.sum_product;
import cs475.sum_product.*;

public class SumProduct {

    private ChainMRFPotentials potentials;
    // add whatever data structures needed

    public SumProduct(ChainMRFPotentials p) {
        this.potentials = p;
    }

    public double[] marginalProbability(int x_i) {
        // TODO
    }

    /**
     * @param incremental +1 or -1 depending on whether we are going up or
     * down the chain
     */
    public int messageVariableToFactor(int incremental) {
    }

    /**
     * @param incremental +1 or -1 depending on whether we are going up or
     * down the chain
     */
    private int messageFactorToVariable(x) {
        
        double sum = 0;
        for (int xs = 1; xs <= this.potentials.numXValues(); ++xs) {
            
        }
    }


}


