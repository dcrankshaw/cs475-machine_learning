package cs475;

import java.io.Serializable;
//import java.util.*;

public class EnsembleSubClassifier implements Serializable {

    private PerceptronPredictor classifier;
    private double weight;
    private int index;
    private double learningRate;

    public EnsembleSubClassifier(PerceptronPredictor predictor,
                                double mu,
                                int k,
                                double eta) {
        classifier = predictor;
        weight = mu;
        index = k;
        learningRate = eta;
    }

    // yHat is the predicted label
    public void updateWeight(Instance x, ClassificationLabel yHat) {

        if (!yHat.equals(x.getLabel())) {
            double dotProd = classifier.dotProduct(classifier.getWeights(), x);
            ClassificationLabel classLabel = (ClassificationLabel) x.getLabel();
            if (classLabel.getLabel() == 1) {
                weight = weight + learningRate * computeActivationFunction(dotProd);
            } else {
                weight = weight - learningRate * computeActivationFunction(dotProd);
            }
        }
    }

    private double computeActivationFunction(double z) {
        double denom = Math.sqrt(1 + z*z);
        return z / denom;
    }

    public double computeSubPrediction(Instance x) {
        double dotProd = classifier.dotProduct(classifier.getWeights(), x);
        return weight * computeActivationFunction(dotProd);

    }







}
