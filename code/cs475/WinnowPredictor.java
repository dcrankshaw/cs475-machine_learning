package cs475;

import java.io.Serializable;
import java.util.List;

public class WinnowPredictor extends LinearThresholdPredictor {
	private static final long serialVersionUID = 1L;


        public WinnowPredictor() {

        }

        public TreeMap<Integer, Double> initWeights() {
            
        }
        public double initBeta(int numInstances);
        public updateWeights(TreeMap<Integer, Double> weights, int yHat, Instance i);
	
}
