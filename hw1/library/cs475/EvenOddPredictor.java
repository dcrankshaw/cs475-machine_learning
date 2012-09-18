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
                        for (Feature currentFeature : features) {
                                if (currentFeature.index_ % 2 == 0) {
                                        evenSum += currentFeature.value;
                                } else {
                                        oddSum += currentFeature.value;
                                }
                        }
                }
                if (evenSum >= oddSum) {
                        label_ = new ClassificationLabel(1);
                } else {
                        label_ = new ClassificationLabel(0);
                }

        }
	
	public Label predict(Instance instance) {
                return label_;
        }
}
