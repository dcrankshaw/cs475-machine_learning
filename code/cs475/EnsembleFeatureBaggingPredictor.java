package cs475;

import java.util.*;

public class EnsembleFeatureBaggingPredictor extends EnsemblePredictor {

	private static final long serialVersionUID = 1L;

    public EnsembleFeatureBaggingPredictor(int kEnsemble, double rate, int T) {
        super(kEnsemble, rate, T);
    }

    public HashMap<Integer, PerceptronPredictor> trainSubClassifiers(List<Instance> instances) {
        HashMap<Integer, PerceptronPredictor> perceptrons = new HashMap<Integer, PerceptronPredictor>();
        for (int k = 0; k < numClassifiers; ++k) {
            PerceptronPredictor currentPerceptron = new PerceptronPredictor(0, 1.0, 5);
            List<Instance> instanceSubset = new ArrayList<Instance>();
            for (Instance instance : instances) {
                FeatureVector featureSubset = new FeatureVector();
                for (Feature f : instance.getFeatureVector()) {
                    if (f.index_ % numClassifiers == k) {
                        featureSubset.add(f.index_, f.value_);
                    }
                }
                instanceSubset.add(new Instance(featureSubset, instance.getLabel()));
            }
            currentPerceptron.train(instanceSubset);
            perceptrons.put(k, currentPerceptron);
        }
        return perceptrons;
    }
}
