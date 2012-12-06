
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
                prod *= messageFactorToVariableDown(x_i, xs);
            }
            if (x_i < potentials.chainLength()) {
                prod *= messageFactorToVariableUp(x_i, xs);
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

    private double messageFactorToVariableUp(int leftIndex, int x) {
        double sum = 0;
        int binaryFactorIndex = potentials.chainLength() + leftIndex;
        for (int xs = 1; xs <= this.potentials.numXValues(); ++xs) {
            sum += messageVariableToFactorUp(leftIndex + 1, xs)*potentials.potential(binaryFactorIndex, x, xs);
        }
        return sum;
    }

    public double messageVariableToFactorUp(int index, int x) {
        double prod = potentials.potential(index, x);
        if (index == potentials.chainLength()) {
            return prod;
        } else {
            prod *= messageFactorToVariableUp(index, x);
        }
        return prod;
    }


    private double messageFactorToVariableDown(int rightIndex, int x) {
        double sum = 0;
        int binaryFactorIndex = potentials.chainLength() + rightIndex - 1;
        for (int xs = 1; xs <= this.potentials.numXValues(); ++xs) {
            sum += messageVariableToFactorDown(rightIndex - 1, xs)*potentials.potential(binaryFactorIndex, xs, x);
        }
        return sum;
    }

    public double messageVariableToFactorDown(int index, int x) {
        double prod = potentials.potential(index, x);
        if (index == 1) {
            return prod;
        } else {
            prod *= messageFactorToVariableDown(index, x);
        }
        return prod;
    }

}


