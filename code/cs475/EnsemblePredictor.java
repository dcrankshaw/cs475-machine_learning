package cs475;

import java.io.Serializable;
import java.util.List;

public abstract class EnsemblePredictor extends Predictor {
    private static final long serialVersionUID = 1L;

    protected int numClassifiers;
    protected double learningRate;
    protected int numIterations;

    public EnsemblePredictor(int kEnsemble, double rate, int T) {
        numClassifiers = kEnsemble;
        learningRate = rate;
        numIterations = T;
    }

    public void train(List<Instance> instances) {
        HashMap<Integer, PerceptronPredictor> predictors = trainSubclassifiers(numClassifiers);

        ArrayList<EnsembleSubClassifier> classifiers = new ArrayList<EnsembleSubClassifier>(numClassifiers);
        for (Integer index : predictors.keySet()) {
            EnsembleSubClassifier current =
                new EnsembleSubClassifier(predictors.get(index), 0, index, learningRate);
            classifiers.add(current);
        }

        for (int t = 0; t < numIterations; ++t) {
            for (Instance xi : instances) {
                double sum = 0;
                for (EnsembleSubClassifier currentClassifier : classifiers) {
                    sum += currentClassifier.computeSubPrediction(xi);
                }
                int prediction = 0;
                if (sum >= 0) {
                    prediction = 1;
                }
                ClassificationLabel yHat = new ClassificationLabel(prediction);
                for (EnsembleSubClassifier currentClassifier : classifiers) {
                    currentClassifier.updateWeight(xi, yHat);
                }
            }
        }
    }

    public abstract HashMap<Integer, PerceptronPredictor> trainSubclassifiers(int k);


    public abstract Label predict(Instance instance);
}
