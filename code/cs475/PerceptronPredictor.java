package cs475;

import java.io.Serializable;
import java.util.List;

public class PerceptronPredictor extends LinearThresholdPredictor {
	private static final long serialVersionUID = 1L;


        public PerceptronPredictor() {

        }

        public abstract TreeMap<Integer, Double> initWeights();
        public abstract double initBeta(int numInstances);
        public abstract updateWeights(TreeMap<Integer, Double> weights, int yHat, Instance i);
	
}
