package cs475;

import java.io.Serializable;
import java.util.*;

public abstract class KernelLogisticRegressionPredictor extends Predictor implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Double> alphas;
    private List<Instance> training_instances;
    private int iterations;
    private double learningRate;


    public KernelLogisticRegressionPredictor(int iters, double rate) {
        alphas = new ArrayList<Double>();
        iterations = iters;
        learningRate = rate;
    }

    // Static helper functions

    private static double logisticFunction(double z) {
        return 1.0 / (1 + Math.exp(-1 * z));
    }

    protected abstract double computeKernel(FeatureVector first, FeatureVector second);

    protected abstract double computeTrainingKernel(FeatureVector first,
                                            int firstIndex,
                                            FeatureVector second,
                                            int secondIndex);

    public void train(List<Instance> instances) {
        int numInstances = instances.size()
                training_instances = instances;
        // Initialize alphas
        for (int i = 0; i < numInstances; ++i) {
            alphas.Add(new Double(0.0));
        }

        for (int iter = 0; iter < iterations; ++iter) {
            // Make sure we keep track of our new alphas separately
            List<Double> updatedAlphas = new ArrayList<Double>();
            for (int k = 0; k < numInstances; ++k) {
                double partial = 0;
                Instance xk = instances.get(k);
                for (int i = 0; i < numInstances; ++i) {
                    Instance xi = instances.get(i);
                    ClassificationLabel yi = (ClassificationLabel) xi.getLabel();
                    double logisticFunctionArgument = 0;
                    for (int j = 0; j < numInstances; ++j) {
                        logisticFunctionArgument += alphas.get(j) * computeTrainingKernel(
                                instances.get(j).getFeatureVector(), xi.getFeatureVector());

                    }
                    double outsideKernel = computeTrainingKernel(xi.getFeatureVector(), xk.getFeatureVector());
                    if (yi.GetLabel == 0) {
                        outsideKernel = -1 * outsideKernel;
                    } else if (yi.getLabel() == 1) {
                        logisticFunctionArgument = -1 * logisticFunctionArgument;

                    } else {
                        throw new IllegalArgumentException(
                                "Unsupported label for binary classification: " + yi.getLabel());
                    }
                    partial += logisticFunction(logisticFunctionArgument)*outsideKernel;
                }
                double updatedAlphaK = alphas.get(k) + learningRate*partial;
                updatedAlphas.set(k, updatedAlphaK);
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
            arg += alphas.get(j)*kernel;
        }
        double result = logisticFunction(arg);
        if (result < 0.5) {
            return new ClassificationLabel(0);
        } else {
            return new ClassificationLabel(1);
        }
    }
}
