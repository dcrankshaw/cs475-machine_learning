package cs475;

import java.io.Serializable;
import java.util.*;

public class WinnowPredictor extends LinearThresholdPredictor {
    private static final long serialVersionUID = 1L;
    public WinnowPredictor(double thickness,
            double online_learning_rate,
            int online_training_iterations) {
        super(thickness, online_learning_rate, online_training_iterations);
    }

    public void initBeta(int numInstances) {
        beta_ = (double) numInstances / 2.0;
    }
    public void updateWeights(TreeMap<Integer, Double> weights, int yActual, Instance i) {
        for (Feature f : i.getFeatureVector()) {
            if (f.value_ != 0) {
                int signX = f.value_ > 0 ? 1 : -1;
                double newWeight = weights.get(f.index_) * Math.pow(learning_rate_, yActual*signX);
                if (newWeight > mu) {
                    weights.put(f.index_, mu);
                } else {
                    weights.put(f.index_, newWeight);
                }
            }
        }
    }

    public TreeMap<Integer, Double> initWeights(int maxDim) {
        TreeMap<Integer, Double> weights = new TreeMap<Integer, Double>();
        for(int i = 1; i <= maxDim; ++i) {
            weights.put(i, 1.0);
        }
        return weights;
    }


}
