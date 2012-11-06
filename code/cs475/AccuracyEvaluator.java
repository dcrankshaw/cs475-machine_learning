package cs475;

import java.util.List;

public class AccuracyEvaluator {

    public static double evaluateClassification(List<Instance> instances, Predictor predictor) {
        double numEvaluated = 0;
        double numCorrect = 0;
        if (instances.size() == 0 || predictor == null) {
            if (predictor == null) {
                System.out.println("Null Predictor");
            }
            return 0;
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
        if (numEvaluated == 0) {
            return 0;
        } else {
            return (numCorrect/numEvaluated);
        }
    }

    public static double evaluateRegression(List<Instance> instances, Predictor predictor) {
        double error = 0;
        double numEvaluated = 0;
        if (instances.size() == 0 || predictor == null) {
            if (predictor == null) {
                System.out.println("Null Predictor");
            }
            return 0;
        }
        for (Instance current : instances) {
            RegressionLabel prediction = (RegressionLabel) predictor.predict(current);
            RegressionLabel actual = (RegressionLabel) current.getLabel();
            if (prediction != null && actual != null) {
                error += Math.abs(prediction.getLabel() - actual.getLabel());
                ++numEvaluated;
            }
        }
        if (numEvaluated == 0) {
            return 0;
        } else {
            return (error/numEvaluated);
        }
    }
}
