package cs475;

import java.io.Serializable;
import java.util.List;

public class PerceptronPredictor extends LinearThresholdPredictor {
	private static final long serialVersionUID = 1L;

        public PerceptronPredictor(double thickness,
                                   double online_learning_rate,
                                   int online_training_iterations) {
            super(thickness, online_learning_rate, online_training_iterations);
        }

        public double initBeta(int numInstances) {
            return 0.0;
        }
        public updateWeights(TreeMap<Integer, Double> weights, int yActual, Instance i) {
            for (Feature f : i.getFeatureVector()) {
                double newWeight = weights.get(f.index_) + learning_rate_*yActual*f.value_;
                if (newWeight > mu) {
                    weights.put(f.index_, mu);
                } else {
                    weights.put(f.index_, newWeight);
                }
            }
        }

        public TreeMap<Integer, Double> initWeights(int maxDim) {
            TreeMap<Integer, Double> weights = new TreeMap<Integer, Double>();
            for(int i = 1; i <= maxDim; ++i) {
                weights.put(i, 0.0);
            }
            return weights;
        }
}
