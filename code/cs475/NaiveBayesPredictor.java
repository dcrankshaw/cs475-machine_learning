package cs475;

import java.io.Serializable;
import java.util.List;

public class NaiveBayesPredictor implements Serializable {
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
        ArrayList<Double> featureOccurrenceCounts = 0;

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
                featureOccurrenceCounts.add(currentFeature.index_.
                                            occurrenceCount == null ? 1 : occurrenceCount + 1);
                double total = meanFeatureVals.get(currentFeature.index_).value_;
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

        PYPosOne = (labelPosOneCount + lambda) / (instances.size() + 2.0 * lambda);
        PYNegOne = (labelNegOneCount + lambda) / (instances.size() + 2.0 * lambda);

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
        HashMap<Integer, Map<Integer, Double>> negOneFeatureCounts = new HashMap<Integer, Map<String, Double>>();
        for (Instance currentInstance : instances) {
            HashMap<Integer, Map<String, Double>> currentLabelCounts;
            bool oneFeature = false;
            if (currentInstance.getLabel().equals(oneLabel)) {
                currentLabelCounts = posOneFeatureCounts;
                oneFeature = true;
            } else {
                currentLabelCounts = negOneFeatureCounts;
            }
            for (Feature currentFeature : currentInstance.getFeatureVector()) {
                Feature meanFeature = meanFeatureVals.get(currentFeature.index_);
                int index = currentFeature.index_;
                if (binaryFeatures.get(index) == null || !binaryFeatures.get(index)) {
                    // Continuous feature
                    Map<String, Double> counts = currentLabelCounts.get(index);
                    if (counts == null) {
                        // Initialize counts
                        counts = new HashMap<String, Double>();
                        counts.put(totalKey, 2*lambda);
                        counts.put(aboveMeanKey, lambda);
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
                        int denominator = oneFeature ? labelPosOneCount : labelNegOneCount;
                        denominator += 2*lambda;
                        counts.put(totalKey, denominator);
                        counts.put(aboveMeanKey, lambda);
                    }
                    counts.put(aboveMeanKey, counts.get(aboveMeanKey) + 1);
                    }
                }
            }
        }
        for (Integer index : posOneFeatureCounts.entrySet()) {
            Map<String, Integer> counts = posOneFeatureCounts.get(index);
            Double condProb = counts.get(aboveMeanKey) / counts.get(totalKey);
            posOneCondProb.put(index, condProb);
        }
        for (Integer index : negOneFeatureCounts.entrySet()) {
            Map<String, Integer> counts = negOneFeatureCounts.get(index);
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
            double posProb = posOneCondProb.get(feature.index_);
            posOneProb += Math.log(posProb) / Math.log(2);
            double negProb = negOneCondProb.get(feature.index_);
            negOneProb += Math.log(negProb) / Math.log(2);
            // Can finish this once I have the whole zero-valued feature slash
            // unobserved features thing worked out.
            // TODO just need to finish computing the sum of the logs of
            // the conditional probabilities
        }
        if(posOneProb >= negOneProb) {
            return new ClassificationLabel(1);
        } else {
            return new ClassificationLabel(0);
        }
    }
}
