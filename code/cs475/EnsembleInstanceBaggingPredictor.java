package cs475;

import java.util.*;

public class EnsembleInstanceBaggingPredictor extends EnsemblePredictor {

	private static final long serialVersionUID = 1L;

    public EnsembleInstanceBaggingPredictor(int kEnsemble, double rate, int T) {
        super(kEnsemble, rate, T);
    }

    public HashMap<Integer, PerceptronPredictor> trainSubClassifiers(List<Instance> instances) {
        HashMap<Integer, PerceptronPredictor> perceptrons = new HashMap<Integer, PerceptronPredictor>();
        for (int k = 0; k < numClassifiers; ++k) {
            PerceptronPredictor currentPerceptron = new PerceptronPredictor(0, 1.0, 5);
            List<Instance> instanceSubset = new ArrayList<Instance>();
            for (int i = 0; i < instances.size(); ++i) {
                if (i % numClassifiers != k) {
                    instanceSubset.add(instances.get(i));
                }
            }
            currentPerceptron.train(instanceSubset);
            perceptrons.put(k, currentPerceptron);
        }
        return perceptrons;
    }
}
