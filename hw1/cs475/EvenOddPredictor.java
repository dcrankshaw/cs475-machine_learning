package cs475;

import java.io.Serializable;
import java.util.List;

public abstract class Predictor implements Serializable {
	private static final long serialVersionUID = 1L;
        
        Label label_ = null;

	public void train(List<Instance> instances) {
                double evenSum = 0;
                double oddSum = 0;
                for (Instance current : instances) {
                        FeatureVector features = current.getFeatureVector();


                }

        }
	
	public Label predict(Instance instance);
}
