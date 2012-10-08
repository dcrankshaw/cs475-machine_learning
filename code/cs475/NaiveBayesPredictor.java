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

    //TODO LAMBDA SMOOTHING
    public void train(List<Instance> instances) {
        double labelPosOneCount = 0;
        double labelNegOneCount = 0;
        ClassificationLabel oneLabel = new ClassificationLabel(1);
        
        // Construct mean feature vector and find P(Y = 1), P(Y = -1)
        FeatureVector meanFeatureVals = new FeatureVector();
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
        // In this case, each feature has two possibible values. Let
        // A be the value if xj <= mean, B be the value if xj > mean.
        // We then find the conditional probability Q = P(xj = B | y), because
        // the other probability will just be 1-Q.
        HashMap<Integer, Integer> posOneFeatureCounts = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> negOneFeatureCounts = new HashMap<Integer, Integer>();
        for (Instance currentInstance : instances) {
            HashMap<Integer, Integer> currentLabelCounts;
            if (currentInstance.getLabel().equals(oneLabel)) {
                currentLabelCounts = posOneFeatureCounts;
            } else {
                currentLabelCounts = negOneFeatureCounts;
            }
            for (Feature currentFeature : currentInstance.getFeatureVector()) {
                Feature meanFeature = meanFeatureVals.get(currentFeature.index_);
                int index = currentFeature.index_;
                if (currentFeature.value_ > meanFeature.value_) {
                    Integer count = currentLabelCounts.get(index);
                    //TODO Lambda smoothing
                    currentLabelCounts.put(index, count == null ? lambda : count + 1);
                }
            }
        }
        for (Integer index : posOneFeatureCounts.entrySet()) {
            Integer count = posOneFeatureCounts.get(index);
            count += lambda;
            Double condProb = count / (labelPosOneCount + 2 * lambda);
            posOneCondProb.put(index, condProb);
        }

        for (Integer index : posOneFeatureCounts.entrySet()) {
            Integer count = posOneFeatureCounts.get(index);
            count += lambda;
            Double condProb = count / (labelPosOneCount + 2 * lambda);
            posOneCondProb.put(index, condProb);
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
            // Can finish this once I have the whole zero-valued feature slash
            // unobserved features thing worked out.
            // TODO just need to finish computing the sum of the logs of 
            // the conditional probabilities
        }
    }
}
