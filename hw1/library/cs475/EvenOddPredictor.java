package cs475;

import java.io.Serializable;
import java.util.List;

public class EvenOddPredictor extends Predictor implements Serializable {
        private static final long serialVersionUID = 1L;

        //private Label label_;
        public EvenOddPredictor() {
                //label_ = null;
        }

        public void train(List<Instance> instances) {
//                double evenSum = 0;
//                double oddSum = 0;
//                for (Instance current : instances) {
//                        FeatureVector features = current.getFeatureVector();
//                        for (Feature currentFeature : features) {
//                                if (currentFeature.index_ % 2 == 0) {
//                                        evenSum += currentFeature.value_;
//                                } else {
//                                        oddSum += currentFeature.value_;
//                                }
//                        }
//                }
//                if (evenSum >= oddSum) {
//                        label_ = new ClassificationLabel(1);
//                } else {
//                        label_ = new ClassificationLabel(0);
//                }
//                System.out.println("EvenSum: " + evenSum + " OddSum: " + oddSum);
//
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
//                System.out.println("EvenSum: " + evenSum + " OddSum: " + oddSum);
                return label;

                //return label_;
        }
}
