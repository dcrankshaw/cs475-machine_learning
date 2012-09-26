package cs475;

import java.io.Serializable;
import java.util.List;
import java.util.*;

public class DecisionTreePredictor extends Predictor implements Serializable {
  private static final long serialVersionUID = 1L;

  private int maxDepth_;
  private Label majorityLabel_;
  private DecisionTreeNode root_;

  public DecisionTreePredictor(int maxDepth) {
    maxDepth_ = maxDepth;
    root_ = null;
    majorityLabel_ = null;
  }

  public void train(List<Instance> instances) throws IllegalArgumentException {
    if (instances.size() == 0) {
      throw new IllegalArgumentException("Cannot provide an empty set of training data.");
    }
    // Get majority label - tie goes to lowest indexed label
    // Can use a majority predictor here to get the majority label
    // Need to modify how majority predictor breaks ties though
    //MajorityPredictor majority = new MajorityPredictor(true);
    MajorityPredictor majority = new MajorityPredictor(true);
    majority.train(instances);
    majorityLabel_ = majority.getMajorityLabel();
    root_ = BuildDecisionTree(instances, new ArrayList<Integer>(), 0);
  }

  /**
   * @param usedFeatures The features that have already been split on
   *
   */
  private DecisionTreeNode BuildDecisionTree(List<Instance> data,
      List<Integer> usedFeatures,
      int depth) {
    // No Examples Base Case
    if (data.size() == 0) {
      //return node with majority label
      return new DecisionTreeNode(majorityLabel_);
    } else {
      boolean splitsPossible = false;
      for (Instance instance : data) {
        if (instance.getFeatureVector().dimensionality() > usedFeatures.size()) {
          splitsPossible = true;
          break; 
        }
      }
      // No Further Splits Possible base case
      if (!splitsPossible) {
        //return majorityLabelFORDATAATNODE - tie goes to lowest indexed label
        MajorityPredictor majority = new MajorityPredictor(true);
        majority.train(data);
        return new DecisionTreeNode(majority.getMajorityLabel());
      }
      Set<Label> possibleLabels = labels(data);
      // All Labels Agree base case
      if (possibleLabels.size() == 1) {
        Label onlyLabel = (Label) possibleLabels.toArray()[0];
        return new DecisionTreeNode(onlyLabel);
      }

      if (depth == maxDepth_) {
        //return majorityLabelFORDATAATNODE - tie goes to lowest indexed label
        MajorityPredictor majority = new MajorityPredictor(true);
        majority.train(data);
        return new DecisionTreeNode(majority.getMajorityLabel());
      }

      // At this point all base cases have been handled,
      // We now need to generate the probability distributions
      //      - P(X)
      //      - P(Y|X)

      //Create P(X)
      int maxFeatureIndex = getMaxFeatureIndex(data);
      int maxIGFeature = -1;
      double maxIGFeatureValueMean = 0;
      double minCondEntropy = Double.MAX_VALUE;
      boolean maxIGFeatureBinary = false;
      double maxIGFeatureMean = 0;
      for (int feature = 0; feature <= maxFeatureIndex; ++feature) {
        // Don't reuse features
        if (usedFeatures.contains(feature)) {
          continue;
        }
        //boolean binaryFeature = true;
        double valuesSum = 0;
        for (Instance currentInstance : data) {
          double featureValue = currentInstance.getFeatureVector().get(feature);
          valuesSum += featureValue;
          //if (binaryFeature && (featureValue != 1) && (featureValue != 0)) {
          //  binaryFeature = false;
          //}
        }
        double meanFeatureValue;
        //if (binaryFeature) {
        //  meanFeatureValue = 0;
        //} else {
          meanFeatureValue = valuesSum / (double) data.size();
        //}
        double entropy = getConditionalEntropy(feature, data, meanFeatureValue, possibleLabels);

        if (entropy < minCondEntropy) {
          minCondEntropy = entropy;
          maxIGFeature = feature;
          maxIGFeatureValueMean  = meanFeatureValue;
        }
      }

      List<Instance> zeroInstances = new ArrayList<Instance>();
      List<Instance> oneInstances = new ArrayList<Instance>();
      for (Instance current : data) {
        double featureValue = current.getFeatureVector().get(maxIGFeature);
        if (featureValue > maxIGFeatureValueMean) {
          oneInstances.add(current);
        } else {
          zeroInstances.add(current);
        }
      }

      List<Integer> updatedUsedFeatures = new ArrayList<Integer>(usedFeatures);
      updatedUsedFeatures.add(maxIGFeature);
      int newDepth = depth + 1;
      DecisionTreeNode left = BuildDecisionTree(zeroInstances, updatedUsedFeatures, newDepth);
      DecisionTreeNode right = BuildDecisionTree(oneInstances, updatedUsedFeatures, newDepth);
      DecisionTreeNode thisNode = new DecisionTreeNode(maxIGFeature,
          maxIGFeatureValueMean,
          left,
          right);
      String debugInfo = "entropy: " + minCondEntropy
                       + " feature: " + maxIGFeature
                       + " split: " + maxIGFeatureValueMean;

      thisNode.addDebugInfo(debugInfo);
      return thisNode;


    }
  }

  private static double getConditionalEntropy(int featureIndex,
      List<Instance> data,
      double mean,
      Set<Label> possibleLabels) {
    List<Instance> zeroInstances = new ArrayList<Instance>();
    List<Instance> oneInstances = new ArrayList<Instance>();
    Map<Label, Integer> zeroAndProbs = new HashMap<Label, Integer>();
    Map<Label, Integer> oneAndProbs = new HashMap<Label, Integer>();
    for (Instance current : data) {
      double featureValue = current.getFeatureVector().get(featureIndex);
      if (featureValue > mean) {
        oneInstances.add(current);
        Integer count = oneAndProbs.get(current.getLabel());
        if(count == null) {
            oneAndProbs.put(current.getLabel(), 1);
        } else {
            oneAndProbs.put(current.getLabel(), count + 1);
        }

      } else {
        zeroInstances.add(current);
        Integer count = zeroAndProbs.get(current.getLabel());
        if(count == null) {
            zeroAndProbs.put(current.getLabel(), 1);
        } else {
            zeroAndProbs.put(current.getLabel(), count + 1);
        }
      }
    }
    double num0 = zeroInstances.size();
    double num1 = oneInstances.size();
    double PX0 = num0 / ((double) data.size());
    double PX1 = num1 / ((double) data.size());
    double entropy = 0;
    for (Label yi : possibleLabels) {
      for (Instance jj : data) {
        double numMatches = 0;
        double featureValue = jj.getFeatureVector().get(featureIndex);
        double PYandX = 0;
        if (featureValue > mean && num1 != 0) {
            PYandX = ((double) oneAndProbs.get(jj.getLabel())) / ((double) data.size());
            entropy += PYandX * Math.log(PYandX / PX1) / Math.log(2);
        } else if (featureValue <= mean && num0 != 0) {
            PYandX = ((double) zeroAndProbs.get(jj.getLabel())) / ((double) data.size());
            entropy += PYandX * Math.log(PYandX / PX0) / Math.log(2);
        }
      }
        




        //for (Instance examineInstance : data) {
        //    double examineFeatureValue = examineInstance.getFeatureVector().get(featureIndex);
        //    if (examineInstance.getLabel() == jj.getLabel()) {
        //        if ((featureValue <= mean && examineFeatureValue <= mean) ||
        //            (featureValue > mean && examineFeatureValue > mean)) {
        //            numMatches += 1;
        //        }
        //    }

        //}
        //double PYandX = numMatches / (double) data.size();
        //if (featureValue <= mean && num0 != 0) {
        //    entropy += PYandX * Math.log(PYandX / PX0) / Math.log(2);
        //} else if (featureValue > mean && num1 != 0) {
        //    entropy += PYandX * Math.log(PYandX / PX1) / Math.log(2);
        //}

        //////////////////////////////////////////////
        //List<Instance> labelsToCheck;
        //if (featureValue > mean) {
        //  labelsToCheck = oneInstances;
        //} else {
        //  labelsToCheck = zeroInstances;
        //}
        //double numCurrentLabel = 0;
        //for (Instance checkLabelInstance : labelsToCheck) {
        //  if (checkLabelInstance.getLabel().equals(yi)) {
        //    ++numCurrentLabel;
        //  }
        //}
        //double PYiGivenXj;
        //if (labelsToCheck.size() > 0) {
        //  PYiGivenXj = numCurrentLabel / labelsToCheck.size();
        //} else {
        //  PYiGivenXj = 0;
        //}
        //double PYiAndXj;
        //if (featureValue > mean) {
        //  PYiAndXj = PYiGivenXj * PX1;
        //} else {
        //  PYiAndXj = PYiGivenXj * PX0;
        //}
        //double additionalEntropy = PYiAndXj * Math.log(PYiGivenXj) / Math.log(2);
        //entropy += additionalEntropy;
        /////////////////////////////////////////////////
      
    }
    entropy = entropy * -1;
    return entropy;

  }

  private int getMaxFeatureIndex(List<Instance> instances) {
    int maxIndex = 0;
    for (Instance current : instances) {
      int dim = current.getFeatureVector().dimensionality();
      maxIndex = dim > maxIndex ? dim : maxIndex;
    }
    return maxIndex;
  }

  //private Set<Label> labels(List<Instance> instances) {
  public static Set<Label> labels(List<Instance> instances) {
    Set<Label> labels = new HashSet<Label>();
    // make sure that we override Equals, Hashcode for Label subclasses so that we
    // are getting the right comparisons for Set.add()
    for (Instance current : instances) {
      labels.add(current.getLabel());
    }
    return labels;
  }

  public Label predict(Instance instance) {
    if (root_ == null) {
      throw new IllegalStateException("Cannot predict before training.");
    }
    return predictInternal(instance, root_);
  }

  private Label predictInternal(Instance instance, DecisionTreeNode currentNode) {
    if (currentNode == null) {
      throw new NullPointerException("Cannot traverse a null node.");
    }
    if (currentNode.isLeaf()) {
      return currentNode.getLabel();
    } else {
      DecisionTreeNode nextNode = currentNode.getNextNode(instance.getFeatureVector());
      return predictInternal(instance, nextNode);
    }
  }

  public void printTree() {
      printTreeInternal(root_);
  }

  private void printTreeInternal(DecisionTreeNode node) {
    if (node.isLeaf()) {
        System.out.println(node.toString());
    } else {
        printTreeInternal(node.getLeftChild());
        System.out.println(node.toString());
        printTreeInternal(node.getRightChild());
    }
  }

  public static void main(String[] args) {
    //DecisionTreePredictor predictor = new DecisionTreePredictor(4);
    ArrayList<Instance> testInstances = new ArrayList<Instance>();
    ArrayList<Label> testLabels = new ArrayList<Label>();
    for (int i = 1; i < 5; ++i) {
      testLabels.add(new ClassificationLabel(i));
      testInstances.add(new Instance(new FeatureVector(), new ClassificationLabel(i)));
    }
      testInstances.add(new Instance(new FeatureVector(), new ClassificationLabel(4)));

    Set<Label> allLabels = labels(testInstances);
    assert(allLabels.size() == 4) : "Found " + allLabels.size() + " distinct labels";
    if (allLabels.size() == 4) {
      System.out.println("right number of labels");
      for(Label label : allLabels) {
        System.out.println(label);
      }
    }



  
  }
}
