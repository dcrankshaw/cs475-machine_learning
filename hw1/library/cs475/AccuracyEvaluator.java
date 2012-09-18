package cs475;

import java.util.List;

public class AccuracyEvaluator {

        public static double evaluate(List<Instance> instances, Predictor predictor) {
                double numEvaluated = 0;
                double numCorrect = 0;
                if (instances.size() == 0) {
                        return -1;
                }
                for (Instance current : instances) {
                        Label prediction = predictor.predict(current);
                        Label actual = current.getLabel();
                        if (prediction != null && actual != null) {
                                if (prediction.toString().equals(actual.toString())) {
                                        ++numCorrect;
                                }
                                ++numEvaluated;
                        }
                }
                return (numCorrect/numEvaluated);
        }
}
