package cs475;

import java.io.Serializable;
import java.util.*;

public class LinearKernelLogisticRegressionPredictor extends KernelLogisticRegressionPredictor {


    public LinearKernelLogisticRegressionPredictor(int iters, double rate) {
        super(iters, rate);
    }

    protected double computeKernel(FeatureVector first, FeatureVector second) {
            double kernel = 0;
            for (int i = 0; i <= first.dimensionality() && i <= second.dimensionality(); ++i) {
                kernel += first.get(i) * second.get(i);
            }
            return kernel;
    }
}
