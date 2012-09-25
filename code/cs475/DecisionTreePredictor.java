package cs475;

import java.io.Serializable;
import java.util.List;

public class DecisionTreePredictor extends Predictor implements Serializable {
  private static final long serialVersionUID = 1L;

  private int maxDepth_;
  private Label majorityLabel_;

  public DecisionTreePredictor(int maxDepth) {
    maxDepth_ = maxDepth;
  }

  public void train(List<Instance> instances) {
    if (instances.size() == 0) {
      throw new IllegalArgumentException("Cannot provide an empty set of training data.");
    }
    // Get majority label - tie goes to lowest indexed label
    majoritylabel_ = majorityLabel;
  }

  /**
   * @param usedFeatures The features that have already been split on
   *
   */
  private DecisionTreeNode BuildDecisionTree(List<Instance> data,
      List<int> usedFeatures,
      int depth) {
    // No Examples Base Case
    if (data.size == 0) {
      //return node with majority label
    } else {
      bool splitsPossible = false;
      for (Instance instance : data) {
        if (instance.getFeatureVector().dimensionality() > usedFeatures.size()) {
          splitsPossible = true;
          break; // TODO(crankshaw) only if we aren't going to do
          // anything else with this loop
        }
      }
      // No Further Splits Possible base case
      if (!splitsPossible) {
        //return majorityLabelFORDATAATNODE - tie goes to lowest indexed label
      }
      Set<Label> labels = labels(data);
      // All Labels Agree base case
      if (labels.size() == 1) {
        //return (Label) labels.toArray()[0];
      }

      if (depth == maxDepth_) {
        //return majorityLabelFORDATAATNODE - tie goes to lowest indexed label
      }

      // At this point all base cases have been handled,
      // We now need to generate the probability distributions
      //      - P(X)
      //      - P(Y|X)

      //Create P(X)
      int maxFeatureIndex = getMaxFeatureIndex(data);
      int maxIGFeature = -1;
      for (int feature = 0; feature <= maxFeatureIndex; ++feature) {
        bool binaryFeature = true;
        double valuesSum = 0;
        for (Instance currentInstance : data) {
          double featureValue = currentInstance.getFeatureVector().get(feature);
          valuesSum += featureValue;
            if (binaryFeature && (featureValue != 1) && (featureValue != 0)) {
              binaryFeature = false;
            }
        }
        double PX0;
        double PX1;
        if (binaryFeature) {
          int num0 = 0;
          int num1 = 0;
          for (Instance currentInstance : data) {
            double featureValue = currentInstance.getFeatureVector().get(feature);
            if(featureValue == 1) {
              ++num1;
            } else {
              ++num0;
            }
          }

        } else {
          double mean = valuesSum / data.size(); //calculate the mean to split on
          int num0 = 0;
          int num1 = 0;
          for (Instance currentInstance : data) {
            double featureValue = currentInstance.getFeatureVector().get(feature);
            if (featureValue > mean) {
              ++num1;
            } else {
              ++num0;
            }

          }

        }
      }


    }

  }

  private double getConditionalEntropyBinaryFeature(int featureIndex,
                                                    List<Instance> data) {
  
  }

  private double getConditionalEntropyContinuousFeature(int featureIndex,
                                                    List<Instance> data,
                                                    double mean) {
  
  }

  private int getMaxFeatureIndex(List<Instance>) {

  }

  private Set<Label> labels(List<Instance> instances) {
    Set<Label> labels = new HashSet<Label>();
    // make sure that we override Equals, Hashcode for Label so that we
    // are getting the right comparisons for Set.add()
    for (Instance current : instances) {
      labels.add(current.getLabel());
    }
  }

  public Label predict(Instance instance);
}
