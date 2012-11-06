package cs475;

import java.util.*;

public abstract class EnsemblePredictor extends Predictor {
    private static final long serialVersionUID = 1L;

    protected int numClassifiers;
    protected double learningRate;
    protected int numIterations;
    protected ArrayList<EnsembleSubClassifier> classifiers;

    public EnsemblePredictor(int kEnsemble, double rate, int T) {
        numClassifiers = kEnsemble;
        learningRate = rate;
        numIterations = T;
        classifiers = new ArrayList<EnsembleSubClassifier>(numClassifiers);
    }


    public void train(List<Instance> instances) {
        HashMap<Integer, PerceptronPredictor> predictors = trainSubclassifiers(instances);

        for (Integer index : predictors.keySet()) {
            EnsembleSubClassifier current =
                new EnsembleSubClassifier(predictors.get(index), 0, index, learningRate);
            classifiers.add(current);
        }

        for (int t = 0; t < numIterations; ++t) {
            for (Instance xi : instances) {
                ClassificationLabel yHat = makePrediction(xi);
                for (EnsembleSubClassifier currentClassifier : classifiers) {
                    currentClassifier.updateWeight(xi, yHat);
                }
            }
        }
    }

    public abstract HashMap<Integer, PerceptronPredictor> trainSubClassifiers(List<Instance> instances);


    private ClassificationLabel makePrediction(Instance instance) {
        double sum = 0;
        for (EnsembleSubClassifier currentClassifier : classifiers) {
            sum += currentClassifier.computeSubPrediction(instance);
        }
        int prediction = 0;
        if (sum >= 0) {
            prediction = 1;
        }
        return new ClassificationLabel(prediction);
    }

    public Label predict(Instance instance) {
     return (Label) makePrediction(instance);
    }
}
