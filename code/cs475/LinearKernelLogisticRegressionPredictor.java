package cs475;

import java.io.Serializable;
import java.util.*;

public class LinearKernelLogisticRegressionPredictor extends KernelLogisticRegressionPredictor {

    HashMap<Pair<Integer, Integer> Double> cachedKernels;

    public LinearKernelLogisticRegressionPredictor(int iters, double rate) {
        super(iters, rate);
        cachedKernels = new ArrayList<ArrayList<Double>>();
    }

    protected double computeKernel(FeatureVector first, int firstIndex, FeatureVector second, int secondIndex) {
        Pair<Double, Double> kernelIndex = new Pair<Double, Double>(
                (firstIndex <= secondIndex ? firstIndex : secondIndex),
                (firstIndex > secondIndex ? firstIndex : secondIndex));
        Double kernel = cachedKernels.get(kernelIndex);
        if (kernel == null) {
            kernel = 0;
            for (int i = 0; i <= first.dimensionality() && i <= second.dimensionality(); ++i) {
                kernel += first.get(i) * second.get(i);
            }
            cachedKernels.put(kernelIndex, kernel);
        }
        return kernel.doubleValue();
    }


}
