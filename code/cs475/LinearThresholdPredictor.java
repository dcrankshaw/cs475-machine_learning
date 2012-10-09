package cs475;

import java.io.Serializable;
import java.util.List;

public abstract class LinearThresholdPredictor implements Serializable {
	private static final long serialVersionUID = 1L;

        double thickness_;
        double mu; // Max Weight
        double learning_rate_;
        int training_iterations_;
        public ClassificationLabel oneLabel;
        private TreeMap<Integer, Double> weights;
        public LinearThresholdPredictor(double thickness,
                                        double online_learning_rate,
                                        int online_training_iterations) {
            thickness_ = thickness;
            learning_rate_ = online_learning_rate;
            training_iterations_ = online_training_iterations;
            oneLabel = new ClassificationLabel(1);
            mu = Math.pow(10.0, 6);
        }


	  public void train(List<Instance> instances) {
            int maxDim = 0;
            for (Instance i : instances) {
                int currentDim = i.getFeatureVector().dimensionality();
                if (currentDim > maxDim) {
                    maxDim = currentDim;
                }
            }
            weights = initWeights(maxDim);
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
                    if (yHat == -1 && i.getLabel().equals(oneLabel)) {
                            updateWeights(weights, -1, i);
                    } else if ((yHat == 1) && !i.getLabel().equals(oneLabel)) {
                            updateWeights(weights, 1, i);
                    } else if (yHat == 0) {
                        if (i.getLabel().equals(oneLabel)) {
                            updateWeights(weights, 1, i);
                        } else {
                            updateWeights(weights, -1, i);
                        }
                    }
                }
            }
        }

        public abstract double initBeta(int numInstances);
        public abstract updateWeights(TreeMap<Integer, Double> weights, int yHat, Instance i);
        public abstract TreeMap<Integer, Double> initWeights(int maxDim);

        public double dotProduct (TreeMap<Integer, Double> weights, Instance i) {
            double result = 0;
            for (Feature f : i.getFeatureVector()) {
                Double weight = weights.get(f.index_);
                result += weight*f.value_;
            }
            return result;
        }

    public Label predict(Instance instance) {
        double dotProd = dotProduct(weights, i);
        if (dotProd >= (beta) {
            return new ClassificationLabel(1);
        } else {
            return new ClassificationLabel(-1);
        }
  }
}
