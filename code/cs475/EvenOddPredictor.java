package cs475;

import java.io.Serializable;
import java.util.List;

public class EvenOddPredictor extends Predictor implements Serializable {
        private static final long serialVersionUID = 1L;

        public EvenOddPredictor() {
        }

        public void train(List<Instance> instances) {
                // No-op
        }

        public Label predict(Instance instance) {
                double evenSum = 0;
                double oddSum = 0;
                Label label;
                FeatureVector features = instance.getFeatureVector();
                for (Feature currentFeature : features) {
                        if (currentFeature.index_ % 2 == 0) {
                                evenSum += currentFeature.value_;
                        } else {
                                oddSum += currentFeature.value_;
                        }
                }
                if (evenSum >= oddSum) {
                        label = new ClassificationLabel(1);
                } else {
                        label = new ClassificationLabel(0);
                }
                return label;
        }
}
