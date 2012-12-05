
package cs475.sum_product;
import cs475.sum_product.*;

public class SumProduct {

    private ChainMRFPotentials potentials;
    // add whatever data structures needed

    public SumProduct(ChainMRFPotentials p) {
        this.potentials = p;
    }

    public double[] marginalProbability(int x_i) {
        double[] marginals = new double[potentials.numXValues() + 1];
        marginals[0] = 0;
        for (int xs = 1; xs <= potentials.numXValues(); ++xs) {
            double prod = potentials.potential(x_i, xs);
            if (x_i > 1) {
                prod *= messageFactorToVariable(-1, x_i, xs);
            }
            if (x_i < potentials.chainLength()) {
                prod *= messageFactorToVariable(1, x_i, xs);
            }
            marginals[xs] = prod;
        }
        double denom = 0;
        for (int i = 1; i < marginals.length; ++i) {
            denom += marginals[i];
        }
        for (int i = 1; i < marginals.length; ++i) {
            marginals[i] = marginals[i] / denom;
        }
        return marginals;
    }

    /**
     * @param increment +1 or -1 depending on whether we are going up or
     * down the chain
     */
    public double messageVariableToFactor(int increment, int x, int index) {
        double prod = potentials.potential(index, x);
        if (index == 1 || index == potentials.chainLength()) {
            return prod;
        } else {
            prod *= messageFactorToVariable(increment, index + increment, x);
        }
        return prod;
    }

    /**
     * @param increment +1 or -1 depending on whether we are going up or
     * down the chain
     */
    private double messageFactorToVariable(int increment, int index, int x) {
        double sum = 0;
        int binaryFactorIndex = potentials.chainLength() + index;
        if (increment < 0) {
            binaryFactorIndex -= 1;
        }
        for (int xs = 1; xs <= this.potentials.numXValues(); ++xs) {
            sum += messageVariableToFactor(increment, xs, index+increment)*potentials.potential(binaryFactorIndex, x, xs);
        }
        return sum;
    }


}


