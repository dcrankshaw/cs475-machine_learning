package cs475;

import java.io.Serializable;
import java.util.*;

public abstract class KernelLogisticRegressionPredictor extends Predictor implements Serializable {
    private static final long serialVersionUID = 1L;
    private double[] alphas;
    private List<Instance> training_instances;
    private int iterations;
    private double learningRate;
    //HashMap<Pair<Integer, Integer>, Double> cachedKernels;


    public KernelLogisticRegressionPredictor(int iters, double rate) {
        iterations = iters;
        learningRate = rate;
    }

    // Static helper functions

    private static double logisticFunction(double z) {
        return 1.0 / (1 + Math.exp(-1 * z));
    }

    protected abstract double computeKernel(FeatureVector first,
                                            FeatureVector second);

    protected double computeTrainingKernel(FeatureVector first,
                                            int firstIndex,
                                            FeatureVector second,
                                            int secondIndex,
                                            Double[][] cache) {
        int row = firstIndex < secondIndex ? firstIndex : secondIndex;
        int col = firstIndex >= secondIndex ? firstIndex : secondIndex;
        Double kernel = cache[row][col];
        if (kernel == null) {
            kernel = new Double(computeKernel(first, second));
            cache[row][col] = kernel;

        } else {
            //System.out.println("Found cached kernel: " + row + "," + col);
        }
        return kernel.doubleValue();
    }

    public void train(List<Instance> instances) {
        int numInstances = instances.size();
        Double[][] cachedKernels = new Double[numInstances][numInstances];
        training_instances = instances;
        // Initialize alphas
        alphas = new double[numInstances];
        for (int iter = 0; iter < iterations; ++iter) {
            // Make sure we keep track of our new alphas separately
            double[] updatedAlphas = new double[numInstances];
            double[] kappas = new double[numInstances];
            for (int i = 0; i < numInstances; ++i) {
                Instance xi = instances.get(i);
                double kappa = 0;
                double logisticFunctionArgument = 0;
                for (int j = 0; j < numInstances; ++j) {
                    kappa += alphas[j] * computeTrainingKernel(
                            instances.get(j).getFeatureVector(), j, xi.getFeatureVector(), i, cachedKernels);

                }
                kappas[i] = kappa;
            }
            for (int k = 0; k < numInstances; ++k) {
                double partial = 0;
                Instance xk = instances.get(k);
                for (int i = 0; i < numInstances; ++i) {
                    Instance xi = instances.get(i);
                    ClassificationLabel yi = (ClassificationLabel) xi.getLabel();
                    double logisticFunctionArgument = kappas[i];
//                    for (int j = 0; j < numInstances; ++j) {
//                        logisticFunctionArgument += alphas[j] * computeTrainingKernel(
//                                instances.get(j).getFeatureVector(), j, xi.getFeatureVector(), i, cachedKernels);
//
//                    }
                    double outsideKernel = computeTrainingKernel(xi.getFeatureVector(), i, xk.getFeatureVector(), k, cachedKernels);
                    if (yi.getLabel() == 0) {
                        outsideKernel = -1 * outsideKernel;
                    } else if (yi.getLabel() == 1) {
                        logisticFunctionArgument = -1 * logisticFunctionArgument;

                    } else {
                        throw new IllegalArgumentException(
                                "Unsupported label for binary classification: " + yi.getLabel());
                    }
                    partial += logisticFunction(logisticFunctionArgument)*outsideKernel;
                }
                double updatedAlphaK = alphas[k] + learningRate*partial;
                updatedAlphas[k] = updatedAlphaK;
            }
            // Update our alphas to the newly computed ones
            alphas = updatedAlphas;
        }
    }

    public Label predict(Instance instance) {
        double arg = 0;
        for (int j = 0; j < training_instances.size(); ++j) {
            double kernel = computeKernel(training_instances.get(j).getFeatureVector(),
                                          instance.getFeatureVector());
            arg += alphas[j]*kernel;
        }
        double result = logisticFunction(arg);
        if (result < 0.5) {
            return new ClassificationLabel(0);
        } else {
            return new ClassificationLabel(1);
        }
    }
}
