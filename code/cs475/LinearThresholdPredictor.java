package cs475;

import java.io.Serializable;
import java.util.List;

public abstract class LinearThresholdPredictor implements Serializable {
	private static final long serialVersionUID = 1L;

        double thickness_;
        double learning_rate_;
        int training_iterations_;
        public ClassificationLabel oneLabel;
        public LinearThresholdPredictor(double thickness,
                                        double online_learning_rate,
                                        int online_training_iterations) {
            thickness_ = thickness;
            learning_rate_ = online_learning_rate;
            training_iterations_ = online_training_iterations;
            oneLabel = new ClassificationLabel(1);
        }


	public void train(List<Instance> instances) {
            TreeMap<Integer, Double> weights = initWeights();
            double beta = initBeta(instances.size());
            
            for (int k = 0; k < training_iterations_; ++k) {
                for (Instance i : instances) {
                    int yHat = 0;
                    double dotProd = dotProduct(weights, i);
                    if (dotProd >= (beta + thickness_) {
                        yHat = 1;
                    } else if (dotProd <= (beta - thickness_) {
                        yhat = -1;
                    }
                    if ((yHat == -1 && i.getLabel().equals(oneLabel))
                        || (yHat == 1) && !i.getLabel().equals(oneLabel)
                        || (yHat == 0)) {
                        updateWeights(weights, yHat, i);
                    }
                }
            }
            
        }

        public abstract TreeMap<Integer, Double> initWeights();
        public abstract double initBeta(int numInstances);
        public abstract updateWeights(TreeMap<Integer, Double> weights, int yHat, Instance i);

        public double dotProduct (TreeMap<Integer, Double> weights, Instance i) {
            double result = 0;
            for (Feature f : i.getFeatureVector()) {
                Double weight = weights.get(f.index_);
                if (weight != null) {
                    result += weight*f.value_;
                }
            }
             return result;
        }
	
	public abstract Label predict(Instance instance);
}
