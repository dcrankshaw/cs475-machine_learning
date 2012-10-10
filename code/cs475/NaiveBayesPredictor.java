package cs475;

import java.io.Serializable;
import java.util.List;
import java.util.*;

public class NaiveBayesPredictor extends Predictor implements Serializable {
    private static final long serialVersionUID = 1L;
    private double lambda_;
    private double PYPosOne;
    private double PYNegOne;
    private HashMap<Integer, Double> posOneCondProb;
    private HashMap<Integer, Double> negOneCondProb;
    boolean trained_;


    public NaiveBayesPredictor(double lambda) {
        lambda_ = lambda;
        trained_ = false;
    }

    public void train(List<Instance> instances) {
        double labelPosOneCount = 0;
        double labelNegOneCount = 0;
        ClassificationLabel oneLabel = new ClassificationLabel(1);

        // Construct mean feature vector and find P(Y = 1), P(Y = -1)
        FeatureVector meanFeatureVals = new FeatureVector();
        ArrayList<Boolean> binaryFeatures = new ArrayList<Boolean>();
        ArrayList<Double> featureOccurrenceCounts = new ArrayList<Double>();

        for (Instance currentInstance : instances) {

            if (currentInstance.getLabel().equals(oneLabel)) {
                ++labelPosOneCount;
            } else {
                ++labelNegOneCount;
            }


            FeatureVector currentVector = currentInstance.getFeatureVector();
            for (Feature currentFeature : currentVector) {
                // Count the number of times we see this feature
                Double occurrenceCount = featureOccurrenceCounts.get(currentFeature.index_);
                featureOccurrenceCounts.add(currentFeature.index_,
                        occurrenceCount == null ? 1 : occurrenceCount + 1);
                double total = meanFeatureVals.get(currentFeature.index_);
                total += currentFeature.value_;
                meanFeatureVals.add(currentFeature.index_, total);
                if (currentFeature.value_ != 1) {
                    // Not a binary feature
                    binaryFeatures.add(currentFeature.index_, false);
                } else {
                    if (binaryFeatures.get(currentFeature.index_) == null) {
                        // as far as we know still a binary feature
                        binaryFeatures.add(currentFeature.index_, true);
                    } else {
                        // It's either already been marked as a binary feature and so no change is
                        // needed or it's been found to be non-binary and so just because we see a 1
                        // for this particular feature does not make this a binary feature.
                    }
                }
            }
        }

        PYPosOne = (labelPosOneCount + lambda_) / (instances.size() + 2.0 * lambda_);
        PYNegOne = (labelNegOneCount + lambda_) / (instances.size() + 2.0 * lambda_);

        // TODO make sure that refs work the way I think they do, and this is
        // updating the feature in the vector
        for (Feature current : meanFeatureVals) {
            double mean = current.value_ / featureOccurrenceCounts.get(current.index_);
            current.value_ = mean;
        }

        // Calculate conditional probalities for each feature
        // In the continuous case, each feature has two possibible values. Let
        // A be the value if xj <= mean, B be the value if xj > mean.
        // We then find the conditional probability Q = P(xj = B | y), because
        // the other probability will just be 1-Q.

        // The Hashmaps are index-->Map: "aboveMean" -> numOccurrences > mean, "total" -> num total occurrences
        String aboveMeanKey = "aboveMean";
        String totalKey = "total";
        HashMap<Integer, Map<String, Double>> posOneFeatureCounts = new HashMap<Integer, Map<String, Double>>();
        HashMap<Integer, Map<String, Double>> negOneFeatureCounts = new HashMap<Integer, Map<String, Double>>();
        for (Instance currentInstance : instances) {
            HashMap<Integer, Map<String, Double>> currentLabelCounts;
            boolean oneFeature = false;
            if (currentInstance.getLabel().equals(oneLabel)) {
                currentLabelCounts = posOneFeatureCounts;
                oneFeature = true;
            } else {
                currentLabelCounts = negOneFeatureCounts;
            }
            for (Feature currentFeature : currentInstance.getFeatureVector()) {
                Feature meanFeature = meanFeatureVals.getFeature(currentFeature.index_);
                int index = currentFeature.index_;
                if (binaryFeatures.get(index) == null || !binaryFeatures.get(index)) {
                    // Continuous feature
                    Map<String, Double> counts = currentLabelCounts.get(index);
                    if (counts == null) {
                        // Initialize counts
                        counts = new HashMap<String, Double>();
                        counts.put(totalKey, 2*lambda_);
                        counts.put(aboveMeanKey, lambda_);
                    }
                    counts.put(totalKey, counts.get(totalKey) + 1);
                    if (currentFeature.value_ > meanFeature.value_) {
                        counts.put(aboveMeanKey, counts.get(aboveMeanKey) + 1);
                    }
                } else {
                    // Binary feature
                    Map<String, Double> counts = currentLabelCounts.get(index);
                    if (counts == null) {
                        // Initialize counts
                        counts = new HashMap<String, Double>();
                        double denominator = oneFeature ? labelPosOneCount : labelNegOneCount;
                        denominator += 2*lambda_;
                        counts.put(totalKey, denominator);
                        counts.put(aboveMeanKey, lambda_);
                    }
                    counts.put(aboveMeanKey, counts.get(aboveMeanKey) + 1);
                }
            }
        }
        for (Integer index : posOneFeatureCounts.keySet()) {
            Map<String, Double> counts = posOneFeatureCounts.get(index);
            Double condProb = counts.get(aboveMeanKey) / counts.get(totalKey);
            posOneCondProb.put(index, condProb);
        }
        for (Integer index : negOneFeatureCounts.keySet()) {
            Map<String, Double> counts = negOneFeatureCounts.get(index);
            Double condProb = counts.get(aboveMeanKey) / counts.get(totalKey);
            negOneCondProb.put(index, condProb);
        }

        trained_ = true;
    }

    public Label predict(Instance instance) {
        if (!trained_) {
            throw new IllegalStateException("Must train model before predicting");
        }

        double posOneProb = Math.log(PYPosOne) / Math.log(2);
        double negOneProb = Math.log(PYNegOne) / Math.log(2);
        for (Feature feature : instance.getFeatureVector()) {
            Double posProb = posOneCondProb.get(feature.index_);
            if (posProb != null)
                posOneProb += Math.log(posProb) / Math.log(2);
            Double negProb = negOneCondProb.get(feature.index_);
            if (negProb != null)
                negOneProb += Math.log(negProb) / Math.log(2);
        }
        if(posOneProb >= negOneProb) {
            return new ClassificationLabel(1);
        } else {
            return new ClassificationLabel(0);
        }
    }
}
