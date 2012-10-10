package cs475;

import java.io.Serializable;
import java.util.*;

public class NaiveBayesPredictor extends Predictor implements Serializable {
    private static final long serialVersionUID = 1L;
    private double lambda_;
    private double PYPosOne;
    private double PYNegOne;
    private HashMap<Integer, Double> posOneCondProb;
    private HashMap<Integer, Double> negOneCondProb;
    boolean trained_;
    private FeatureVector meanFeatureVals;
    private HashMap<Integer, Boolean> binaryFeatures;


    public NaiveBayesPredictor(double lambda) {
        lambda_ = lambda;
        trained_ = false;
        posOneCondProb = new HashMap<Integer, Double>();
        negOneCondProb = new HashMap<Integer, Double>();
    }

    public void train(List<Instance> instances) {
        double labelPosOneCount = 0;
        double labelNegOneCount = 0;
        ClassificationLabel oneLabel = new ClassificationLabel(1);
        List<Instance> posOneInstances = new ArrayList<Instance>();
        List<Instance> negOneInstances = new ArrayList<Instance>();

        // Construct mean feature vector and find P(Y = 1), P(Y = -1)
        FeatureVector totalFeatureVals = new FeatureVector();
        binaryFeatures = new HashMap<Integer, Boolean>();
        HashMap<Integer, Double> featureOccurrenceCounts = new HashMap<Integer, Double>();

        for (Instance currentInstance : instances) {

            if (currentInstance.getLabel().equals(oneLabel)) {
                ++labelPosOneCount;
                posOneInstances.add(currentInstance);
            } else {
                ++labelNegOneCount;
                negOneInstances.add(currentInstance);
            }


            FeatureVector currentVector = currentInstance.getFeatureVector();
            for (Feature currentFeature : currentVector) {
                // Count the number of times we see this feature
                Double occurrenceCount = featureOccurrenceCounts.get(currentFeature.index_);
                featureOccurrenceCounts.put(currentFeature.index_,
                        occurrenceCount == null ? 1 : occurrenceCount + 1);
                double total = totalFeatureVals.get(currentFeature.index_);
                total += currentFeature.value_;
                totalFeatureVals.add(currentFeature.index_, total);
                if (currentFeature.value_ != 1) {
                    // Not a binary feature
                    binaryFeatures.put(currentFeature.index_, false);
                } else {
                    if (binaryFeatures.get(currentFeature.index_) == null) {
                        // as far as we know still a binary feature
                        binaryFeatures.put(currentFeature.index_, true);
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

        meanFeatureVals = new FeatureVector();

        for (Feature current : totalFeatureVals) {
            double mean = current.value_ / featureOccurrenceCounts.get(current.index_);
            meanFeatureVals.add(current.index_, mean);
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
        /*
        for (Instance curInstance : posOneInstances) {
            for (Feature f : curInstance.getFeatureVector()) {
                Feature meanFeatureVal = meanFeatureVals.getFeature(f.index_);
                Map<String, Double> counts = posOneFeatureCounts.get(f.index_);
                if (binaryFeatures.get(f.index_)) {
                    //binary
                    if (counts == null) {
                        counts = new HashMap<String, Double>();
                        posOneFeatureCounts.put(f.index_, counts);
                        counts.put(aboveMeanKey, lambda_);
                        counts.put(totalKey, labelPosOneCount + 2*lambda_);
                    }
                    counts.put(aboveMeanKey, counts.get(aboveMeanKey) + 1);
                } else {
                    //continuous
                    if (counts == null) {
                        counts = new HashMap<String, Double>();
                        posOneFeatureCounts.put(f.index_, counts);
                        counts.put(aboveMeanKey, 1.0*lambda_);
                        counts.put(totalKey, 2.0*lambda_);
                    }
                    counts.put(totalKey, counts.get(totalKey) + 1.0);
                    if (f.value_ > meanFeatureVal.value_) {
                        counts.put(aboveMeanKey, counts.get(aboveMeanKey) + 1);
                    }
                }
            }
        }
        HashMap<Integer, Map<String, Double>> negOneFeatureCounts = new HashMap<Integer, Map<String, Double>>();
        for (Instance curInstance : negOneInstances) {
            for (Feature f : curInstance.getFeatureVector()) {
                Feature meanFeatureVal = meanFeatureVals.getFeature(f.index_);
                Map<String, Double> counts = negOneFeatureCounts.get(f.index_);
                if (binaryFeatures.get(f.index_)) {
                    //binary
                    if (counts == null) {
                        counts = new HashMap<String, Double>();
                        negOneFeatureCounts.put(f.index_, counts);
                        counts.put(aboveMeanKey, lambda_);
                        counts.put(totalKey, labelPosOneCount + 2*lambda_);
                    }
                    counts.put(aboveMeanKey, counts.get(aboveMeanKey) + 1);
                } else {
                    //continuous
                    if (counts == null) {
                        counts = new HashMap<String, Double>();
                        negOneFeatureCounts.put(f.index_, counts);
                        counts.put(aboveMeanKey, 1.0*lambda_);
                        counts.put(totalKey, 2.0*lambda_);
                    }
                    counts.put(totalKey, counts.get(totalKey) + 1.0);
                    if (f.value_ > meanFeatureVal.value_) {
                        counts.put(aboveMeanKey, counts.get(aboveMeanKey) + 1);
                    }
                }
            }
        }
        */

        
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
                        currentLabelCounts.put(index, counts);
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
                        currentLabelCounts.put(index, counts);
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
            Map<String, Double> mycounts = posOneFeatureCounts.get(index);
            Double condProb = mycounts.get(aboveMeanKey) / mycounts.get(totalKey);
            posOneCondProb.put(index, condProb);
        }
        for (Integer index : negOneFeatureCounts.keySet()) {
            Map<String, Double> counts2 = negOneFeatureCounts.get(index);
            Double condProb = counts2.get(aboveMeanKey) / counts2.get(totalKey);
            negOneCondProb.put(index, condProb);
        }
        /*
        System.out.println("Binary features:");
        for (Integer i : binaryFeatures.keySet()) {
            Boolean binary = binaryFeatures.get(i);
            if (binary) {
                System.out.println(i);
            }
        }
        */

        trained_ = true;
    }

    public Label predict(Instance instance) {
        if (!trained_) {
            throw new IllegalStateException("Must train model before predicting");
        }

        double posOneProb = Math.log(PYPosOne);
        for (Feature feature : instance.getFeatureVector()) {
            if (binaryFeatures.get(feature.index_) != null) {
                if (binaryFeatures.get(feature.index_)) {
                    //binary feature
                    Double additionalProb = posOneCondProb.get(feature.index_);
                    if (additionalProb != null) {
                        posOneProb += Math.log(additionalProb);
                    }
                } else {
                    Double meanFeatureVal = meanFeatureVals.get(feature.index_);
                    if (feature.value_ > meanFeatureVal) {
                        Double additionalProb = posOneCondProb.get(feature.index_);
                        if (additionalProb != null) {
                            posOneProb += Math.log(additionalProb);
                        }
                    } else {
                        Double oneMinusAdditionalProb = posOneCondProb.get(feature.index_);
                        if (oneMinusAdditionalProb != null) {
                            Double additionalProb = 1 - oneMinusAdditionalProb;
                            posOneProb += Math.log(additionalProb);
                        }
                    }
                }
            }
        }
        double negOneProb = Math.log(PYNegOne);
        for (Feature feature : instance.getFeatureVector()) {
            if (binaryFeatures.get(feature.index_) != null) {
                if (binaryFeatures.get(feature.index_)) {
                    //binary feature
                    Double additionalProb = negOneCondProb.get(feature.index_);
                    if (additionalProb != null) {
                        negOneProb += Math.log(additionalProb);
                    }
                } else {
                    Double meanFeatureVal = meanFeatureVals.get(feature.index_);
                    if (feature.value_ > meanFeatureVal) {
                        Double additionalProb = negOneCondProb.get(feature.index_);
                        if (additionalProb != null) {
                            negOneProb += Math.log(additionalProb);
                        }
                    } else {
                        Double oneMinusAdditionalProb = negOneCondProb.get(feature.index_);
                        if (oneMinusAdditionalProb != null) {
                            Double additionalProb = 1 - oneMinusAdditionalProb;
                            negOneProb += Math.log(additionalProb);
                        }
                    }
                }
            }
        }
        if(posOneProb >= negOneProb) {
            return new ClassificationLabel(1);
        } else {
            return new ClassificationLabel(0);
        }
    }
}
